package dev.utils.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import dev.utils.JCLogUtils;

/**
 * detail: 域工具
 * Created by Ttt
 */
public final class FieldUtils {

    private FieldUtils() {
    }

    // 日志 TAG
    private static final String TAG = FieldUtils.class.getSimpleName();

    /**
     * 判断是否序列化
     *
     * @param field
     * @return
     */
    public static boolean isSerializable(final Field field) {
        if (field == null) return false;
        Class<?>[] clazzs = field.getType().getInterfaces();
        for (Class<?> clazz : clazzs) {
            if (Serializable.class == clazz) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置域的值
     *
     * @param field
     * @param object
     * @param value
     * @return
     */
    public static Object set(final Field field, final Object object, final Object value) {
        if (field == null || object == null) return null;
        try {
            field.setAccessible(true);
            field.set(object, value);
            return field.get(object);
        } catch (Exception e) {
            JCLogUtils.eTag(TAG, e, "set");
        }
        return null;
    }

    /**
     * 获取域的值
     *
     * @param field
     * @param object
     * @return
     */
    public static Object get(final Field field, final Object object) {
        if (field == null || object == null) return null;
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            JCLogUtils.eTag(TAG, e, "get");
        }
        return null;
    }

    // =

    /**
     * 是否 long/Long 类型
     *
     * @param field
     * @return
     */
    public static boolean isLong(final Field field) {
        return field != null && (field.getType() == long.class || field.getType() == Long.class);
    }

    /**
     * 是否 float/Float 类型
     *
     * @param field
     * @return
     */
    public static boolean isFloat(final Field field) {
        return field != null && (field.getType() == float.class || field.getType() == Float.class);
    }

    /**
     * 是否 double/Double 类型
     *
     * @param field
     * @return
     */
    public static boolean isDouble(final Field field) {
        return field != null && (field.getType() == double.class || field.getType() == Double.class);
    }

    /**
     * 是否 int/Integer 类型
     *
     * @param field
     * @return
     */
    public static boolean isInteger(final Field field) {
        return field != null && (field.getType() == int.class || field.getType() == Integer.class);
    }

    /**
     * 是否 String 类型
     *
     * @param field
     * @return
     */
    public static boolean isString(final Field field) {
        return field != null && (field.getType() == String.class);
    }

    // =

    /**
     * 获取域的泛型类型，如果不带泛型返回 null
     *
     * @param field
     * @return
     */
    public static Class<?> getGenericType(final Field field) {
        if (field == null) return null;
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            type = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (type instanceof Class<?>) {
                return (Class<?>) type;
            }
        } else if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    /**
     * 获取数组的类型
     *
     * @param field
     * @return
     */
    public static Class<?> getComponentType(final Field field) {
        if (field == null || field.getType() == null) return null;
        return field.getType().getComponentType();
    }

    /**
     * 获取全部Field，包括父类
     *
     * @param clazz
     * @return
     */
    public static List<Field> getAllDeclaredFields(final Class<?> clazz) {
        if (clazz == null) return null;
        Class<?> clazzTemp = clazz;
        // find all field.
        LinkedList<Field> fieldList = new LinkedList<>();
        while (clazzTemp != null && clazzTemp != Object.class) {
            Field[] fs = clazzTemp.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                if (!isInvalid(f)) {
                    fieldList.addLast(f);
                }
            }
            clazzTemp = clazzTemp.getSuperclass();
        }
        return fieldList;
    }

    /**
     * 是静态常量或者内部结构属性
     *
     * @param field
     * @return
     */
    public static boolean isInvalid(final Field field) {
        return field != null && ((Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) || field.isSynthetic());
    }
}
