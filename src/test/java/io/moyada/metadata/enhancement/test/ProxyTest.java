package io.moyada.metadata.enhancement.test;

import io.moyada.metadata.enhancement.EnhanceFactory;
import io.moyada.metadata.enhancement.statement.*;
import io.moyada.metadata.enhancement.support.Annotation;
import io.moyada.metadata.enhancement.support.Value;
import io.moyada.metadata.enhancement.test.domain.Invocation;
import io.moyada.metadata.enhancement.test.domain.Monitor;
import io.moyada.metadata.enhancement.test.domain.Target;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;

/**
 * @author xueyikang
 * @since 1.0
 **/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProxyTest {

    @BeforeAll
    public static void setPrint() {
        System.setProperty("print.result", "true");
    }

    @Test
    @DisplayName("方法代理")
    public void proxyTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        String fileName = "_monitor";

        Class<Target> target = EnhanceFactory.extend(Target.class)
                .addImport(Invocation.class.getName())
                .addAnnotationToClass(Annotation.of(Resource.class))
                .addField(fileName, Monitor.class, Modifier.PRIVATE, Annotation.of(Resource.class))
                .beforeMethod("apply", Collections.<Class<?>>singletonList(String.class),
                        BodyStatement.init()
                                .addStatement(IfStatement.If(new ConditionStatement(ConditionType.NE, IdentStatement.of(fileName), IdentStatement.of("null")),
                                        BodyStatement.init()
                                                .addStatement(new VariableStatement(Invocation.class, "var1", Value.newObject(Invocation.class)))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("info"), Value.of(IdentStatement.of(1))))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("status"), Value.of("success")))
                                                .addStatement(InvokeStatement.of(IdentStatement.of(fileName), "listener", Value.of(IdentStatement.of("var1"))))
                                        )
                                )
                )
                .create();

        final Target newInstance = target.getDeclaredConstructor().newInstance();
        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                newInstance.apply("test");
            }
        });
    }

}
