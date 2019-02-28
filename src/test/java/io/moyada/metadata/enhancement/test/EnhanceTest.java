package io.moyada.metadata.enhancement.test;

import io.moyada.metadata.enhancement.EnhanceFactory;
import io.moyada.metadata.enhancement.statement.*;
import io.moyada.metadata.enhancement.support.Annotation;
import io.moyada.metadata.enhancement.support.Types;
import io.moyada.metadata.enhancement.support.Value;
import io.moyada.metadata.enhancement.test.domain.*;
import org.junit.jupiter.api.*;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author xueyikang
 * @since 1.0
 **/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnhanceTest {

    @BeforeAll
    public static void setPrint() {
        System.setProperty("write.output", "true");
    }

    @Test
    @DisplayName("增加类注解")
    public void classTest() {
        Class<Target> target = EnhanceFactory.extend(Target.class).create();
        target = EnhanceFactory.extend(Target.class).create(Target.class.getName() + "0");
        target = EnhanceFactory.copy(Target.class).create();
        target = EnhanceFactory.copy(Target.class).create(Target.class.getName() + "1");
        target = EnhanceFactory.extend(Target.class).create();
    }

    @Test
    @DisplayName("增加类注解")
    public void classAnnotationTest() {
        Class<Target> target = EnhanceFactory.copy(Target.class)
                .addAnnotationToClass(Annotation.of(Title.class).set("value", "target"))
                .create(Target.class.getName() + "classAnnotationTest");

        Title title = target.getAnnotation(Title.class);
        Assertions.assertNotNull(title);
    }

    @Test
    @DisplayName("增加方法注解")
    public void methodAnnotationTest() throws NoSuchMethodException {
        Class<Target> target = EnhanceFactory.copy(Target.class)
                .addAnnotationToMethod("apply", Types.of(String.class),
                        Annotation.of(Proxy.class))
                .create(Target.class.getName() + "methodAnnotationTest");

        Method apply = target.getDeclaredMethod("apply", String.class);
        Proxy proxy = apply.getAnnotation(Proxy.class);
        Assertions.assertNotNull(proxy);
    }

    @Test
    @DisplayName("增加字段注解")
    public void fieldAnnotationTest() throws NoSuchFieldException {
        Class<Target> target = EnhanceFactory.copy(Target.class)
                .addAnnotationToField("name", Annotation.of(Resource.class).set("name", "name"))
                .create(Target.class.getName() + "fieldAnnotationTest");

        Field name = target.getDeclaredField("name");
        Resource resource = name.getAnnotation(Resource.class);
        Assertions.assertNotNull(resource);
    }

    @Test
    @DisplayName("增加字段")
    public void fieldTest() {
        Class<Target> target = EnhanceFactory.copy(Target.class)
                .addField("serialVersionUID", long.class, Modifier.PRIVATE, Value.of(1L))
                .addField("monitor", Monitor.class, Modifier.PUBLIC)
                .create(Target.class.getName() + "fieldTest");

        Field serialVersionUID, defaultName;

        try {
            serialVersionUID = target.getDeclaredField("serialVersionUID");
        } catch (NoSuchFieldException e) {
            serialVersionUID = null;
        }

        try {
            defaultName = target.getDeclaredField("monitor");
        } catch (NoSuchFieldException e) {
            defaultName = null;
        }

        Assertions.assertNotNull(serialVersionUID);
        Assertions.assertNotNull(defaultName);
    }

    @Test
    @DisplayName("增加字段")
    public void annotationFieldTest() {
        Class<Target> target = EnhanceFactory.copy(Target.class)
                .addField("value", String.class, Modifier.PUBLIC,
                        Annotation.of(Resource.class).set("name", "target"))
                .create();

        Field monitor;
        Resource annotation;
        try {
            monitor = target.getDeclaredField("value");
            annotation = monitor.getAnnotation(Resource.class);
        } catch (NoSuchFieldException e) {
            monitor = null;
            annotation = null;
        }

        Assertions.assertNotNull(monitor);
        Assertions.assertNotNull(annotation);
    }

    @Test
    @DisplayName("增加方法")
    public void methodTest() {
        Class<Target> target = EnhanceFactory.copy(Target.class)
                .addMethod("set", Types.of(String.class), null, null,
                        Modifier.PUBLIC, null,
                        Annotation.of(Proxy.class))
                .addMethod("get", null, String.class, null,
                        Modifier.PUBLIC,
                        BodyStatement.init()
                                .addStatement(new ReturnStatement(Value.of(IdentStatement.of("this.name"))))
                )
                .create();

        Method set, get;
        try {
            set = target.getDeclaredMethod("set", String.class);
        } catch (NoSuchMethodException e) {
            set = null;
        }
        try {
            get = target.getDeclaredMethod("get");
        } catch (NoSuchMethodException e) {
            get = null;
        }

        Assertions.assertNotNull(set);
        Assertions.assertNotNull(get);
    }

    @Test
    @DisplayName("增强方法")
    public void proxyMethodTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Class<Target> target = EnhanceFactory.extend(Target.class)
                .addImport(Invocation.class)
                .beforeMethod("apply", Types.of(String.class),
                        BodyStatement.init()
                                .addStatement(IfStatement.If(new ConditionStatement(ConditionType.NE, IdentStatement.of("this.name"), IdentStatement.of("null")),
                                        BodyStatement.init()
                                                .addStatement(new VariableStatement(Invocation.class, "var1", Value.newObject(Invocation.class)))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("name"), Value.of(IdentStatement.of("name"))))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("info"), Value.of(IdentStatement.of(1))))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("status"), Value.of("success")))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "getData").assign(IdentStatement.of(1)))
                                        )
                                )
                )
                .create();

        Field name = Target.class.getDeclaredField("name");
        name.setAccessible(true);

        Target instance = target.getDeclaredConstructor().newInstance();
        name.set(instance, "enhance");

        Assertions.assertEquals(new Target().apply("test"), "test");
        Assertions.assertNotEquals(instance.apply("test"), "test");
    }

    @Test
    @DisplayName("增强方法")
    public void proxySuperMethodTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Class<Target> target = EnhanceFactory.copy(Target.class)
                .addImport(Invocation.class)
                .beforeMethod("say", Types.of(String.class),
                        BodyStatement.init()
                                .addStatement(IfStatement.If(new ConditionStatement(ConditionType.NE, IdentStatement.of(1), IdentStatement.NULL),
                                        BodyStatement.init()
                                                .addStatement(new VariableStatement(Invocation.class, "var1", Value.newObject(Invocation.class)))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("name"), Value.of(IdentStatement.of("name"))))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("info"), Value.of(IdentStatement.of(1))))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("status"), Value.of("success")))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "getData").assign(IdentStatement.of(1)))
                                        )
                                )
                )
                .create();

        Method say = target.getDeclaredMethod("say", String.class);
        say.setAccessible(true);

        Object instance = target.getDeclaredConstructor().newInstance();
        say.invoke(instance, "haha");
    }
}
