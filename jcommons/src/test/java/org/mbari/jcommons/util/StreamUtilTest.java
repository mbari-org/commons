/* 
Copyright Monterey Bay Aquarium Research Institute (MBARI) 2023

MBARI licenses this file to you under the Apache License, 
Version 2.0 (the "License"); you may not use this file except in
compliance with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.   
*/
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
