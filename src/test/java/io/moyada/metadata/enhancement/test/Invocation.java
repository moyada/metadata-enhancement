package io.moyada.metadata.enhancement.test;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Invocation {

    StringBuilder str;

    public Invocation() {
        this.str = new StringBuilder();
    }

    public void addArgs(String info) {
        str.append(info);
    }

    public String record() {
        return str.toString();
    }
}
