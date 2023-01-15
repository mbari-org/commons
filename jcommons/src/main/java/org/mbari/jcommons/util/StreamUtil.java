/* 
Copyright Monterey Bay Aquarium Research Institute (MBARI) 2022

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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil {

    /**
     * Transform an Iterator to a Stream
     *
     * @param sourceIterator
     * @param <T>
     * @return A sequential stream
     */
    public static <T> Stream<T> toStream(Iterator<T> sourceIterator) {
        return toStream(sourceIterator, false);
    }

    /**
     * Transform an Iterator to a Stream
     *
     * @param sourceIterator
     * @param parallel true to create a parallel stream, false for sequential stream
     * @param <T>
     * @return A stream
     */
    public static <T> Stream<T> toStream(Iterator<T> sourceIterator, boolean parallel) {
        Iterable<T> iterable = () -> sourceIterator;
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }

    /**
     * Transform an Enumeration to a Stream
     *
     * @param e
     * @param <T>
     * @return A sequential stream
     */
    public static <T> Stream<T> toStream(Enumeration<T> e) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            public T next() {
                                return e.nextElement();
                            }

                            public boolean hasNext() {
                                return e.hasMoreElements();
                            }
                        },
                        Spliterator.ORDERED),
                false);
    }
}
