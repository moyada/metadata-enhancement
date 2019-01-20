package io.moyada.metadata.enhancement.test;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Monitor {

    public void listener(Invocation invocation) {
        System.out.println(invocation.record());
    }
}
