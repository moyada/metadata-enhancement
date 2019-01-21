package io.moyada.metadata.enhancement.test.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Invocation {

    private Map<String, Object> data;

    public Invocation() {
        this.data = new HashMap<>();
    }

    public void addArgs(String key, Object value) {
        data.put(key, value);
    }

    public String getData() {
        return data.toString();
    }
}
