package io.moyada.metadata.enhancement;

import io.moyada.metadata.enhancement.statement.BodyStatement;
import io.moyada.metadata.enhancement.support.Annotation;
import io.moyada.metadata.enhancement.support.Value;

/**
 * 代理工厂接口
 * @author xueyikang
 * @since 1.0
 **/
public interface Enhance<T> {

    /**
     * 添加类引用
     * @param clazz 类
     * @return
     */
    Enhance<T> addImport(Class<?> clazz);

    /**
     * 添加引用
     * @param name 路径
     * @return
     */
    Enhance<T> addImport(String name);

    /**
     * 添加字段
     * @param name 属性名
     * @param type 字段类型
     * @param modifier 访问权限
     * @return
     */
    Enhance<T> addField(String name, Class<?> type, int modifier);

    /**
     * 添加字段并设置初始值
     * @param name 属性名
     * @param type 字段类型
     * @param modifier 访问权限
     * @param init 初始值
     * @return
     */
    Enhance<T> addField(String name, Class<?> type, int modifier, Value init);

    /**
     * 添加字段并配置注解
     * @param name 属性名
     * @param type 字段类型
     * @param modifier 访问权限
     * @param annotations 注解值
     * @return
     */
    Enhance<T> addField(String name, Class<?> type, int modifier, Annotation... annotations);

    /**
     * 添加字段并设置初始值与注解
     * @param name 属性名
     * @param type 字段类型
     * @param modifier 访问权限
     * @param init 初始值
     * @param annotations 注解值
     * @return
     */
    Enhance<T> addField(String name, Class<?> type, int modifier, Value init, Annotation... annotations);

    /**
     * 增加方法
     * @param name 方法名
     * @param paramType 参数类型
     * @param returnType 返回类型
     * @param exception 异常
     * @param modifier 访问权限
     * @param body 方法体
     * @param annotations 注解值
     * @return
     */
    Enhance<T> addMethod(String name, Class<?>[] paramType, Class<?> returnType, Class<? extends Throwable>[] exception,
                         int modifier, BodyStatement body, Annotation... annotations);

    /**
     * 在方法前添加逻辑
     * @param name 方法名
     * @param paramType 参数类型
     * @param statements 代码逻辑
     * @return
     */
    Enhance<T> beforeMethod(String name, Class<?>[] paramType, BodyStatement statements);

    /**
     * 在方法后添加逻辑
     * @param name 方法名
     * @param paramType 参数类型
     * @param statements 代码逻辑
     * @return
     */
    Enhance<T> afterMethod(String name, Class<?>[] paramType, BodyStatement statements);

    /**
     * 为类添加注解
     * @param annotations 注解值
     * @return
     */
    Enhance<T> addAnnotationToClass(Annotation... annotations);

    /**
     * 为属性添加注解
     * @param name 属性名
     * @param annotations 注解值
     * @return
     */
    Enhance<T> addAnnotationToField(String name, Annotation... annotations);

    /**
     * 为方法添加注解
     * @param name 方法名
     * @param paramType 参数
     * @param annotations 注解值
     * @return
     */
    Enhance<T> addAnnotationToMethod(String name, Class<?>[] paramType, Annotation... annotations);

    /**
     * 生成代理类，名字由系统生成
     * @return
     */
    Class<T> create();

    /**
     * 生成代理类并指定名
     * @param name 类名，不可无源类名一致
     * @return
     */
    Class<T> create(String name);
}
