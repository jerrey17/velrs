package com.velrs.engine.controller;

import com.alibaba.fastjson.JSON;
import com.velrs.engine.exception.RuleExpiredException;
import com.velrs.engine.exception.RuleRunnerBizException;
import com.velrs.engine.model.*;
import com.velrs.engine.service.RunInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 运行
 *
 * @Author rui
 * @Date 2021-08-08 16:45
 **/
@Slf4j
@RestController
public class RunController {

    @Autowired
    @Qualifier(value = "runCoreService")
    private RunInterface runCoreService;

    @Autowired
    @Qualifier(value = "runTestService")
    private RunInterface runTestService;

    /**
     * 运行规则接口
     *
     * @param mono 规则运行入参
     * @return 规则匹配结果
     */
    @PostMapping(value = "/run", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<RunRespModel> run(@RequestBody Mono<RunReqModel> mono) {
        return mono
                .filter(model -> {
                    String identityId = RandomStringUtils.randomNumeric(12);
                    log.info("{}-运行规则-入参:{}", identityId, model);
                    model.setIdentityId(identityId);
                    return model.simpleCheck();
                })
                .map(model -> {
                    ResultInfo resultInfo = runCoreService.run(model);
                    RunRespModel runRespModel = RunRespModel.responseSuccess(resultInfo.getResultMsg(), resultInfo.isPass());
                    log.info("{}-运行规则-成功-出参：{}", model.getIdentityId(), runRespModel);
                    return runRespModel;
                })
                .onErrorResume(this::throwDate);
    }

    /**
     * 规则测试接口
     *
     * @param mono
     * @return
     */
    @PostMapping(value = "/run/test", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<RunRespModel> runTest(@RequestBody Mono<RunReqModel> mono) {
        return mono
                .filter(model -> {
                    String identityId = RandomStringUtils.randomNumeric(12);
                    log.info("{}-TEST-入参:{}", identityId, model);
                    model.setIdentityId(identityId);
                    return model.simpleCheck();
                })
                .map(model -> {
                    ResultInfo resultInfo = runTestService.run(model);
                    RunRespModel runRespModel = RunRespModel.responseSuccess(resultInfo.getResultMsg(), resultInfo.isPass());
                    log.info("{}-TEST-成功-出参：{}", model.getIdentityId(), runRespModel);
                    return runRespModel;
                })
                .onErrorResume(this::throwDate);

    }

    /**
     * 规则配对接口
     *
     * @param mono
     * @return
     */
    @PostMapping(value = "/run/match", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<RunMatchRespModel> runMatch(@RequestBody Mono<RunMatchReqModel> mono) {
        return mono
                .filter(model -> {
                    String identityId = RandomStringUtils.randomNumeric(12);
                    log.info("{}-规则配对-入参:{}", identityId, model);
                    model.setIdentityId(identityId);
                    return model.simpleCheck();
                })
                .flatMapMany(model -> Flux.fromIterable(model.getSources()).map(source -> {
                    Optional<RunMatchRespModel> optional = model.getTargets().stream()
                            .filter(target -> !target.isEmpty())// 被匹配上的target会清空。
                            .map(target -> {
                                // fact
                                Map<String, String> fact = new HashMap<>();
                                fact.putAll(target);
                                fact.putAll(source);
                                // request param
                                RunReqModel runReqModel = new RunReqModel();
                                runReqModel.setIdentityId(model.getIdentityId());
                                runReqModel.setRuleId(model.getRuleId());
                                runReqModel.setProjectId(model.getProjectId());
                                runReqModel.setVersion(model.getVersion());
                                runReqModel.setFact(fact);
                                String sourceId = fact.get(RunMatchReqModel.SOURCE_ID);
                                String targetId = fact.get(RunMatchReqModel.TARGET_ID);
                                // match run
                                try {
                                    ResultInfo resultInfo = runCoreService.run(runReqModel);
                                    log.info("{}-规则配对-运行规则-成功-[source:{}~target:{}]出参：{}", model.getIdentityId(), sourceId, targetId, JSON.toJSONString(resultInfo));
                                    if (resultInfo.isPass()) {
                                        target.clear();// 匹配上，清空target
                                    }
                                    return RunMatchRespModel.responseByRunResponse(sourceId, targetId, resultInfo);
                                } catch (Exception e) {
                                    log.error("{}-规则配对-运行规则-异常-[source:{}~target:{}] runReqModel:{}; e:{}",
                                            model.getIdentityId(), sourceId, targetId, runReqModel, e.getMessage(), e);
                                    return null;
                                }
                            })
                            .filter(matchResp -> Objects.nonNull(matchResp) && matchResp.getRunResp().isPass())
                            .findFirst();
                    RunMatchRespModel respModel = optional.isPresent()
                            ? optional.get()
                            : RunMatchRespModel.responseMatchFailure(source.get(RunMatchReqModel.SOURCE_ID));
                    log.info("{}-规则配对-出参:{}", model.getIdentityId(), respModel);
                    return respModel;
                }))
                .onErrorResume(throwable -> {
                    log.error("规则配对-规则运行异常!", throwable);
                    if (throwable instanceof RuleRunnerBizException) {
                        RuleRunnerBizException exception = (RuleRunnerBizException) throwable;
                        log.error("{}-规则配对-运行规则异常：{}", exception.getIdentityId(), exception.getMessage());
                        return Mono.just(RunMatchRespModel.responseFailure("100001", throwable.getMessage()));
                    }
                    return Mono.just(RunMatchRespModel.responseFailure("500", throwable.getMessage()));
                });
    }

    /**
     * 规则运行异常处理
     *
     * @param throwable
     * @return
     */
    private Mono<RunRespModel> throwDate(Throwable throwable) {
        log.error("规则运行异常!", throwable);

        if (throwable instanceof RuleRunnerBizException) {
            // 运行异常
            RuleRunnerBizException exception = (RuleRunnerBizException) throwable;
            log.error("{}-运行规则异常：{}", exception.getIdentityId(), exception.getMessage());
            return Mono.just(RunRespModel.responseFailure("100001", throwable.getMessage()));

        } else if (throwable instanceof RuleExpiredException) {
            //规则表达式或参数翻译异常
            RuleExpiredException exception = (RuleExpiredException) throwable;
            log.error("{}-规则表达式异常或参数翻译异常：{}", exception.getIdentityId(), exception.getMessage());
            return Mono.just(RunRespModel.responseFailure("100002", throwable.getMessage()));
        }
        return Mono.just(RunRespModel.responseFailure("500", throwable.getMessage()));
    }
}
