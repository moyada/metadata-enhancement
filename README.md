基于 `javassist` 封装的类元数据增强的工具

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.moyada/metadata-enhancement/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.moyada/metadata-enhancement)

```
// 继承原始类
Class<Target> target = EnhanceFactory.extend(Target.class)
                // 增加引用
                .addImport(Invocation.class.getName())
                // 对类增加注解
                .addAnnotationToClass(Annotation.of(Resource.class))
                // 增加字段与注解
                .addField(fileName, Monitor.class, Modifier.PRIVATE, Annotation.of(Resource.class))
                // 增强方法
                .beforeMethod("apply", Types.of(String.class),
                        // 扩展代码
                        BodyStatement.init()
                                // 条件判断
                                .addStatement(IfStatement.If(new ConditionStatement(ConditionType.NE, IdentStatement.of(fileName), IdentStatement.NULL),
                                        BodyStatement.init()
                                                .addStatement(new VariableStatement(Invocation.class, "var1", Value.newObject(Invocation.class)))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("info"), Value.of(IdentStatement.of(1))))
                                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("status"), Value.of("success")))
                                                .addStatement(InvokeStatement.of(IdentStatement.of(fileName), "listener", Value.of(IdentStatement.of("var1"))))
                                        )
                                )
                )
                // 返回增强的新类
                .create();
```