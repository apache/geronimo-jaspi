/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.geronimo.components.jaspi;

import java.net.URL;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigFactory.RegistrationContext;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.RegistrationListener;
import javax.security.auth.message.module.ClientAuthModule;
import javax.security.auth.message.module.ServerAuthModule;

import junit.framework.TestCase;
import org.apache.geronimo.components.jaspi.model.AuthModuleType;
import org.apache.geronimo.components.jaspi.model.JaspiUtil;
import org.apache.geronimo.components.jaspi.providers.BadConstructorProvider;
import org.apache.geronimo.components.jaspi.providers.BadImplementProvider;
import org.apache.geronimo.components.jaspi.providers.DummyClientAuthModule;
import org.apache.geronimo.components.jaspi.providers.DummyProvider;
import org.apache.geronimo.components.jaspi.providers.DummyServerAuthModule;

public class AuthConfigFactoryImplTest extends TestCase {

    protected void setUp() throws Exception {
        URL url = getClass().getClassLoader().getResource("test-jaspi.xml");
        System.setProperty(AuthConfigFactoryImpl.JASPI_CONFIGURATION_FILE, url.getPath());
        CallbackHandler callbackHandler = null;
        AuthConfigFactoryImpl.staticCallbackHandler = callbackHandler;
        AuthConfigFactory.setFactory(null);
    }

    public void testFactory() throws Exception {
        AuthConfigFactory factory1 = AuthConfigFactory.getFactory();
        assertNotNull(factory1);
        AuthConfigFactory factory2 = AuthConfigFactory.getFactory();
        assertNotNull(factory2);
        assertSame(factory1, factory2);
    }

    public void testBadConstructorProvider() throws Exception {
        AuthConfigFactory factory = AuthConfigFactory.getFactory();
        try {
            factory.registerConfigProvider(BadConstructorProvider.class.getName(), null, "layer1", "appContext1", "description");
            fail("An exception should have been thrown");
        } catch (SecurityException e) {

        }
    }

    public void testBadImplementProvider() throws Exception {
        AuthConfigFactory factory = AuthConfigFactory.getFactory();
        try {
            factory.registerConfigProvider(BadImplementProvider.class.getName(), null, "layer2", "appContext2", "description");
            fail("An exception should have been thrown");
        } catch (SecurityException e) {
            //e.printStackTrace();
        }
    }

    public void testRegisterUnregister() throws Exception {
        AuthConfigFactory factory = AuthConfigFactory.getFactory();
        String regId = factory.registerConfigProvider(DummyProvider.class.getName(), null, "layer3", "appContext3", "description");
        assertNotNull(regId);
        RegistrationContext regContext = factory.getRegistrationContext(regId);
        assertNotNull(regContext);
        assertEquals("layer3", regContext.getMessageLayer());
        assertEquals("appContext3", regContext.getAppContext());
        assertEquals("description", regContext.getDescription());

        assertTrue(factory.removeRegistration(regId));

        regContext = factory.getRegistrationContext(regId);
        assertNull(regContext);
    }

    public void testProviderWithLayerAndContext() throws Exception {
        AuthConfigFactory factory = AuthConfigFactory.getFactory();
        String registrationID = factory.registerConfigProvider(DummyProvider.class.getName(), null, "layer4", "appContext4", "description");

        assertNotNull(factory.getConfigProvider("layer4", "appContext4", null));
        assertNull(factory.getConfigProvider("layer4", "bad", null));
        assertNull(factory.getConfigProvider("bad", "appContext4", null));
        factory.removeRegistration(registrationID);
        assertNull(factory.getRegistrationContext(registrationID));
    }

    public void testProviderWithLayer() throws Exception {
        AuthConfigFactory factory = AuthConfigFactory.getFactory();
        String registrationID = factory.registerConfigProvider(DummyProvider.class.getName(), null, "layer5", null, "description");

        assertNotNull(factory.getConfigProvider("layer5", "appContext5", null));
        assertNotNull(factory.getConfigProvider("layer5", "bad", null));
        assertNull(factory.getConfigProvider("bad", "appContext5", null));
        factory.removeRegistration(registrationID);
        assertNull(factory.getRegistrationContext(registrationID));
    }

