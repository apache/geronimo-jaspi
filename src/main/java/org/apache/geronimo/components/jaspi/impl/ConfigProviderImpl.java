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


package org.apache.geronimo.components.jaspi.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ClientAuthContext;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ClientAuthModule;
import javax.security.auth.message.module.ServerAuthModule;
import org.apache.geronimo.components.jaspi.model.AuthModuleType;
import org.apache.geronimo.components.jaspi.model.ClientAuthConfigType;
import org.apache.geronimo.components.jaspi.model.ClientAuthContextType;
import org.apache.geronimo.components.jaspi.model.ConfigProviderType;
import org.apache.geronimo.components.jaspi.model.KeyedObjectMapAdapter;
import org.apache.geronimo.components.jaspi.model.MessagePolicyType;
import org.apache.geronimo.components.jaspi.model.ProtectionPolicyType;
import org.apache.geronimo.components.jaspi.model.ServerAuthConfigType;
import org.apache.geronimo.components.jaspi.model.ServerAuthContextType;
import org.apache.geronimo.components.jaspi.model.TargetPolicyType;
import org.apache.geronimo.components.jaspi.model.TargetType;
import org.apache.geronimo.osgi.locator.ProviderLocator;

/**
* @version $Rev:$ $Date:$
*/
public class ConfigProviderImpl implements AuthConfigProvider {

    private final Map<String, ClientAuthConfigType> clientConfigTypeMap;
    private final Map<String, ServerAuthConfigType> serverAuthConfigMap;

