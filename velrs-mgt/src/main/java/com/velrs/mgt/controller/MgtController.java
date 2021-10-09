package com.velrs.mgt.controller;

import com.velrs.mgt.controller.message.CreateReqMessage;
import com.velrs.mgt.controller.message.MessageWrapper;
import com.velrs.mgt.controller.message.SaveCompileResultReqMessage;
import com.velrs.mgt.exception.BizException;
import com.velrs.mgt.service.biz.CompileResultService;
import com.velrs.mgt.service.biz.SaveRuleService;
import com.velrs.mgt.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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


    public void saveByTest() {

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
