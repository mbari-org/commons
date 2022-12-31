/* (C)2022 */
package org.mbari.jcommons.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;

class ReflectUtil {

    /**
     * Get the default no arg Constructor of a class. None if one isn't found.
     *
     * @param clazz
     * @return
     */
    public static Optional<Constructor<?>> findDefaultConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.getParameterCount() == 0)
                .findFirst();
    }
}
