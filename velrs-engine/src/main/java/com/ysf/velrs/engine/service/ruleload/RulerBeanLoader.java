package com.ysf.velrs.engine.service.ruleload;

import com.ysf.velrs.engine.constant.RuleRunnerConstant;
import com.ysf.velrs.engine.model.RuleCodeModel;
import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * 规则加载器
 *
 * @Author rui
 * @Date 2021-08-08 10:45
 **/
@Slf4j
@Component
public class RulerBeanLoader implements InitializingBean {

    private DefaultListableBeanFactory beanFactory;
    private GroovyClassLoader groovyClassLoader = new GroovyClassLoader();

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;
    @Autowired
    private RuleRegistry ruleRegistry;

    @Override
    public void afterPropertiesSet() {
        beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        beanFactory.setAllowBeanDefinitionOverriding(true);
    }

    /**
     * 注册规则
     *
     * @param ruleCodeModel
     * @return
     */
    public void registerRule(RuleCodeModel ruleCodeModel) {
        String javaCode = new String(Base64.getDecoder().decode(ruleCodeModel.getRuleClassCode()));
        Class rClass = groovyClassLoader.parseClass(javaCode);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(rClass);
        // TODO configurableApplicationContext.getBean(RunnerContext.class) 可能存在多个实例？
        beanDefinitionBuilder.addPropertyValue(RuleRunnerConstant.RUN_CONTEXT_BEAN_NAME, configurableApplicationContext.getBean(RunnerContext.class));
        //注册bean实例
        beanFactory.registerBeanDefinition(ruleCodeModel.getBeanName(), beanDefinitionBuilder.getBeanDefinition());
        //注册到注册表
        ruleRegistry.registerRule(ruleCodeModel.getBeanName(), ruleCodeModel);
        log.info(">>>rule-bean[{}] register success...", ruleCodeModel.getBeanName());
    }

    /**
     * 销毁规则
     *
     * @param beanName
     */
    public void destroyRule(String beanName) {
        ruleRegistry.destroy(beanName);
        beanFactory.destroySingleton(beanName);
        log.info(">>>rule-bean[{}] destroy success...", beanName);
    }

}
