package info.android15.dagger2example;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Dagger2Helper {

    private static HashMap<Class<?>, HashMap<Class<?>, Method>> componentsMethodsCache = new HashMap<>();

    /**
     * This method is based on https://github.com/square/mortar/blob/master/dagger2support/src/main/java/mortar/dagger2support/Dagger2.java
     * file that has been released with Apache License Version 2.0, January 2004 http://www.apache.org/licenses/ by Square, Inc.
     *
     * Magic method that creates a component with its dependencies set, by reflection. Relies on
     * Dagger2 naming conventions.
     */
    public static <T> T buildComponent(Class<T> componentClass, Object... dependencies) {
        String fqn = componentClass.getName();

        String packageName = componentClass.getPackage().getName();
        // Accounts for inner classes, ie MyApplication$Component
        String simpleName = fqn.substring(packageName.length() + 1);
        String generatedName = (packageName + ".Dagger_" + simpleName).replace('$', '_');

        try {
            HashMap<Class<?>, Method> methodsCache = new HashMap<>();
            for (Method method : componentClass.getMethods()) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1)
                    methodsCache.put(params[0], method);
            }
            componentsMethodsCache.put(componentClass, methodsCache);

            Class<?> generatedClass = Class.forName(generatedName);
            Object builder = generatedClass.getMethod("builder").invoke(null);

            for (Method method : builder.getClass().getMethods()) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1) {
                    Class<?> dependencyClass = params[0];
                    for (Object dependency : dependencies) {
                        if (dependencyClass.isAssignableFrom(dependency.getClass())) {
                            method.invoke(builder, dependency);
                            break;
                        }
                    }
                }
            }
            //noinspection unchecked
            return (T)builder.getClass().getMethod("build").invoke(builder);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void inject(Class<?> componentClass, Object component, Object target) {
        try {
            componentsMethodsCache.get(componentClass).get(target.getClass()).invoke(component, target);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
