/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package org.apache.geronimo.components.jaspi.model;

import org.apache.geronimo.components.jaspi.ClassLoaderLookup;
import org.apache.geronimo.components.jaspi.ConstantClassLoaderLookup;

import javax.security.auth.callback.CallbackHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version $Rev$ $Date$
 */
public class KeyedObjectMapAdapter<T extends KeyedObject> extends XmlAdapter<T[], Map<String, T>> {
    public static ClassLoaderLookup staticClassLoaderLookup;
    public static CallbackHandler staticCallbackHandler;
    private final ClassLoaderLookup classLoaderLookup;
    private final CallbackHandler callbackHandler;
    private final Class<T> type;

    public KeyedObjectMapAdapter(ClassLoaderLookup classLoaderLookup, CallbackHandler callbackHandler, Class<T> type) {
        this.classLoaderLookup = classLoaderLookup;
        this.callbackHandler = callbackHandler;
        this.type = type;
    }

    public KeyedObjectMapAdapter(Class<T> type) {
        if (staticClassLoaderLookup != null) {
            this.classLoaderLookup = staticClassLoaderLookup;
        } else {
            ClassLoader testLoader = Thread.currentThread().getContextClassLoader();
            final ClassLoader cl = testLoader == null ? KeyedObjectMapAdapter.class.getClassLoader() : testLoader;
            classLoaderLookup = new ConstantClassLoaderLookup(cl);
        }
        this.type = type;
        callbackHandler = staticCallbackHandler;
    }

    public Map<String, T> unmarshal(T[] configProviderTypes) throws Exception {
        Map<String, T> map = new HashMap<String, T>();
        if (configProviderTypes != null) {
            for (T configProviderType : configProviderTypes) {
                if (configProviderType != null) {
                    String key = configProviderType.getKey();
                    map.put(key, configProviderType);
                    configProviderType.initialize(classLoaderLookup, callbackHandler);
                }
            }
        }
        return map;
    }

    public T[] marshal(Map<String, T> stringConfigProviderTypeMap) throws Exception {
        if (stringConfigProviderTypeMap == null) {
            return null;
        }
        List<T> list = new ArrayList<T>();
        for (T configProviderType : stringConfigProviderTypeMap.values()) {
            if (configProviderType.isPersistent()) {
                list.add(configProviderType);
            }
        }
        T[] array = (T[]) Array.newInstance(type, list.size());
        return list.toArray(array);
    }
}
