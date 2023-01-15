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
package org.mbari.jcommons.awt;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.awt.geom.Point2D;
import java.util.Optional;

/**
 * @author brian
 */
public class AwtUtil {

    /**
     * Convert a {@link Point} to a {@link Point2D}. Decimals are rounded.
     *
     * @param p2 The Point2D object to convert
     * @return the Point object closes to the Point2D object
     */
    public static Point toPoint(Point2D p2) {
        return new Point((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
    }

    /**
     * Convert a {@link Point2D} to a {@link Point}.
     *
     * @param p The Point object to convert
     * @return the Point2D.Double object containing the infromation from the point you supplied.
     */
    public static Point2D toPoint2D(Point p) {
        return new Point2D.Double(p.x, p.y);
    }

    /**
     * Returns the Window that contains the given component. If it's not contained in window then
     * null is returned.
     *
     * @param component The component whose containing window we want to find.
     * @return The containing window or None if not in a containing window
     */
    public static Optional<Window> getWindow(Component component) {
        while (true) {
            Component parent = component.getParent();
            if (parent == null) {
                Window window = (component instanceof Window) ? (Window) component : null;
                return Optional.ofNullable(window);
            }
            component = parent;
        }
    }

    /**
     * Returns the Frame that contains the given component. If it's not contained in a Frame (for
     * example if it's in a Dialog, which is a Window but not a Frame) then None is returned
     */
    public static Optional<Frame> getFrame(Component component) {
        return getWindow(component)
                .flatMap(
                        window -> {
                            Frame frame = (window instanceof Frame) ? (Frame) window : null;
                            return Optional.ofNullable(frame);
                        });
    }
}
