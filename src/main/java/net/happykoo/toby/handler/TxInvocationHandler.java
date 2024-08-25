package net.happykoo.toby.handler;

import lombok.Setter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TxInvocationHandler implements InvocationHandler  {
    private PlatformTransactionManager transactionManager;
    private Object target;
    private String pattern; //트랜잭션을 적용할 메서드 이름 패턴

    public TxInvocationHandler(PlatformTransactionManager transactionManager,
                               Object target,
                               String pattern) {
        this.transactionManager = transactionManager;
        this.target = target;
        this.pattern = pattern;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().startsWith(pattern)) { //메서드 명이 pattern 으로 시작하면 트랜잭션 처리
            return invokeInTransaction(method, args);
        } else { //아니면 타깃에 그대로 위임
            return method.invoke(target, args);
        }
    }

    private Object invokeInTransaction(Method method, Object[] args) {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object ret = method.invoke(target, args);
            this.transactionManager.commit(status);
            return ret;
        } catch(Exception e) {
            this.transactionManager.rollback(status);
            throw new RuntimeException(e);
        }
    }
}
