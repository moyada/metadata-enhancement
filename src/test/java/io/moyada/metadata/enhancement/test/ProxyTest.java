package io.moyada.metadata.enhancement.test;

import io.moyada.metadata.enhancement.ProxyFactory;
import io.moyada.metadata.enhancement.statement.*;
import io.moyada.metadata.enhancement.support.Annotation;
import io.moyada.metadata.enhancement.support.Value;
import javassist.ClassPool;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ProxyTest {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<ModelTest> model = ProxyFactory.extend(ModelTest.class)
                .addImport(Invocation.class.getName())
                .addField("_monitor", Monitor.class, Modifier.PRIVATE, Value.newObject(Monitor.class))
                .addAnnotationToMethod("toName", null,
                        Annotation.of(Catch.class).set("value", "model"))
                .beforeMethod("toName", null,
                        BodyStatement.init()
                                .addStatement(InvokeStatement.of(IdentStatement.of("System.out"), "println", Value.of("ok")))
                                .addStatement(IfStatement.If(new ConditionStatement(ConditionType.NE, IdentStatement.of("_monitor"), IdentStatement.of("null")),
                                        BodyStatement.init()
                                                .addStatement(new VariableStatement(Invocation.class, "_tmp", Value.newObject(Invocation.class)))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("_tmp"), "addArgs", Value.of("test")))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("_tmp"), "addArgs", Value.of(" => ")))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("_tmp"), "addArgs", Value.of("success")))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("_monitor"), "listener", Value.of(IdentStatement.of("_tmp"))))
                                        )
                                )
                )
                .create();

        model.newInstance().toName();

        test();
    }

    private static void base() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {

        Class<Model> proxy = ProxyFactory.extend(Model.class)
                .addImport(ClassPool.class.getName())
                .addField("ClassPool", ClassPool.class, Modifier.PUBLIC)
                .addAnnotationToClass(Annotation.of(Title.class).set("value", "model"))
                .addAnnotationToField("id", Annotation.of(Param.class).set("id", 12))
                .addAnnotationToMethod("toName", null,
                        Annotation.of(Catch.class).set("value", "model"))
                .addField("name", String.class, Modifier.PUBLIC,
                        Annotation.of(Data.class)
                                .set("value", "data")
                                .set("iv", 123)
                                .set("cv", 'c')
                                .set("fv", 123.12F)
                                .set("clazz", ProxyTest.class)
                                .set("array", new int[]{1, 2, 3})
                )
                .addField("name1", String.class, Modifier.PUBLIC, Value.staticMethod(Model.class, "getName", 1))
                .addField("shenme", int[].class, Modifier.PUBLIC)
                .addField("test", int.class, Modifier.STATIC, Value.ref(Modifier.class, "PUBLIC"))
                .addField("type", Class.class, Modifier.PUBLIC, Value.of(Model.class))
                .addField("owner", Model.class, Modifier.PUBLIC, Value.newObject(Model.class, '2'))
                .addField("mode", Mode.class, Modifier.PUBLIC | Modifier.STATIC, Value.ref(Mode.NEW))
                .addField("monitor", Monitor.class, Modifier.PRIVATE)
                .addMethod("run", null, null, null,  Modifier.PUBLIC,
                        BodyStatement.init().addStatement(InvokeStatement.of(IdentStatement.of("System.out"), "println", Value.of("ok"))))
                .addMethod("init", Collections.<Class<?>>singletonList(Mode.class), void.class, null,  Modifier.PUBLIC | Modifier.STATIC, null)
                .beforeMethod("run", null,
                        BodyStatement.init()
                                .addStatement(new VariableStatement(String.class, "var1", Value.of("666")))
                                .addStatement(IfStatement.If(new ConditionStatement(ConditionType.NE, IdentStatement.of("var1"), IdentStatement.of("null")),
                                        BodyStatement.init()
                                                .addStatement(InvokeStatement.of(IdentStatement.of("System.out"), "println", Value.of(100)))
                                                .addStatement(new ForLoopStatement(
                                                        new VariableStatement(int.class, "id", Value.of(1)),
                                                        new ConditionStatement(ConditionType.LT, IdentStatement.of("id"), IdentStatement.of("50")),
                                                        new OperatorStatement(IdentStatement.of("id"), Operator.PLUS, Value.of(12)).assign(IdentStatement.of("id")),
                                                        BodyStatement.init()
                                                                .addStatement(new VariableStatement(String.class, "var2", Value.of("success")))
                                                                .addStatement(InvokeStatement.of(IdentStatement.of("name1"), "toString"))
                                                                .addStatement(InvokeStatement.of(IdentStatement.of("System.out"), "println", Value.of(IdentStatement.of("var2"))))
                                                ))
                                ))
                )
                .create();

        System.out.println(proxy.getSuperclass().getName());
        System.out.println(Arrays.toString(proxy.getAnnotations()));
        System.out.println(Arrays.toString(proxy.getDeclaredMethods()));
        System.out.println("--------");
        Object instance = proxy.newInstance();

        Method run = proxy.getDeclaredMethod("run");
        run.setAccessible(true);
        run.invoke(instance);
        System.out.println("--------");

        Method getName = proxy.getDeclaredMethod("toName");
        getName.setAccessible(true);
        System.out.println(Arrays.toString(getName.getAnnotations()));

        Field name = proxy.getDeclaredField("name");
        name.setAccessible(true);
        Data annotation1 = name.getAnnotation(Data.class);
        System.out.println(annotation1.toString());

        System.out.println(name.get(instance));

        Field type = proxy.getDeclaredField("type");
        type.setAccessible(true);
        System.out.println(type.get(instance));

        Field test = proxy.getDeclaredField("test");
        test.setAccessible(true);
        System.out.println(test.get(null));

        Field owner = proxy.getDeclaredField("owner");
        owner.setAccessible(true);
        System.out.println(owner.get(instance));

        Field mode = proxy.getDeclaredField("mode");
        mode.setAccessible(true);
        System.out.println(mode.get(null));
    }

    private static void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Target> proxy = ProxyFactory.extend(Target.class)
                .addImport(Invocation.class.getName())
                .addField("_monitor", Monitor.class, Modifier.PRIVATE)
                .beforeMethod("apply", null,
                        BodyStatement.init()
                                .addStatement(IfStatement.If(new ConditionStatement(ConditionType.NE, IdentStatement.of("_monitor"), IdentStatement.of("null")),
                                        BodyStatement.init()
                                                .addStatement(new VariableStatement(Invocation.class, "var1", Value.newObject(Invocation.class)))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("test")))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of(" => ")))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("success")))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("_monitor"), "listener", Value.of(IdentStatement.of("var1"))))
                                        )
                                )
                )
                .create();

        Target target = proxy.getDeclaredConstructor().newInstance();
        target.apply();
    }
}
