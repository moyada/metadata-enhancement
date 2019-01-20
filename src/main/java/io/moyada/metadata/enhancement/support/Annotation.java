package io.moyada.metadata.enhancement.support;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Annotation {

    private Class<? extends java.lang.annotation.Annotation> type;

    private Map<String, Object> properties;

    private Annotation(Class<? extends java.lang.annotation.Annotation> type) {
        this.type = type;
    }

    public static Annotation of(Class<? extends java.lang.annotation.Annotation> type) {
        Assert.checkNotNull(type, "type of Annotation");
        return new Annotation(type);
    }

    public Annotation set(String name, Object value) {
        Assert.checkNotNull(name, "name");
        Assert.checkNotNull(value, "value");
        if (null == properties) {
            properties = new HashMap<>();
        }
        properties.put(name, value);
        return this;
    }

    public Class<? extends java.lang.annotation.Annotation> getType() {
        return type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
