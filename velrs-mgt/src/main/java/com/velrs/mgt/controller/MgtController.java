package com.velrs.mgt.controller;

import com.velrs.mgt.controller.message.CreateReqMessage;
import com.velrs.mgt.controller.message.MessageWrapper;
import com.velrs.mgt.controller.message.SaveCompileResultReqMessage;
import com.velrs.mgt.exception.BizException;
import com.velrs.mgt.service.biz.CompileResultService;
import com.velrs.mgt.service.biz.SaveRuleService;
import com.velrs.mgt.service.biz.TestResultService;
import com.velrs.mgt.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 管理后台接口
 *
 * @Author rui
 * @Date 2021-10-08 15:03
 **/
@Slf4j
@RestController
public class MgtController {

    @Autowired
    private SaveRuleService saveRuleService;
    @Autowired
    private CompileResultService compileResultService;
    @Autowired
    private TestResultService testResultService;

    /**
     * 创建和编辑规则接口开发
     * 编译结果保存开发
     * 测试结果保存开发
     * 规则发布接口开发
     */

    /**
     * 创建或更新规则
     *
     * @param reqMessage
     * @return
     */
    @PostMapping("velrs/mgt/create_or_update")
    public @ResponseBody
    MessageWrapper<String> createOrUpdate(@RequestBody CreateReqMessage reqMessage) {
        return process(() -> {
            ValidatorUtil.validate(reqMessage);
            saveRuleService.saveOrUpdate(reqMessage);
            return reqMessage.getRuleId();
        });
    }

    /**
     * 编译结果保存
     *
     * @param reqMessage
     * @returnxx
     */
    @PostMapping("velrs/mgt/compile_result/save")
    public @ResponseBody
    MessageWrapper<String> saveByCompile(@RequestBody SaveCompileResultReqMessage reqMessage) {
        return process(() -> {
            ValidatorUtil.validate(reqMessage);
            compileResultService.saveCompileResult(reqMessage);
            return reqMessage.getRuleId();
        });
    }


    /**
     * 保存测试结果
     *
     * @param ruleId
     * @param result
     */
    @PostMapping("velrs/mgt/{ruleId}/test/{result}")
    public MessageWrapper<String> saveByTest(@PathVariable String ruleId, @PathVariable Boolean result) {
        return process(() -> {
            if(StringUtils.isBlank(ruleId) || Objects.isNull(result)) {
                throw new BizException("参数错误");
            }
            if(!result.booleanValue()) {
                throw new BizException("测试失败，不允许保存");
            }
            testResultService.saveTestPass(ruleId);
            return ruleId;
        });
    }

    public void publish() {

    }

    public <T> MessageWrapper<T> process(Supplier<T> supplier) {
        try {
            return new MessageWrapper<>(supplier.get());
        } catch (BizException e) {
            log.error("业务异常！", e);
            return new MessageWrapper<>(e);
        } catch (Exception e) {
            log.error("系统异常！", e);
            return new MessageWrapper<>(e);
        }
    }
}
