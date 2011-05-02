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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.module.ClientAuthModule;
import javax.security.auth.message.module.ServerAuthModule;
import org.apache.geronimo.components.jaspi.impl.ConfigProviderImpl;

/**
 * Convenience methods to wrap various jaspi objects into AuthConfigProvider instances, ready to be registered.
 * @version $Rev: 939768 $ $Date: 2010-04-30 11:26:46 -0700 (Fri, 30 Apr 2010) $
 */
public class JaspiUtil {

    private JaspiUtil() {
    }

    public static AuthConfigProvider wrapAuthConfigProvider(ConfigProviderType configProviderType) {
        return new ConfigProviderImpl(configProviderType.getClientAuthConfig(), configProviderType.getServerAuthConfig());
    }

    public static AuthConfigProvider wrapClientAuthConfig(ClientAuthConfigType clientAuthConfigType) throws AuthException {
        List<ClientAuthConfigType> clientAuthConfig = Collections.singletonList(clientAuthConfigType);
        return new ConfigProviderImpl(clientAuthConfig, Collections.<ServerAuthConfigType>emptyList());
    }

    public static AuthConfigProvider wrapClientAuthContext(ClientAuthContextType clientAuthContextType, boolean _protected) throws AuthException {
        ClientAuthConfigType clientAuthConfigType = new ClientAuthConfigType(clientAuthContextType, _protected);
        return wrapClientAuthConfig(clientAuthConfigType);
    }

    public static AuthConfigProvider wrapClientAuthModule(String messageLayer, String appContext, String authenticationContextID, AuthModuleType<ClientAuthModule> clientAuthModuleType, boolean _protected) throws AuthException {
        ClientAuthContextType clientAuthContextType = new ClientAuthContextType(messageLayer, appContext, authenticationContextID, clientAuthModuleType);
        return wrapClientAuthContext(clientAuthContextType, _protected);
    }


    public static AuthConfigProvider wrapServerAuthConfig(ServerAuthConfigType serverAuthConfigType) throws AuthException {
        List<ServerAuthConfigType> serverAuthConfig = Collections.singletonList(serverAuthConfigType);
        return new ConfigProviderImpl(Collections.<ClientAuthConfigType>emptyList(), serverAuthConfig);
    }

    public static AuthConfigProvider wrapServerAuthContext(ServerAuthContextType serverAuthContextType, boolean _protected) throws AuthException {
        ServerAuthConfigType serverAuthConfigType = new ServerAuthConfigType(serverAuthContextType, _protected);
        return wrapServerAuthConfig(serverAuthConfigType);
    }

    public static AuthConfigProvider wrapServerAuthModule(String messageLayer, String appContext, String authenticationContextID, AuthModuleType<ServerAuthModule> serverAuthModuleType, boolean _protected) throws AuthException {
        ServerAuthContextType serverAuthContextType = new ServerAuthContextType(messageLayer, appContext, authenticationContextID, serverAuthModuleType);
        return wrapServerAuthContext(serverAuthContextType, _protected);
    }

}
