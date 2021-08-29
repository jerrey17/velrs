package com.ysf.velrs.engine.controller;

import com.ysf.velrs.engine.controller.message.CompileReqMessage;
import com.ysf.velrs.engine.controller.message.CompileRespMessage;
import com.ysf.velrs.engine.controller.message.MessageWrapper;
import com.ysf.velrs.engine.exception.CompileException;
import com.ysf.velrs.engine.service.compile.CompileHandler;
import com.ysf.velrs.engine.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

/**
 * @Author rui
 * @Date 2021-08-29 14:03
 **/
@Slf4j
@RestController
public class CompileController {

    @Autowired
    private CompileHandler compileHandler;

    /**
     * 编译
     *
     * @param message
     * @return
     */
    @PostMapping("/rule/compile")
    public MessageWrapper<CompileRespMessage> compile(@RequestBody CompileReqMessage message) {
        return process(() -> {
            ValidatorUtil.validate(message);
            CompileRespMessage respMessage = compileHandler.execute(message);
            log.info("编译结果:{}", respMessage);
            return respMessage;
        });
    }

    public <T> MessageWrapper<T> process(Supplier<T> supplier) {
        try {
            return new MessageWrapper<>(supplier.get());
        } catch (CompileException e) {
            log.error("编译时异常！", e);
            return new MessageWrapper<>("100001", e.getMessage());
        } catch (RuntimeException e) {
            log.error("系统运行时异常！", e);
            return new MessageWrapper<>(e);
        }
    }
}
