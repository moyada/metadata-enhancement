package io.moyada.metadata.enhancement;

import io.moyada.metadata.enhancement.statement.BodyStatement;
import io.moyada.metadata.enhancement.support.Value;
import io.moyada.metadata.enhancement.support.Annotation;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface Proxy<T> {

    Proxy<T> addImport(String name);

    Proxy<T> addField(String name, Class<?> type, int modifier);

    Proxy<T> addField(String name, Class<?> type, int modifier, Value init);

    Proxy<T> addField(String name, Class<?> type, int modifier, Annotation... annotations);

    Proxy<T> addField(String name, Class<?> type, int modifier, Value init, Annotation... annotations);

    Proxy<T> addMethod(String name, List<Class<?>> paramType, Class<?> returnType, List<Class<? extends Throwable>> exception,
                       int modifier, BodyStatement body, Annotation... annotations);

    Proxy<T> beforeMethod(String name, List<Class<?>> paramType, BodyStatement statements);

    Proxy<T> afterMethod(String name, List<Class<?>> paramType, BodyStatement statements);

    Proxy<T> addAnnotationToClass(Annotation... annotations);

    Proxy<T> addAnnotationToField(String name, Annotation... annotations);

    Proxy<T> addAnnotationToMethod(String name, List<Class<?>> paramType, Annotation... annotations);

    Class<T> create();
}
