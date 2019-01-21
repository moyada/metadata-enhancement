package io.moyada.metadata.enhancement.test.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Data {

    String value();

    int iv();

    char cv();

    float fv();

    Class clazz();

    int[] array();
}