    public ConfigProviderImpl(List<ClientAuthConfigType> clientAuthConfigTypes, List<ServerAuthConfigType> serverAuthConfigTypes) {
        try {
            this.clientConfigTypeMap =  new KeyedObjectMapAdapter<ClientAuthConfigType>().unmarshal(clientAuthConfigTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            this.serverAuthConfigMap = new KeyedObjectMapAdapter<ServerAuthConfigType>().unmarshal(serverAuthConfigTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * spec required constructor
     * @param properties useless properties map
     * @param factory useless factory
     */
    public ConfigProviderImpl(Map<String, String> properties, AuthConfigFactory factory) {
        throw new RuntimeException("don't call this");
    }

    public ClientAuthConfig getClientAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException, SecurityException {
        if (layer == null) {
            throw new NullPointerException("messageLayer");
        }
        if (appContext == null) {
            throw new NullPointerException("appContext");
        }
        ClientAuthConfigType ctx = clientConfigTypeMap.get(ConfigProviderType.getRegistrationKey(layer, appContext));
        if (ctx == null) {
            ctx = clientConfigTypeMap.get(ConfigProviderType.getRegistrationKey(null, appContext));
        }
        if (ctx == null) {
            ctx = clientConfigTypeMap.get(ConfigProviderType.getRegistrationKey(layer, null));
        }
        if (ctx == null) {
            ctx = clientConfigTypeMap.get(ConfigProviderType.getRegistrationKey(null, null));
        }
        if (ctx != null) {

            return newClientAuthConfig(ctx, layer, appContext, handler);
        }
        throw new AuthException("No suitable ClientAuthConfig");
    }

    public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException, SecurityException {
        if (layer == null) {
            throw new NullPointerException("messageLayer");
        }
        if (appContext == null) {
            throw new NullPointerException("appContext");
        }
        ServerAuthConfigType ctx = serverAuthConfigMap.get(ConfigProviderType.getRegistrationKey(layer, appContext));
        if (ctx == null) {
            ctx = serverAuthConfigMap.get(ConfigProviderType.getRegistrationKey(null, appContext));
        }
        if (ctx == null) {
            ctx = serverAuthConfigMap.get(ConfigProviderType.getRegistrationKey(layer, null));
        }
        if (ctx == null) {
            ctx = serverAuthConfigMap.get(ConfigProviderType.getRegistrationKey(null, null));
        }
        if (ctx != null) {

            return newServerAuthConfig(ctx, layer, appContext, handler);
        }
        throw new AuthException("No suitable ServerAuthConfig");
    }

    public void refresh() throws SecurityException {
    }

    public static AuthConfigProvider newConfigProvider(final AuthConfigFactory authConfigFactory, final ConfigProviderType configProviderType) {
        AuthConfigProvider provider;
        if (configProviderType.getClassName() == null) {
            provider = new ConfigProviderImpl(configProviderType.getClientAuthConfig(), configProviderType.getServerAuthConfig());
        } else {
            try {
                provider = java.security.AccessController
                .doPrivileged(new PrivilegedExceptionAction<AuthConfigProvider>() {
                    public AuthConfigProvider run() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
                        Class<? extends AuthConfigProvider> cl = ProviderLocator.loadClass(configProviderType.getClassName(), getClass(), Thread.currentThread().getContextClassLoader()).asSubclass(AuthConfigProvider.class);
                        Constructor<? extends AuthConfigProvider> cnst = cl.getConstructor(Map.class, AuthConfigFactory.class);
                        return cnst.newInstance(configProviderType.getProperties(), authConfigFactory);
                    }
                });
            } catch (PrivilegedActionException e) {
                Exception inner = e.getException();
                if (inner instanceof InstantiationException) {
                    throw new SecurityException("AuthConfigFactory error:"
                                    + inner.getCause().getMessage(), inner.getCause());
                } else {
                    throw new SecurityException("AuthConfigFactory error: " + inner, inner);
                }
            } catch (Exception e) {
                throw new SecurityException("AuthConfigFactory error: " + e, e);
            }
        }
        return provider;
    }

    static ClientAuthConfig newClientAuthConfig(ClientAuthConfigType clientAuthConfigType, String messageLayer, String appContext, CallbackHandler callbackHandler) throws AuthException {
        Map<String, ClientAuthContext> authContextMap = new HashMap<String, ClientAuthContext>();
        for (ClientAuthContextType clientAuthContextType: clientAuthConfigType.getClientAuthContext()) {
            if (match(clientAuthContextType, messageLayer, appContext)) {
                ClientAuthContext clientAuthContext = newClientAuthContext(clientAuthContextType, callbackHandler);
                String authContextID = clientAuthContextType.getAuthenticationContextID();
                if (authContextID == null) {
                    authContextID = clientAuthConfigType.getAuthenticationContextID();
                }
                if (!authContextMap.containsKey(authContextID)) {
                    authContextMap.put(authContextID,  clientAuthContext);
                }
            }
        }
        return new ClientAuthConfigImpl(clientAuthConfigType, authContextMap);
    }

    static ClientAuthContext newClientAuthContext(ClientAuthContextType clientAuthContextType, CallbackHandler callbackHandler) throws AuthException {
        List<ClientAuthModule> clientAuthModules = new ArrayList<ClientAuthModule>();
        for (AuthModuleType<ClientAuthModule> clientAuthModuleType: clientAuthContextType.getClientAuthModule()) {
            ClientAuthModule instance = newAuthModule(clientAuthModuleType, callbackHandler);
            clientAuthModules.add(instance);
        }
        return new ClientAuthContextImpl(clientAuthModules);
    }

    private static boolean match(ClientAuthContextType clientAuthContextType, String messageLayer, String appContext) {
        if (messageLayer == null) throw new NullPointerException("messageLayer");
        if (appContext == null) throw new NullPointerException("appContext");
        if (messageLayer.equals(clientAuthContextType.getMessageLayer())) {
            return appContext.equals(clientAuthContextType.getAppContext()) || clientAuthContextType.getAppContext() == null;
        }
        if (clientAuthContextType.getMessageLayer() == null) {
            return appContext.equals(clientAuthContextType.getAppContext()) || clientAuthContextType.getAppContext() == null;
        }
        return false;
    }


    static ServerAuthConfig newServerAuthConfig(ServerAuthConfigType serverAuthConfigType, String messageLayer, String appContext, CallbackHandler callbackHandler) throws AuthException {
        Map<String, ServerAuthContext> authContextMap = new HashMap<String, ServerAuthContext>();
        for (ServerAuthContextType serverAuthContextType: serverAuthConfigType.getServerAuthContext()) {
            if (match(serverAuthContextType, messageLayer, appContext)) {
                ServerAuthContext serverAuthContext = newServerAuthContext(serverAuthContextType, callbackHandler);
                String authContextID = serverAuthContextType.getAuthenticationContextID();
                if (authContextID == null) {
                    authContextID = serverAuthConfigType.getAuthenticationContextID();
                }
                if (!authContextMap.containsKey(authContextID)) {
                    authContextMap.put(authContextID,  serverAuthContext);
                }
            }
        }
        return new ServerAuthConfigImpl(serverAuthConfigType, authContextMap);
    }

    static ServerAuthContext newServerAuthContext(ServerAuthContextType serverAuthContextType, CallbackHandler callbackHandler) throws AuthException {
        List<ServerAuthModule> serverAuthModules = new ArrayList<ServerAuthModule>();
        for (AuthModuleType<ServerAuthModule> serverAuthModuleType: serverAuthContextType.getServerAuthModule()) {
            ServerAuthModule instance = newAuthModule(serverAuthModuleType, callbackHandler);
            serverAuthModules.add(instance);
        }
        return new ServerAuthContextImpl(serverAuthModules);
    }

    private static boolean match(ServerAuthContextType serverAuthContextType, String messageLayer, String appContext) {
        if (messageLayer == null) throw new NullPointerException("messageLayer");
        if (appContext == null) throw new NullPointerException("appContext");
        if (messageLayer.equals(serverAuthContextType.getMessageLayer())) {
            return appContext.equals(serverAuthContextType.getAppContext()) || serverAuthContextType.getAppContext() == null;
        }
        if (serverAuthContextType.getMessageLayer() == null) {
            return appContext.equals(serverAuthContextType.getAppContext()) || serverAuthContextType.getAppContext() == null;
        }
        return false;
    }

    static <T> T newAuthModule(final AuthModuleType authModuleType, final CallbackHandler callbackHandler) throws AuthException {
        T authModule;
        try {
            authModule = java.security.AccessController
            .doPrivileged(new PrivilegedExceptionAction<T>() {
                public T run() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, AuthException {
                    Class<? extends T> cl = (Class<? extends T>) ProviderLocator.loadClass(authModuleType.getClassName(), getClass(), Thread.currentThread().getContextClassLoader());
                    Constructor<? extends T> cnst = cl.getConstructor();
                    T authModule = cnst.newInstance();
                    Method m = cl.getMethod("initialize", MessagePolicy.class, MessagePolicy.class, CallbackHandler.class, Map.class);
                    MessagePolicy reqPolicy = newMessagePolicy(authModuleType.getRequestPolicy());
                    MessagePolicy respPolicy = newMessagePolicy(authModuleType.getResponsePolicy());
                    m.invoke(authModule, reqPolicy, respPolicy, callbackHandler, authModuleType.getOptions());
                    return authModule;
                }
            });
        } catch (PrivilegedActionException e) {
            Exception inner = e.getException();
            if (inner instanceof InstantiationException) {
                throw (AuthException) new AuthException("AuthConfigFactory error:"
                                + inner.getCause().getMessage()).initCause(inner.getCause());
            } else {
                throw (AuthException) new AuthException("AuthConfigFactory error: " + inner).initCause(inner);
            }
        } catch (Exception e) {
            throw (AuthException) new AuthException("AuthConfigFactory error: " + e).initCause(e);
        }
        return authModule;
    }

    private static MessagePolicy newMessagePolicy(MessagePolicyType messagePolicyType) throws AuthException {
        if (messagePolicyType == null) {
            return null;
        }
        if (messagePolicyType.getTargetPolicy().size() == 0) {
            return null;
        }
        MessagePolicy.TargetPolicy[] targetPolicies = new MessagePolicy.TargetPolicy[messagePolicyType.getTargetPolicy().size()];
        int i = 0;
        for (TargetPolicyType targetPolicyType: messagePolicyType.getTargetPolicy()) {
            targetPolicies[i++] = newTargetPolicy(targetPolicyType);
        }
        return new MessagePolicy(targetPolicies, messagePolicyType.isMandatory());
    }

    private static MessagePolicy.TargetPolicy newTargetPolicy(TargetPolicyType targetPolicyType) throws AuthException {
        MessagePolicy.Target[] targets = new MessagePolicy.Target[targetPolicyType.getTarget().size()];
        int i = 0;
        for (TargetType targetType: targetPolicyType.getTarget()) {
            targets[i++] = newTarget(targetType);
        }
        return new MessagePolicy.TargetPolicy(targets, newProtectionPolicy(targetPolicyType.getProtectionPolicy()));
    }

    private static MessagePolicy.Target newTarget(final TargetType targetType) throws AuthException {
        try {
            return java.security.AccessController
            .doPrivileged(new PrivilegedExceptionAction<MessagePolicy.Target>() {
                public MessagePolicy.Target run() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
                    Class<? extends MessagePolicy.Target> cl = ProviderLocator.loadClass(targetType.getClassName(), getClass(), Thread.currentThread().getContextClassLoader()).asSubclass(MessagePolicy.Target.class);
                    Constructor<? extends MessagePolicy.Target> cnst = cl.getConstructor();
                    MessagePolicy.Target target = cnst.newInstance();
                    return target;
                }
            });
        } catch (PrivilegedActionException e) {
            Exception inner = e.getException();
            if (inner instanceof InstantiationException) {
                throw (AuthException) new AuthException("AuthConfigFactory error:"
                                + inner.getCause().getMessage()).initCause(inner.getCause());
            } else {
                throw (AuthException) new AuthException("AuthConfigFactory error: " + inner).initCause(inner);
            }
        } catch (Exception e) {
            throw (AuthException) new AuthException("AuthConfigFactory error: " + e).initCause(e);
        }

    }

    private static MessagePolicy.ProtectionPolicy newProtectionPolicy(final ProtectionPolicyType protectionPolicyType) throws AuthException {
        try {
            return java.security.AccessController
            .doPrivileged(new PrivilegedExceptionAction<MessagePolicy.ProtectionPolicy>() {
                public MessagePolicy.ProtectionPolicy run() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
                    Class<? extends MessagePolicy.ProtectionPolicy> cl = ProviderLocator.loadClass(protectionPolicyType.getClassName(), getClass(), Thread.currentThread().getContextClassLoader()).asSubclass(MessagePolicy.ProtectionPolicy.class);
                    Constructor<? extends MessagePolicy.ProtectionPolicy> cnst = cl.getConstructor();
                    MessagePolicy.ProtectionPolicy target = cnst.newInstance();
                    return target;
                }
            });
        } catch (PrivilegedActionException e) {
            Exception inner = e.getException();
            if (inner instanceof InstantiationException) {
                throw (AuthException) new AuthException("AuthConfigFactory error:"
                                + inner.getCause().getMessage()).initCause(inner.getCause());
            } else {
                throw (AuthException) new AuthException("AuthConfigFactory error: " + inner).initCause(inner);
            }
        } catch (Exception e) {
            throw (AuthException) new AuthException("AuthConfigFactory error: " + e).initCause(e);
        }
    }

}
