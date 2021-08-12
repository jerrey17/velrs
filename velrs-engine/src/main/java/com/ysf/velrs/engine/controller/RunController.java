package com.ysf.velrs.engine.controller;

import com.ysf.velrs.engine.exception.RuleExpiredException;
import com.ysf.velrs.engine.exception.RuleRunnerBizException;
import com.ysf.velrs.engine.model.ResultInfo;
import com.ysf.velrs.engine.model.RunReqModel;
import com.ysf.velrs.engine.model.RunRespModel;
import com.ysf.velrs.engine.service.RunInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
            return Mono.just(RunRespModel.responseFailure(exception.getIdentityId(), throwable.getMessage()));

        } else if (throwable instanceof RuleExpiredException) {
            //规则表达式或参数翻译异常
            RuleExpiredException exception = (RuleExpiredException) throwable;
            log.error("{}-规则表达式异常或参数翻译异常：{}", exception.getIdentityId(), exception.getMessage());
            return Mono.just(RunRespModel.responseFailure(exception.getIdentityId(), throwable.getMessage()));
        }
        return Mono.just(RunRespModel.responseFailure("500", throwable.getMessage()));
    }
}
