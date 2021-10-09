package com.velrs.mgt.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 条件
 *
 * @Author rui
 * @Date 2021-08-18 16:08
 **/
@Data
public class ConditionModel {

    @NotNull(message = "exps不能为空")
    @Valid
    private List<ExpBean> exps;

    private String logicalExp;

    @Data
    public static class ExpBean {

        /**
         * 表达式比对左边属性值
         */
        @NotNull(message = "exps.source不能为空")
        @Valid
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

            @Size(max = 64, message = "source.code不超过{max}位")
            @NotBlank(message = "source.code不能为空")
            private String code;
            @Size(max = 128, message = "source.name不超过{max}位")
            @NotBlank(message = "source.name不能为空")
            private String name;
            @Size(max = 128, message = "source.classType不超过{max}位")
            @NotBlank(message = "source.classType不能为空")
            private String classType;
            @Size(max = 128, message = "source.method不超过{max}位")
            @NotBlank(message = "source.method不能为空")
            private String method;
            @Min(value = 0, message = "source.paramSize不能小于0")
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
        }
    }
}
