package net.happykoo.toby.advisor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TxAdvice implements MethodInterceptor {
    private PlatformTransactionManager transactionManager;

    public TxAdvice(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            //프록시 콜백 메서드 호출
            Object ret = invocation.proceed();
            this.transactionManager.commit(status);
            return ret;
        } catch(Exception e) {
            this.transactionManager.rollback(status);
            throw new RuntimeException(e);
        }
    }
}
