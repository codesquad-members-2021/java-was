package annotation;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ReflectionTest {

    @Test
    void getAnnotation() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("annotation.RequestMapping");


        Reflections reflections = new Reflections("controller");
        Set<Class<?>> getAno = reflections.getTypesAnnotatedWith(RequestMapping.class);

        for (Class<?> aClass1 : getAno) {
            RequestMapping declaredAnnotation = aClass1.getDeclaredAnnotation(RequestMapping.class);
            System.out.println(declaredAnnotation);
        }
    }
}
