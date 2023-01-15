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

import java.lang.reflect.Method;

class BeanUtil {
    /**
     * Retrieve a value from a property using
     *
     * @param obj The object who's property you want to fetch
     * @param property The property name
     * @param defaultValue A default value to be returned if either a) The property is not found or
     *     b) if the property is found but the value is null
     * @return THe value of the property
     */
    public static <T> T getProperty(Object obj, String property, T defaultValue) {

        T returnValue = (T) getProperty(obj, property);
        if (returnValue == null) {
            returnValue = defaultValue;
        }

        return returnValue;
    }

    /**
     * Fetch a property from an object. For example of you wanted to get the foo property on a bar
     * object you would normally call {@code bar.getFoo()}. This method lets you call it like {@code
     * BeanUtil.getProperty(bar, "foo")}
     *
     * @param obj The object who's property you want to fetch
     * @param property The property name
     * @return The value of the property or null if it does not exist.
     */
    public static Object getProperty(Object obj, String property) {
        Object returnValue = null;

        try {
            String methodName =
                    "get"
                            + property.substring(0, 1).toUpperCase()
                            + property.substring(1, property.length());
            Class<?> clazz = obj.getClass();
            Method method = clazz.getMethod(methodName, null);
            returnValue = method.invoke(obj, null);
        } catch (Exception e) {
            // Do nothing, we'll return the default value
        }

        return returnValue;
    }
}
