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

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.geronimo.components.jaspi.ClassLoaderLookup;

/**
 * @version $Rev:$ $Date:$
 */
public class ConfigProviderMapAdapter extends XmlAdapter<ConfigProviderType[], Map<String, ConfigProviderType>> {

    private final ClassLoaderLookup classLoaderLookup;

    public ConfigProviderMapAdapter(ClassLoaderLookup classLoaderLookup) {
        this.classLoaderLookup = classLoaderLookup;
    }

    public ConfigProviderMapAdapter() {
        ClassLoader testLoader = Thread.currentThread().getContextClassLoader();
        final ClassLoader cl = testLoader == null? ConfigProviderMapAdapter.class.getClassLoader(): testLoader;
        classLoaderLookup = new ClassLoaderLookup() {

            public ClassLoader getClassLoader(String name) {
                return cl;
            }
        };
    }

    public Map<String, ConfigProviderType> unmarshal(ConfigProviderType[] configProviderTypes) throws Exception {
        Map<String, ConfigProviderType> map = new HashMap<String, ConfigProviderType>();
        for (ConfigProviderType configProviderType: configProviderTypes) {
            String key = configProviderType.getRegistrationKey();
            map.put(key, configProviderType);
            configProviderType.createAuthConfigProvider(classLoaderLookup);
        }
        return map;
    }

    public ConfigProviderType[] marshal(Map<String, ConfigProviderType> stringConfigProviderTypeMap) throws Exception {
        List<ConfigProviderType> list = new ArrayList<ConfigProviderType>();
        for (ConfigProviderType configProviderType: stringConfigProviderTypeMap.values()) {
            if (configProviderType.isPersistent()) {
                list.add(configProviderType);
            }
        }
        return list.toArray(new ConfigProviderType[stringConfigProviderTypeMap.size()]);
    }
}
