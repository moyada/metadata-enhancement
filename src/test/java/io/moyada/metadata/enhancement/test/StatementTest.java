package io.moyada.metadata.enhancement.test;

import io.moyada.metadata.enhancement.statement.*;
import io.moyada.metadata.enhancement.support.Value;
import io.moyada.metadata.enhancement.test.domain.Invocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


/**
 * @author xueyikang
 * @since 1.0
 **/
public class StatementTest {

    @Test
    @DisplayName("方法参数引用语句")
    public void paramTest() {
        IdentStatement statement = IdentStatement.of(1);
        Assertions.assertEquals(statement.getContent(), "$1");
    }

    @Test
    @DisplayName("变量引用语句")
    public void identTest() {
        IdentStatement statement = IdentStatement.of("name");
        Assertions.assertEquals(statement.getContent(), "name");
    }

    @Test
    @DisplayName("新定义变量语句")
    public void varTest() {
        final VariableStatement name1 = new VariableStatement(String.class, "name");
        final VariableStatement name2 = new VariableStatement(String.class, "name", Value.of("hello"));
        Assertions.assertAll(new Executable() {
            @Override
            public void execute() throws Throwable {
                Assertions.assertEquals(name1.getContent(), "String name;");
            }
        }, new Executable() {
            @Override
            public void execute() throws Throwable {
                Assertions.assertEquals(name2.getContent(), "String name = \"hello\";");
            }
        });
    }

    @Test
    @DisplayName("运算符操作语句")
    public void operateTest() {
        VariableStatement id = new VariableStatement(int.class, "id", Value.of(1));
        OperatorStatement operate = new OperatorStatement(Value.of(id), Operator.TIMES, Value.of(5)).operate(Operator.PLUS, Value.of(1));
        final AssginStatement assign = operate.assign(id);
        Assertions.assertAll(new Executable() {
            @Override
            public void execute() throws Throwable {
                Assertions.assertEquals(assign.getApply(), "id = id * 5 + 1;");
            }
        }, new Executable() {
            @Override
            public void execute() throws Throwable {
                Assertions.assertEquals(assign.getContent(), "int id = id * 5 + 1;");
            }
        });
    }

    @Test
    @DisplayName("方法调用操作语句")
    public void invokeTest() {
        VariableStatement id = new VariableStatement(String.class, "name", Value.of("test"));
        InvokeStatement charAt = InvokeStatement.of(id, "charAt", Value.of(1));
        Assertions.assertEquals(charAt.getContent(), "name.charAt(1);");
    }

    @Test
    @DisplayName("分支语句")
    public void ifTest() {
        Statement statement = IfStatement.If(new ConditionStatement(ConditionType.LE, IdentStatement.of(1), IdentStatement.of("12")), null)
                .ElseIf(new ConditionStatement(ConditionType.GT, IdentStatement.of(1), IdentStatement.of("size")), null)
                .ElseIf(new ConditionStatement(ConditionType.EQ, IdentStatement.of(1), IdentStatement.of("size")), null)
                .Else(null);

        String body = "if ($1 <= 12) {\n" +
                "\n} else if ($1 > size) {\n" +
                "\n} else if ($1 == size) {\n" +
                "\n} else {\n" +
                "\n}" +
                "\n";
        Assertions.assertEquals(statement.getContent(), body);
    }

    @Test
    @DisplayName("for循环语句")
    public void forTest() {
        ForLoopStatement statement = new ForLoopStatement(
                new VariableStatement(int.class, "id", Value.of(1)),
                new ConditionStatement(ConditionType.LT, IdentStatement.of("id"), IdentStatement.of("50")),
                new OperatorStatement(Value.of(IdentStatement.of("id")), Operator.PLUS, Value.of(12)).assign(IdentStatement.of("id")),
                BodyStatement.init()
                        .addStatement(new VariableStatement(String.class, "name", Value.of("success")))
                        .addStatement(InvokeStatement.of(IdentStatement.of("name"), "toString"))
                        .addStatement(InvokeStatement.of(IdentStatement.of("System.out"), "println", Value.of(IdentStatement.of("name"))))
        );

        String body = "for (int id = 1; id < 50; id = id + 12) {\n" +
                "String name = \"success\";\n" +
                "name.toString();\n" +
                "System.out.println(name);\n" +
                "}" +
                "\n";
        Assertions.assertEquals(statement.getContent(), body);
    }

    @Test
    @DisplayName("while循环语句")
    public void whileTest() {
        Statement statement = new WhileStatement(new ConditionStatement(ConditionType.LE, IdentStatement.of(1), IdentStatement.of("size")), null);

        String body = "while ($1 <= size) {\n" +
                "\n}" +
                "\n";
        Assertions.assertEquals(statement.getContent(), body);
    }

    @Test
    @DisplayName("代码块语句")
    public void bodyTest() {
        BodyStatement bodyStatement = BodyStatement.init()
                .addStatement(IfStatement.If(new ConditionStatement(ConditionType.NE, IdentStatement.of("_monitor"), IdentStatement.NULL),
                        BodyStatement.init()
                                .addStatement(new VariableStatement(Invocation.class, "var1", Value.newObject(Invocation.class)))
                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("info"), Value.of("test")))
                                .addStatement(InvokeStatement.of(IdentStatement.of("var1"), "addArgs", Value.of("status"), Value.of("success")))
                                .addStatement(InvokeStatement.of(IdentStatement.of("_monitor"), "listener", Value.of(IdentStatement.of("var1"))))
                        )
                );

        Assertions.assertNotNull(bodyStatement.getContent());
    }
}
