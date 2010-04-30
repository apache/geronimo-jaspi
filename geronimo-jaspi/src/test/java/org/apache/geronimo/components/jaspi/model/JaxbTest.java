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
import javax.security.auth.message.AuthException;
import javax.security.auth.message.module.ClientAuthModule;
import javax.security.auth.callback.CallbackHandler;

import org.testng.annotations.Test;
import org.xml.sax.SAXException;

/**
 * @version $Rev$ $Date$
 */

public class JaxbTest {
    public static final XMLInputFactory XMLINPUT_FACTORY = XMLInputFactory.newInstance();

    private final int count = 2;

    private CallbackHandler callbackHandler;

    @Test
    public void testLoad() throws Exception {
        String file = "jaspi";
        JaspiType jaspi1 = loadJaspi(file);
        if (jaspi1.getConfigProvider().size() != count) throw new Exception("expected " + count + " configprovider, not this: " + jaspi1.getConfigProvider());
        File newFile = getWriteFile(file);
        Writer writer = new FileWriter(newFile);
        JaspiXmlUtil.writeJaspi(jaspi1, writer);
        JaspiType jaspi2 = JaspiXmlUtil.loadJaspi(new FileReader(newFile));
        if (jaspi2.getConfigProvider().size() != count) throw new Exception("expected " + count + " configprovider, not this: " + jaspi2.getConfigProvider());
    }

    @Test
    public void testLoad2() throws Exception {
        String file = "jaspi-2";
        JaspiType jaspi1 = loadJaspi(file);
        if (jaspi1.getConfigProvider().size() != count) throw new Exception("expected " + count + " configprovider, not this: " + jaspi1.getConfigProvider());
        File newFile = getWriteFile(file);
        Writer writer = new FileWriter(newFile);
        JaspiXmlUtil.writeJaspi(jaspi1, writer);
        JaspiType jaspi2 = JaspiXmlUtil.loadJaspi(new FileReader(newFile));
        if (jaspi2.getConfigProvider().size() != count) throw new Exception("expected " + count + " configprovider, not this: " + jaspi2.getConfigProvider());

        AuthConfigProvider configProvider = jaspi1.getConfigProvider().get(ConfigProviderType.getRegistrationKey("Http", "test-app1")).getProvider();
        checkConfigProvider(configProvider);
    }

    private JaspiType loadJaspi(String file) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        Reader reader = getReader(file);
        JaspiType rbac = JaspiXmlUtil.loadJaspi(reader);
        return rbac;
    }

    private Reader getReader(String file) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("test-" + file + ".xml");
        Reader reader = new InputStreamReader(in);
        return reader;
    }

    private File getWriteFile(String file) {
        URL url = getClass().getClassLoader().getResource("test-jaspi.xml");
        File newFile = new File(new File(url.getPath()).getParentFile(), "test-" + file + "-write.xml");
        return newFile;
    }

    @Test
    public void testConfigProvider() throws Exception {
        String file = "config-provider";
        Reader reader = getReader(file);
        ConfigProviderType jaspi1 = JaspiXmlUtil.loadConfigProvider(reader);
        jaspi1.initialize(callbackHandler);
        File newFile = getWriteFile(file);
        Writer writer = new FileWriter(newFile);
        JaspiXmlUtil.writeConfigProvider(jaspi1, writer);
        ConfigProviderType jaspi2 = JaspiXmlUtil.loadConfigProvider(new FileReader(newFile));

        AuthConfigProvider configProvider = jaspi1.getProvider();
        checkConfigProvider(configProvider);
    }

    @Test
    public void testClientAuthConfig() throws Exception {
        String file = "client-auth-config";
        Reader reader = getReader(file);
        ClientAuthConfigType jaspi1 = JaspiXmlUtil.loadClientAuthConfig(reader);
        jaspi1.initialize(callbackHandler);
        File newFile = getWriteFile(file);
        Writer writer = new FileWriter(newFile);
        JaspiXmlUtil.writeClientAuthConfig(jaspi1, writer);
        ClientAuthConfigType jaspi2 = JaspiXmlUtil.loadClientAuthConfig(new FileReader(newFile));

        ClientAuthConfig clientAuthConfig = jaspi1.newClientAuthConfig("Http", "app", callbackHandler);
        checkClientAuthConfig(clientAuthConfig);
    }

    @Test
    public void testClientAuthContext() throws Exception {
        String file = "client-auth-context";
        Reader reader = getReader(file);
        ClientAuthContextType jaspi1 = JaspiXmlUtil.loadClientAuthContext(reader);
        File newFile = getWriteFile(file);
        Writer writer = new FileWriter(newFile);
        JaspiXmlUtil.writeClientAuthContext(jaspi1, writer);
        ClientAuthContextType jaspi2 = JaspiXmlUtil.loadClientAuthContext(new FileReader(newFile));

        ClientAuthContext clientAuthConfig = jaspi1.newClientAuthContext(callbackHandler);
        clientAuthConfig.secureRequest(null, null);
    }

    @Test
    public void testClientAuthModule() throws Exception {
        String file = "client-auth-module";
        Reader reader = getReader(file);
        AuthModuleType<ClientAuthModule> jaspi1 = JaspiXmlUtil.loadClientAuthModule(reader);
        File newFile = getWriteFile(file);
        Writer writer = new FileWriter(newFile);
        JaspiXmlUtil.writeClientAuthModule(jaspi1, writer);
        AuthModuleType jaspi2 = JaspiXmlUtil.loadClientAuthModule(new FileReader(newFile));

        ClientAuthModule clientAuthConfig = jaspi1.newAuthModule(callbackHandler);
        clientAuthConfig.secureRequest(null, null);
    }

    private void checkConfigProvider(AuthConfigProvider configProvider) throws AuthException {
        ClientAuthConfig clientAuthConfig = configProvider.getClientAuthConfig("Http", "test-app1", null);
        checkClientAuthConfig(clientAuthConfig);

        ServerAuthConfig serverAuthConfig = configProvider.getServerAuthConfig("Http", "test-app1", null);
        String authContextID = serverAuthConfig.getAuthContextID(null);
        ServerAuthContext serverAuthContext = serverAuthConfig.getAuthContext(authContextID, null, null);
        serverAuthContext.secureResponse(null, null);
    }

    private void checkClientAuthConfig(ClientAuthConfig clientAuthConfig) throws AuthException {
        String authContextID = clientAuthConfig.getAuthContextID(null);
        ClientAuthContext clientAuthContext = clientAuthConfig.getAuthContext(authContextID, null, null);
        clientAuthContext.secureRequest(null, null);
    }


}
