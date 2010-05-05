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


package org.apache.geronimo.components.jaspi.modules.openid;

import java.util.Map;
import java.util.HashMap;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;

import org.apache.geronimo.components.jaspi.model.JaspiUtil;
import org.apache.geronimo.components.jaspi.model.AuthModuleType;
import org.testng.annotations.Test;

/**
 * @version $Rev$ $Date$
 */
public class OpenIDServerAuthModuleTest {

    @Test
    public void testServerAuthModule() throws Exception {
        CallbackHandler callbackHandler = null;
//        AuthConfigFactoryImpl.staticCallbackHandler = callbackHandler;
        AuthConfigFactory factory1 = AuthConfigFactory.getFactory();
        AuthModuleType<ServerAuthModule> authModuleType = new AuthModuleType<ServerAuthModule>();
        authModuleType.setClassName(OpenIDServerAuthModule.class.getName());
        Map<String, String> options = new HashMap<String, String>();
        options.put(OpenIDServerAuthModule.LOGIN_PAGE_KEY, "/login.jsp");
        options.put(OpenIDServerAuthModule.ERROR_PAGE_KEY, "/error.jsp");
        authModuleType.setOptions(options);
        AuthConfigProvider authConfigProvider = JaspiUtil.wrapServerAuthModule("Http", "testApp", "id", authModuleType, true);
        factory1.registerConfigProvider(authConfigProvider, "Http", "testApp", "description");
        AuthConfigProvider authConfigProvider2 = factory1.getConfigProvider("Http", "testApp", null);
        ServerAuthConfig serverAuthConfig = authConfigProvider2.getServerAuthConfig("Http", "testApp", callbackHandler);
        ServerAuthContext serverAuthContext = serverAuthConfig.getAuthContext("id", null, null);
        if (serverAuthContext == null) {
            throw new NullPointerException("no serverAuthContext");
        }
    }
}
