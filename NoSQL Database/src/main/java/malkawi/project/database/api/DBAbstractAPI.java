package malkawi.project.database.api;

import malkawi.project.database.data.Result;
import malkawi.project.utilities.io.console.Console;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class DBAbstractAPI {

    private final Map<String, Method> methodCache = new HashMap<>();

    public Result executeAPICall(Object... methodData) {
        Method method = getMethod(((String) methodData[0]).toLowerCase());
        return method != null ? (Result) invokeMethod(method, methodData) : null;
    }

    public Object invokeMethod(Method method, Object[] methodData) {
        try {
            if (method.getParameterCount() == 0)
                return method.invoke(this);
            else
                return method.invoke(this, Arrays.copyOfRange(methodData, 1, methodData.length));
        } catch (IllegalAccessException | InvocationTargetException e) {
            Console.error("[ERROR] can't invoke method: [" + method.getName() + "].\n" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private Method getMethod(String mapping) {
        return methodCache.getOrDefault(mapping, searchForMethod(mapping));
    }

    private Method searchForMethod(String mapping) {
        Method[] methods = getClass().getDeclaredMethods();
        for(Method method : methods) {
            if(method.isAnnotationPresent(Mapping.class) &&
                    method.getAnnotation(Mapping.class).value().equalsIgnoreCase(mapping)) {
                methodCache.put(mapping, method);
                return method;
            }
        }
        return null;
    }

}
