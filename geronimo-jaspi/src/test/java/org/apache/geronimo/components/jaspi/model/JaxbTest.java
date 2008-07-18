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
import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.FileReader;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ClientAuthContext;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;

import org.testng.annotations.Test;
import org.xml.sax.SAXException;

/**
 * @version $Rev$ $Date$
 */

public class JaxbTest {
    public static final XMLInputFactory XMLINPUT_FACTORY = XMLInputFactory.newInstance();

    private final int count = 2;

    @Test
    public void testLoad() throws Exception {
        String file = "test-jaspi.xml";
        JaspiType jaspi1 = loadJaspi(file);
        if (jaspi1.getConfigProvider().size() != count) throw new Exception("expected " + count + " configprovider, not this: " + jaspi1.getConfigProvider());
        URL url = getClass().getClassLoader().getResource("test-jaspi.xml");
        File newFile = new File(new File(url.getPath()).getParentFile(), "test-jaspi-write.xml");
        Writer writer = new FileWriter(newFile);
        JaspiXmlUtil.writeJaspi(jaspi1, writer);
        JaspiType jaspi2 = JaspiXmlUtil.loadJaspi(new FileReader(newFile));
        if (jaspi2.getConfigProvider().size() != count) throw new Exception("expected " + count + " configprovider, not this: " + jaspi2.getConfigProvider());
    }

    @Test
    public void testLoad2() throws Exception {
        String file = "test-jaspi-2.xml";
        JaspiType jaspi1 = loadJaspi(file);
        if (jaspi1.getConfigProvider().size() != count) throw new Exception("expected " + count + " configprovider, not this: " + jaspi1.getConfigProvider());
        URL url = getClass().getClassLoader().getResource("test-jaspi.xml");
        File newFile = new File(new File(url.getPath()).getParentFile(), "test-jaspi-2-write.xml");
        Writer writer = new FileWriter(newFile);
        JaspiXmlUtil.writeJaspi(jaspi1, writer);
        JaspiType jaspi2 = JaspiXmlUtil.loadJaspi(new FileReader(newFile));
        if (jaspi2.getConfigProvider().size() != count) throw new Exception("expected " + count + " configprovider, not this: " + jaspi2.getConfigProvider());

        AuthConfigProvider configProvider = jaspi1.getConfigProvider().get(ConfigProviderType.getRegistrationKey("Http", "test-app1")).getProvider();
        ClientAuthConfig clientAuthConfig = configProvider.getClientAuthConfig("Http", "test-app1", null);
        String authContextID = clientAuthConfig.getAuthContextID(null);
        ClientAuthContext clientAuthContext = clientAuthConfig.getAuthContext(authContextID, null, null);
        clientAuthContext.secureRequest(null, null);

        ServerAuthConfig serverAuthConfig = configProvider.getServerAuthConfig("Http", "test-app1", null);
         authContextID = serverAuthConfig.getAuthContextID(null);
        ServerAuthContext serverAuthContext = serverAuthConfig.getAuthContext(authContextID, null, null);
        serverAuthContext.secureResponse(null, null);



    }

    private JaspiType loadJaspi(String file) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(file);
        Reader reader = new InputStreamReader(in);
        JaspiType rbac = JaspiXmlUtil.loadJaspi(reader);
        return rbac;
    }



}