    public void testProviderContextLayer() throws Exception {
        AuthConfigFactory factory = AuthConfigFactory.getFactory();
        String registrationID = factory.registerConfigProvider(DummyProvider.class.getName(), null, null, "appContext6", "description");

        assertNotNull(factory.getConfigProvider("layer6", "appContext6", null));
        assertNull(factory.getConfigProvider("layer6", "bad", null));
        assertNotNull(factory.getConfigProvider("bad", "appContext6", null));
        factory.removeRegistration(registrationID);
        assertNull(factory.getRegistrationContext(registrationID));
    }

    public void testProviderDefault() throws Exception {
        AuthConfigFactory factory = AuthConfigFactory.getFactory();
        String registrationID = factory.registerConfigProvider(DummyProvider.class.getName(), null, null, null, "description");

        assertNotNull(factory.getConfigProvider("layer7", "appContext7", null));
        assertNotNull(factory.getConfigProvider("layer7", "bad", null));
        assertNotNull(factory.getConfigProvider("bad", "appContext7", null));
        assertNotNull(factory.getConfigProvider("bad", "bad", null));
        factory.removeRegistration(registrationID);
        assertNull(factory.getRegistrationContext(registrationID));
    }

    public void testListenerOnRegister() throws Exception {
        AuthConfigFactory factory = AuthConfigFactory.getFactory();
        String registrationID = factory.registerConfigProvider(DummyProvider.class.getName(), null, null, null, "description");
        DummyListener listener = new DummyListener();
        assertNotNull(factory.getConfigProvider("foo", "bar", listener));
        factory.registerConfigProvider(DummyProvider.class.getName(), null, null, null, "description");
        assertTrue(listener.notified);
        factory.removeRegistration(registrationID);
        assertNull(factory.getRegistrationContext(registrationID));
    }

    public void testListenerOnUnregister() throws Exception {
        AuthConfigFactory factory = AuthConfigFactory.getFactory();
        String regId = factory.registerConfigProvider(DummyProvider.class.getName(), null, null, null, "description");
        DummyListener listener = new DummyListener();
        assertNotNull(factory.getConfigProvider("foo", "bar", listener));
        factory.removeRegistration(regId);
        assertTrue(listener.notified);
    }

    public void testWrapClientAuthModule() throws Exception {
        AuthConfigFactory factory = AuthConfigFactory.getFactory();
        AuthModuleType<ClientAuthModule> authModuleType = new AuthModuleType<ClientAuthModule>();
        authModuleType.setClassName(DummyClientAuthModule.class.getName());
        AuthConfigProvider authConfigProvider = JaspiUtil.wrapClientAuthModule("layer", "appContext1", "id", authModuleType, true);
        String regId = factory.registerConfigProvider(authConfigProvider, "layer", "appContext1", "description");
        DummyListener listener = new DummyListener();
        assertNotNull(factory.getConfigProvider("layer", "appContext1", listener));
        factory.removeRegistration(regId);
        assertTrue(listener.notified);
    }

    public void testWrapServerAuthModule() throws Exception {
        AuthConfigFactory factory = AuthConfigFactory.getFactory();
        AuthModuleType<ServerAuthModule> authModuleType = new AuthModuleType<ServerAuthModule>();
        authModuleType.setClassName(DummyServerAuthModule.class.getName());
        AuthConfigProvider authConfigProvider = JaspiUtil.wrapServerAuthModule("layer", "appContext1", "id", authModuleType, true);
        String regId = factory.registerConfigProvider(authConfigProvider, "layer", "appContext1", "description");
        DummyListener listener = new DummyListener();
        assertNotNull(factory.getConfigProvider("layer", "appContext1", listener));
        factory.removeRegistration(regId);
        assertTrue(listener.notified);
    }


    public static class DummyListener implements RegistrationListener {
        public boolean notified = true;

        public void notify(String layer, String appContext) {
            notified = true;
        }
    }

}
