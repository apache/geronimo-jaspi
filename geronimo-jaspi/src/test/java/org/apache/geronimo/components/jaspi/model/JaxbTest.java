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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.testng.annotations.Test;
import org.xml.sax.SAXException;

/**
 * @version $Rev:$ $Date:$
 */

public class JaxbTest {
    public static final XMLInputFactory XMLINPUT_FACTORY = XMLInputFactory.newInstance();

    @Test
    public void testLoad() throws Exception {
        String file = "test-jaspi.xml";
        JaspiType rbac = loadRbac(file);
        
    }

    private JaspiType loadRbac(String file) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(file);
        Reader reader = new InputStreamReader(in);
        JaspiType rbac = JaspiXmlUtil.loadJaspi(reader);
        return rbac;
    }



}
