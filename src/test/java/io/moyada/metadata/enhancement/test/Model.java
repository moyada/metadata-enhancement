package io.moyada.metadata.enhancement.test;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Model {

    private int id;

    public static String getName(int id) {
        return id + "";
    }

    public String toName() {
        return id + "";
    }

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        System.out.println(list.getClass());
        TypeVariable<? extends Class<? extends ArrayList>> typeParameter = list.getClass().getTypeParameters()[0];
        System.out.println(Arrays.toString(typeParameter.getBounds()));
        System.out.println(Arrays.toString(list.getClass().getTypeParameters()));
    }

    public Model() {
    }

    public Model(String name) {
    }


    public Model(char name) {
        list(List.class);
    }


    private void list(Class<?> clazz) {

    }

    @Override
    public String toString() {
        return "666";
    }
}
