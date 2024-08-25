package net.happykoo.toby.test.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {
    @Test
    @DisplayName("invoke() 테스트")
    public void invokeTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String name = "Hello";
        Class<String> clazz = String.class;

        Method lengthMethod = clazz.getMethod("length");
        assertEquals(name.length(), lengthMethod.invoke(name));

        Method charAtMethod = clazz.getMethod("charAt", int.class);
        assertEquals(name.charAt(0), charAtMethod.invoke(name, 0));
    }

    @Test
    @DisplayName("dynamic proxy 테스트")
    public void dynamicProxyTest() {
        Hello proxyHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UppercaseHandler(new HelloTarget()));

        assertEquals("HELLO HAPPYKOO", proxyHello.sayHello("happykoo"));
    }
}
