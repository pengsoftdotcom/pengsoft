package com.pengsoft.support.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.pengsoft.support.config.properties.TransactionConfigurationProperties;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * Transaction auto configure.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(TransactionConfigurationProperties.class)
public class DefaultTransactionConfigurer {

    /**
     * Transaction interceptor
     */
    @Bean
    public TransactionInterceptor transactionAdvice(final TransactionManager transactionManager,
            final TransactionConfigurationProperties properties) {
        final var readOnlyTransaction = new RuleBasedTransactionAttribute();
        readOnlyTransaction.setReadOnly(true);
        readOnlyTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);

        final var requiredTransaction = new RuleBasedTransactionAttribute();
        requiredTransaction.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiredTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        final var transactionAttributes = new HashMap<String, TransactionAttribute>(16);
        properties.getReadonly().forEach(property -> transactionAttributes.put(property, readOnlyTransaction));
        properties.getRequired().forEach(property -> transactionAttributes.put(property, requiredTransaction));

        final var transactionAttributeSource = new NameMatchTransactionAttributeSource();
        transactionAttributeSource.setNameMap(transactionAttributes);

        return new TransactionInterceptor(transactionManager, transactionAttributeSource);
    }

    /**
     * Register transaction
     */
    @Bean
    public Advisor transactionAdviceAdvisor(final TransactionInterceptor transactionAdvice) {
        final var pointcut = new AspectJExpressionPointcut();
        final var expressions = new ArrayList<String>();
        expressions.add("execution(public * com.pengsoft..service.*Service.*(..))");
        expressions.add("execution(public * com.pengsoft..facade.*Facade.*(..))");
        expressions.add("execution(public * com.pengsoft..aspect.*Aspect.*(..))");
        pointcut.setExpression(String.join("||", expressions));
        return new DefaultPointcutAdvisor(pointcut, transactionAdvice);
    }

}
