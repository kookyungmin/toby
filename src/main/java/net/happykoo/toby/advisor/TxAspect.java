package net.happykoo.toby.advisor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Aspect
public class TxAspect {
    private PlatformTransactionManager transactionManager;

    public TxAspect(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Around("execution(* *..*ServiceImpl.upgrade*(..))")
    public Object processTransaction(ProceedingJoinPoint joinPoint) {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            //프록시 콜백 메서드 호출
            Object ret = joinPoint.proceed();
            this.transactionManager.commit(status);
            return ret;
        } catch(Throwable e) {
            this.transactionManager.rollback(status);
            throw new RuntimeException(e);
        }
    }
}
