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


package org.apache.geronimo.components.jaspi;

import java.io.Reader;

import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.module.ClientAuthModule;
import javax.security.auth.message.module.ServerAuthModule;
import org.apache.geronimo.components.jaspi.model.AuthModuleType;
import org.apache.geronimo.components.jaspi.model.ClientAuthConfigType;
import org.apache.geronimo.components.jaspi.model.ClientAuthContextType;
import org.apache.geronimo.components.jaspi.model.ConfigProviderType;
import org.apache.geronimo.components.jaspi.model.JaspiUtil;
import org.apache.geronimo.components.jaspi.model.JaspiXmlUtil;
import org.apache.geronimo.components.jaspi.model.ServerAuthConfigType;
import org.apache.geronimo.components.jaspi.model.ServerAuthContextType;

/**
 * @version $Rev$ $Date$
 */
public class JaspicUtil {

    private JaspicUtil() {
    }

    public static String registerAuthConfigProvider(Reader config, String messageLayer, String appContext) throws ConfigException {
        try {
            ConfigProviderType configProviderType = JaspiXmlUtil.loadConfigProvider(config);
            AuthConfigProvider authConfigProvider = JaspiUtil.wrapAuthConfigProvider(configProviderType);
            return AuthConfigFactory.getFactory().registerConfigProvider(authConfigProvider, messageLayer, appContext, null);
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }

    public static String registerClientAuthConfig(Reader config) throws ConfigException {
        try {
            ClientAuthConfigType clientAuthConfigType = JaspiXmlUtil.loadClientAuthConfig(config);
            AuthConfigProvider authConfigProvider = JaspiUtil.wrapClientAuthConfig(clientAuthConfigType);
            return AuthConfigFactory.getFactory().registerConfigProvider(authConfigProvider, clientAuthConfigType.getMessageLayer(), clientAuthConfigType.getAppContext(), null);
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }

    public static String registerClientAuthContext(Reader config, boolean _protected) throws ConfigException {
        try {
            ClientAuthContextType clientAuthContextType = JaspiXmlUtil.loadClientAuthContext(config);
            AuthConfigProvider authConfigProvider = JaspiUtil.wrapClientAuthContext(clientAuthContextType, _protected);
            return AuthConfigFactory.getFactory().registerConfigProvider(authConfigProvider, clientAuthContextType.getMessageLayer(), clientAuthContextType.getAppContext(), null);
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }

    public static String registerClientAuthModule(String messageLayer, String appContext, String authenticationContextID, Reader config, boolean _protected) throws ConfigException {
        try {
            AuthModuleType<ClientAuthModule> clientAuthModuleType = JaspiXmlUtil.loadClientAuthModule(config);
            AuthConfigProvider authConfigProvider = JaspiUtil.wrapClientAuthModule(messageLayer, appContext, authenticationContextID, clientAuthModuleType, _protected);
            return AuthConfigFactory.getFactory().registerConfigProvider(authConfigProvider, messageLayer, appContext, null);
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }


    public static String registerServerAuthConfig(Reader config) throws ConfigException {
        try {
            ServerAuthConfigType serverAuthConfigType = JaspiXmlUtil.loadServerAuthConfig(config);
            AuthConfigProvider authConfigProvider = JaspiUtil.wrapServerAuthConfig(serverAuthConfigType);
            return AuthConfigFactory.getFactory().registerConfigProvider(authConfigProvider, serverAuthConfigType.getMessageLayer(), serverAuthConfigType.getAppContext(), null);
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }

    public static String registerServerAuthContext(Reader config, boolean _protected) throws ConfigException {
        try {
            ServerAuthContextType serverAuthContextType = JaspiXmlUtil.loadServerAuthContext(config);
            AuthConfigProvider authConfigProvider = JaspiUtil.wrapServerAuthContext(serverAuthContextType, _protected);
            return AuthConfigFactory.getFactory().registerConfigProvider(authConfigProvider, serverAuthContextType.getMessageLayer(), serverAuthContextType.getAppContext(), null);
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }

    public static String registerServerAuthModule(String messageLayer, String appContext, String authenticationContextID, Reader config, boolean _protected) throws ConfigException {
        try {
            AuthModuleType<ServerAuthModule> serverAuthModuleType = JaspiXmlUtil.loadServerAuthModule(config);
            AuthConfigProvider authConfigProvider = JaspiUtil.wrapServerAuthModule(messageLayer, appContext, authenticationContextID, serverAuthModuleType, _protected);
            return AuthConfigFactory.getFactory().registerConfigProvider(authConfigProvider, messageLayer, appContext, null);
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }

}
