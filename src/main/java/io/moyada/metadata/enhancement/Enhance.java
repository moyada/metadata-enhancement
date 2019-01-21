package io.moyada.metadata.enhancement;

import io.moyada.metadata.enhancement.statement.BodyStatement;
import io.moyada.metadata.enhancement.support.Value;
import io.moyada.metadata.enhancement.support.Annotation;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface Enhance<T> {

    Enhance<T> addImport(String name);

    Enhance<T> addField(String name, Class<?> type, int modifier);

    Enhance<T> addField(String name, Class<?> type, int modifier, Value init);

    Enhance<T> addField(String name, Class<?> type, int modifier, Annotation... annotations);

    Enhance<T> addField(String name, Class<?> type, int modifier, Value init, Annotation... annotations);

    Enhance<T> addMethod(String name, List<Class<?>> paramType, Class<?> returnType, List<Class<? extends Throwable>> exception,
                         int modifier, BodyStatement body, Annotation... annotations);

    Enhance<T> beforeMethod(String name, List<Class<?>> paramType, BodyStatement statements);

    Enhance<T> afterMethod(String name, List<Class<?>> paramType, BodyStatement statements);

    Enhance<T> afterMethod(String name, List<Class<?>> paramType, BodyStatement statements, boolean asFinally);

    Enhance<T> addAnnotationToClass(Annotation... annotations);

    Enhance<T> addAnnotationToField(String name, Annotation... annotations);

    Enhance<T> addAnnotationToMethod(String name, List<Class<?>> paramType, Annotation... annotations);

    Class<T> create();

    Class<T> create(String name);
}
