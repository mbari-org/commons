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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author brian
 */
public class IOUtil {

    /**
     * Copy ALL available data from one stream into another
     *
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        ReadableByteChannel source = Channels.newChannel(in);
        WritableByteChannel target = Channels.newChannel(out);

        ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);
        while (source.read(buffer) != -1) {
            buffer.flip(); // Prepare the buffer to be drained
            while (buffer.hasRemaining()) {
                target.write(buffer);
            }
            buffer.clear(); // Empty buffer to get ready for filling
        }

        source.close();
        target.close();
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        byte[] byteArray = null;
        try {
            copy(in, bo);
            byteArray = bo.toByteArray();
        } finally {
            bo.close();
        }
        return byteArray;
    }
}
