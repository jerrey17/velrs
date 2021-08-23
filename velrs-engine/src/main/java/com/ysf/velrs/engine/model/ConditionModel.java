package com.ysf.velrs.engine.model;

import lombok.Data;

import java.util.List;

/**
 * 条件
 *
 * @Author rui
 * @Date 2021-08-18 16:08
 **/
@Data
public class ConditionModel {

    private List<ExpBean> exps;

    private String logicalExp;

    @Data
    public static class ExpBean {

        /**
         * 表达式比对左边属性值
         */
        private SourceBean source;

        /**
         * 表达式右边值列表
         */
        private List<TargetBean> target;

        /**
         * 逻辑表达式拼接
         */
        private String logicalExp;

        /**
         * 表达式条件属性（左边）
         */
        @Data
        public static class SourceBean {

            private String valueType;
            private String code;
            private String name;
            private String classType;
            private String method;
            private int paramSize;

        }

        /**
         * 表达式条件值（右边）
         */
        @Data
        public static class TargetBean {

            private String valueType;
            private String value;
            private String code;
            private String name;
            private String classType;
        }
    }
}
