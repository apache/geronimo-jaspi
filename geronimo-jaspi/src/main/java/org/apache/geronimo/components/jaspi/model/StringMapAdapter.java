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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * jaxb helper class for maps
 *
 * @version $Rev: 939768 $ $Date: 2010-04-30 11:26:46 -0700 (Fri, 30 Apr 2010) $
 */
public class StringMapAdapter extends XmlAdapter<String, Map<String, String>> {
    public Map<String, String> unmarshal(String s) throws Exception {
        if (s == null) {
            return null;
        }
        Properties properties = new Properties();
        ByteArrayInputStream in = new ByteArrayInputStream(s.getBytes());
        properties.load(in);
        Map<String, String> map = new HashMap<String, String>(properties.size());
        for (Map.Entry entry: properties.entrySet()) {
            map.put((String)entry.getKey(), (String)entry.getValue());
        }
        return map;
    }

    public String marshal(Map<String, String> map) throws Exception {
        if (map == null) {
            return "";
        }
        Properties properties = new Properties();
        properties.putAll(map);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        properties.store(out, null);
        return new String(out.toByteArray());
    }

}
