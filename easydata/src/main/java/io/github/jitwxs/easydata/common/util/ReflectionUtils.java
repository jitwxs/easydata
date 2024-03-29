package io.github.jitwxs.easydata.common.util;

import com.google.common.collect.ObjectArrays;
import io.github.jitwxs.easydata.common.exception.EasyDataException;
import io.github.jitwxs.easydata.common.exception.EasyDataVerifyException;
import io.github.jitwxs.easydata.common.function.ThrowableBiFunction;
import io.github.jitwxs.easydata.common.function.ThrowableFunction;
import org.apache.ibatis.annotations.Param;
import org.powermock.reflect.Whitebox;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 18:01
 */
public class ReflectionUtils {
    /**
     * 子类获取继承父类中的泛型
     *
     * @param target 子类
     * @return 父类中的泛型信息
     */
    public static Type[] getGenericSuperClass(final Class<?> target) {
        Type t = target.getGenericSuperclass();

        if (t instanceof ParameterizedType) {
            return ((ParameterizedType) t).getActualTypeArguments();
        }

        if (t instanceof Class) {
            return getGenericSuperClass((Class<?>) t);
        }

        return null;
    }

    /**
     * 子类获取接口父类中的泛型
     *
     * @param target 子类
     * @return 父类接口中的泛型信息
     */
    public static Type[] getGenericInterface0Class(final Class<?> target) {
        final Type t = target.getGenericInterfaces()[0];
        ParameterizedType p = (ParameterizedType) t;
        return p.getActualTypeArguments();
    }

    /**
     * 获取某类的所有父类和接口，递归查询
     *
     * @param target     目标类
     * @param resultList 结果集
     */
    public static void loadSuperAndInterfaceClass(final Class<?> target, final List<Class<?>> resultList) {
        for (Type interfaceType : target.getGenericInterfaces()) {
            final Class<?> actualClass = resolveActualClass(interfaceType);
            if (actualClass != null) {
                resultList.add(actualClass);
                loadSuperAndInterfaceClass(actualClass, resultList);
            }
        }

        final Type superType = target.getGenericSuperclass();
        if (superType != null) {
            final Class<?> actualClass = resolveActualClass(superType);
            if (actualClass != null) {
                resultList.add(actualClass);
                loadSuperAndInterfaceClass(actualClass, resultList);
            }
        }
    }

    public static Field[] getFieldsUpTo(Class<?> target, Class<?> exclusiveParent) {
        Field[] result = target.getDeclaredFields();

        Class<?> parentClass = target.getSuperclass();
        if (parentClass != null && (exclusiveParent == null || !parentClass.equals(exclusiveParent))) {
            Field[] parentClassFields = getFieldsUpTo(parentClass, exclusiveParent);
            result = ObjectArrays.concat(result, parentClassFields, Field.class);
        }

        return result;
    }

    /**
     * 获取字段的读方法
     * <p>
     * 优先通过自省获取读方法，使用反射兜底
     *
     * @param fieldName  字段名
     * @param descriptor 基于自省机制的字段描述
     * @param field      字段
     * @return 读方法 (object, result)
     */
    public static ThrowableFunction<Object, ?> getReadFunc(final String fieldName, final PropertyDescriptor descriptor, final Field field) {
        if (descriptor != null) {
            final Method method;

            // support protobuf repeat grammar
            if (descriptor instanceof IndexedPropertyDescriptor) {
                final IndexedPropertyDescriptor propertyDescriptor = (IndexedPropertyDescriptor) descriptor;
                method = propertyDescriptor.getIndexedReadMethod();
            } else {
                method = descriptor.getReadMethod();
            }

            if (method != null) {
                return bean -> {
                    method.setAccessible(true);
                    return method.invoke(bean);
                };
            }
        }

        if (field != null) {
            return bean -> Whitebox.getInternalState(bean, fieldName);
        }

        return null;
    }

    /**
     * 获取字段的写方法
     * <p>
     * 优先通过自省获取写方法，使用反射兜底
     *
     * @param fieldName  字段名
     * @param descriptor 基于自省机制的字段描述
     * @param field      字段
     * @return 写方法 (object, field_value, result)
     */
    public static ThrowableBiFunction<Object, Object, ?> getWriteFunc(final String fieldName, final PropertyDescriptor descriptor, final Field field) {
        if (descriptor != null) {
            final Method method;

            // support protobuf repeat grammar
            if (descriptor instanceof IndexedPropertyDescriptor) {
                final IndexedPropertyDescriptor propertyDescriptor = (IndexedPropertyDescriptor) descriptor;
                method = propertyDescriptor.getIndexedWriteMethod();
            } else {
                method = descriptor.getWriteMethod();
            }

            if (method != null) {
                return (bean, fieldValue) -> {
                    method.setAccessible(true);
                    return method.invoke(bean, fieldValue);
                };
            }
        }

        if (field != null) {
            return (bean, fieldValue) -> {
                Whitebox.setInternalState(bean, fieldName, fieldValue);
                return bean;
            };
        }

        return null;
    }

    /**
     * 获取字段的 {@link Type}，优先使用 field 属性
     *
     * @param field      字段
     * @param descriptor 基于自省机制的字段描述
     * @return {@link Type}
     */
    public static Type getPropertyType(final Field field, final PropertyDescriptor descriptor) {
        if (field != null) {
            final Type type = field.getGenericType();

            if (type != Object.class) {
                return type;
            }
        }

        // support protobuf repeat grammar
        if (descriptor instanceof IndexedPropertyDescriptor) {
            final IndexedPropertyDescriptor propertyDescriptor = (IndexedPropertyDescriptor) descriptor;
            return new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[]{propertyDescriptor.getIndexedPropertyType()};
                }

                @Override
                public Type getRawType() {
                    return List.class;
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }
            };
        } else {
            return descriptor.getPropertyType();
        }
    }

    public static Class<?> getPropertyClass(final Field field, final PropertyDescriptor descriptor) {
        if (field != null) {
            return field.getType();
        }

        // support protobuf repeat grammar
        if (descriptor instanceof IndexedPropertyDescriptor) {
            return Iterable.class;
        } else {
            return descriptor.getPropertyType();
        }
    }

    public static Class<?> getProtoMessageClassByBuilder(final Class<?> protoBuilderClass) {
        final Object emptyBuilder = ObjectUtils.create(protoBuilderClass, null);
        final Object protoMessage = ObjectUtils.buildBuilder(emptyBuilder);
        if (protoMessage == null) {
            throw new EasyDataException("newInstance protobuf message error: " + protoBuilderClass);
        }

        return protoMessage.getClass();
    }

    public static String[] listMethodParamNames(final Method method) {
        final Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) {
            return new String[0];
        }

        final Annotation[][] annotations = method.getParameterAnnotations();
        if (parameters.length != annotations.length) {
            throw new EasyDataVerifyException("Parameter or annotation length not equals");
        }

        final String[] paramNames = new String[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            String key = null;

            // using annotation first
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof Param) {
                    key = ((Param) annotation).value();
                    break;
                }
            }

            // fault by parameter name
            if (key == null) {
                key = parameters[i].getName();
            }

            paramNames[i] = key;
        }

        return paramNames;
    }

    private static Class<?> resolveActualClass(final Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return resolveActualClass(((ParameterizedType) type).getRawType());
        }
        return null;
    }
}
