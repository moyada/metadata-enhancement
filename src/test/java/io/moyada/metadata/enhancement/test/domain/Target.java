package io.moyada.metadata.enhancement.test.domain;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Target {

    protected String name;

    public String apply(String name) {
        this.name = name;
        return name;
    }
}
