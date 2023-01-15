/* (C)2023 */
package org.mbari.jcommons.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class StreamUtilTest {
    @Test
    public void testIteratorToStream() {
        Iterator<String> sourceIterator = Arrays.asList("A", "B", "C").iterator();
        List<String> aPrefixedStrings =
                StreamUtil.toStream(sourceIterator)
                        .filter(t -> t.startsWith("A"))
                        .collect(Collectors.toList());

        assertEquals(1, aPrefixedStrings.size());
    }
}
