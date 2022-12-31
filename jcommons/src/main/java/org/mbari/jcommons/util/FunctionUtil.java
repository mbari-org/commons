/* (C)2022 */
package org.mbari.jcommons.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

class FunctionUtil {
    /**
     * Attempt to emulate Scala collections distinct by function Usage:
     *
     * <pre>
     *       persons.stream().filter(distinctBy(p -> p.getName());
     *   </pre>
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctBy(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
