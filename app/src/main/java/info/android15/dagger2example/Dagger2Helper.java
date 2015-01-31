package info.android15.dagger2example;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Dagger2Helper {
    /**
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
            //+build injection cache
            long time1 = System.nanoTime();
            HashMap<Class<?>, Method> componentCache = new HashMap<>();
            for (Method method : componentClass.getMethods()) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1)
                    componentCache.put(params[0], method);
            }
            cache.put(componentClass, componentCache);
            Log.v(Dagger2Helper.class.getSimpleName(), "build injection cache took " + (System.nanoTime() - time1) / 1000000 + " ms");
            //-build injection cache

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
            Log.v(Dagger2Helper.class.getSimpleName(), "build injection cache + builder instantiation took " + (System.nanoTime() - time1) / 1000000 + " ms");
            //noinspection unchecked
            return (T)builder.getClass().getMethod("build").invoke(builder);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static HashMap<Class<?>, HashMap<Class<?>, Method>> cache = new HashMap<>();

    public static void inject(Class<?> componentClass, Object component, Object target) {
        try {
            cache.get(componentClass).get(target.getClass()).invoke(component, target);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
