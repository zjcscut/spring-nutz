package org.throwable.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;

/**
 * @author zhangjinci
 * @version 2017/1/24 17:44
 * @function
 */
@Component
public class SysTransactionInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(SysTransactionInterceptor.class);

    private static final int ISOLATION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
    private static final int PROPAGATION_REQUIRES_NEW = TransactionDefinition.PROPAGATION_REQUIRED;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
    	logger.debug("execute sysTransactionInterceptor ... ");
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setIsolationLevel(ISOLATION_REPEATABLE_READ);
        def.setPropagationBehavior(PROPAGATION_REQUIRES_NEW);
        def.setName(joinPoint.toString());
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        Object object = null;
        try {
            object = joinPoint.proceed();
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
        return object;
    }



}
