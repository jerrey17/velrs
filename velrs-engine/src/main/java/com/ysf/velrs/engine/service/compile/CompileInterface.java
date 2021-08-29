package com.ysf.velrs.engine.service.compile;

/**
 * 表达式编译
 *
 * @Author rui
 * @Date 2021-08-18 16:28
 **/
public interface CompileInterface {

    /**
     * 获取表达式对象
     *
     * @return
     */
    String getExpObj();

    /**
     * 获取逻辑表达式
     *
     * @return
     */
    String getLogic();

    /**
     * 获取表达式语句
     *
     * @return
     */
    String getExp();

    /**
     * 获取变量名称
     *
     * @return
     */
    String getName();

}
