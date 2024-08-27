package net.happykoo.toby.advisor;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {
    public void setMappedClassName(String classNamePattern) {
        this.setClassFilter(new SimpleClassFilter(classNamePattern));
    }

    private static class SimpleClassFilter implements ClassFilter {
        private String pattern;
        private SimpleClassFilter(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public boolean matches(Class<?> clazz) {
            //패턴으로 클래스 이름을 매칭
            return PatternMatchUtils.simpleMatch(pattern, clazz.getSimpleName());
        }
    }
}
