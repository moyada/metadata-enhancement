package io.moyada.metadata.enhancement.util;

import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class AnnotationUtil {

    public static Map<String, Class<?>> getMemberType(Class<? extends Annotation> type) {
        Method[] methods = type.getDeclaredMethods();
        if (null == methods || methods.length == 0) {
            return Collections.emptyMap();
        }

        Map<String, Class<?>> properties = new HashMap<>(methods.length);
        for (Method method : methods) {
            properties.put(method.getName(), method.getReturnType());
        }
        return properties;
    }

    public static MemberValue getMemberValue(Class<?> valueType, Object value, ConstPool cpool) {
        MemberValue memberValue;
        switch (valueType.getName()) {
            case "java.lang.String":
                memberValue = new StringMemberValue(value.toString(), cpool);
                break;
            case "boolean":
                memberValue = new BooleanMemberValue(TypeUtil.getType(value, boolean.class), cpool);
                break;
            case "byte":
                memberValue = new ByteMemberValue(TypeUtil.getType(value, byte.class), cpool);
                break;
            case "short":
                memberValue = new ShortMemberValue(TypeUtil.getType(value, short.class), cpool);
                break;
            case "int":
                memberValue = new IntegerMemberValue(cpool, TypeUtil.getType(value, int.class));
                break;
            case "long":
                memberValue = new LongMemberValue(TypeUtil.getType(value, long.class), cpool);
                break;
            case "float":
                memberValue = new FloatMemberValue(TypeUtil.getType(value, float.class), cpool);
                break;
            case "double":
                memberValue = new DoubleMemberValue(TypeUtil.getType(value, double.class), cpool);
                break;
            case "char":
                memberValue = new CharMemberValue(TypeUtil.getType(value, char.class), cpool);
                break;
            case "java.lang.Class":
                memberValue = new ClassMemberValue(((Class) value).getName(), cpool);
                break;
            default:
                if (valueType.isArray()) {
                    memberValue = getArrayMember(valueType.getComponentType(), value, cpool);
                } else {
                    memberValue = null;
                }

        }
        return memberValue;
    }

    private static ArrayMemberValue getArrayMember(Class<?> valueType, Object value, ConstPool cpool) {
        ArrayMemberValue memberValue = new ArrayMemberValue(cpool);

        MemberValue[] memberValues;
        String className = valueType.getName();
        int index = 0;
        switch (className) {
            case "java.lang.String":
                String[] strs = (String[]) value;
                memberValues = new MemberValue[strs.length];
                for (String val : strs) {
                    memberValues[index++] = new StringMemberValue(val, cpool);
                }
                break;
            case "boolean":
                boolean[] booleans = (boolean[]) value;
                memberValues = new MemberValue[booleans.length];
                for (boolean val : booleans) {
                    memberValues[index++] = new BooleanMemberValue(val, cpool);
                }
                break;
            case "byte":
                byte[] bytes = (byte[]) value;
                memberValues = new MemberValue[bytes.length];
                for (byte val : bytes) {
                    memberValues[index++] = new ByteMemberValue(val, cpool);
                }
                break;
            case "short":
                short[] shorts = (short[]) value;
                memberValues = new MemberValue[shorts.length];
                for (short val : shorts) {
                    memberValues[index++] = new ShortMemberValue(val, cpool);
                }
                break;
            case "int":
                int[] ints = (int[]) value;
                memberValues = new MemberValue[ints.length];
                for (int val : ints) {
                    memberValues[index++] = new IntegerMemberValue(cpool, val);
                }
                break;
            case "long":
                long[] longs = (long[]) value;
                memberValues = new MemberValue[longs.length];
                for (long val : longs) {
                    memberValues[index++] = new LongMemberValue(val, cpool);
                }
                break;
            case "float":
                float[] floats = (float[]) value;
                memberValues = new MemberValue[floats.length];
                for (float val : floats) {
                    memberValues[index++] = new FloatMemberValue(val, cpool);
                }
                break;
            case "double":
                double[] doubles = (double[]) value;
                memberValues = new MemberValue[doubles.length];
                for (double val : doubles) {
                    memberValues[index++] = new DoubleMemberValue(val, cpool);
                }
                break;
            case "char":
                char[] chars = (char[]) value;
                memberValues = new MemberValue[chars.length];
                for (char val : chars) {
                    memberValues[index++] = new CharMemberValue(val, cpool);
                }
                break;
            case "java.lang.Class":
                Class<?>[] classes = (Class<?>[]) value;
                memberValues = new MemberValue[classes.length];
                for (Class<?> val : classes) {
                    memberValues[index++] = new ClassMemberValue(val.getName(), cpool);
                }
                break;
            default:
                return memberValue;
        }
        memberValue.setValue(memberValues);
        return memberValue;
    }
}
