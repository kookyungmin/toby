package net.happykoo.toby.factory;

import lombok.Setter;
import net.happykoo.toby.handler.TxInvocationHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

@Setter
public class TxProxyFactoryBean implements FactoryBean<Object> {
    private PlatformTransactionManager transactionManager;
    private Object target;
    private String pattern;
    private Class<?> targetInterface;

    public TxProxyFactoryBean(PlatformTransactionManager transactionManager,
                              Object target,
                              String pattern,
                              Class<?> targetInterface) {
        this.transactionManager = transactionManager;
        this.target = target;
        this.pattern = pattern;
        this.targetInterface = targetInterface;
    }


    @Override
    public Object getObject() {
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{ targetInterface },
                new TxInvocationHandler(transactionManager, target, pattern));
    }

    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    @Override
    public boolean isSingleton() {
        return false; //getObject() 가 매번 같은 오브젝트를 리턴할지에 대한 여부
    }
}
