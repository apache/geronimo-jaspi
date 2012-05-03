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

import org.apache.geronimo.components.jaspi.impl.ConfigProviderImpl;
import org.apache.geronimo.components.jaspi.model.ConfigProviderType;
import org.apache.geronimo.components.jaspi.model.JaspiType;
import org.apache.geronimo.components.jaspi.model.JaspiXmlUtil;
import org.apache.geronimo.components.jaspi.model.ObjectFactory;
import org.xml.sax.SAXException;

import javax.security.auth.AuthPermission;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.RegistrationListener;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the AuthConfigFactory.
 *
 * @version $Rev: $ $Date: $
 */
public class AuthConfigFactoryImpl extends AuthConfigFactory {

    public static final String JASPI_CONFIGURATION_FILE = "org.apache.geronimo.jaspic.configurationFile";
    private static final File DEFAULT_CONFIG_FILE = new File("var/config/security/jaspic/jaspic.xml");
    public static CallbackHandler staticCallbackHandler;

    private static ClassLoader contextClassLoader;

    private Map<String, ConfigProviderInfo> configProviders = new HashMap<String, ConfigProviderInfo>();

    private final CallbackHandler callbackHandler;
    private final File configFile;

    static {
        contextClassLoader = java.security.AccessController
                .doPrivileged(new java.security.PrivilegedAction<ClassLoader>() {
                    public ClassLoader run() {
                        return Thread.currentThread().getContextClassLoader();
                    }
                });
    }

    public AuthConfigFactoryImpl(CallbackHandler callbackHandler, File configFile) {
        JaspiXmlUtil.initialize(callbackHandler);
        this.callbackHandler = callbackHandler;
        this.configFile = configFile;
        loadConfig();
    }

    public AuthConfigFactoryImpl() {
        this(staticCallbackHandler, getConfigFile());
    }

    private static File getConfigFile() {
        String fileLocation = java.security.AccessController
                .doPrivileged(new java.security.PrivilegedAction<String>() {
                    public String run() {
                        return System.getProperty(JASPI_CONFIGURATION_FILE);
                    }
                });
        File file;
        if (fileLocation == null) {
            file = DEFAULT_CONFIG_FILE;
        } else {
            file = new File(fileLocation);
        }
        return file;
    }

    public AuthConfigFactoryImpl(JaspiType jaspiType, CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
        this.configFile = null;
        initialize(jaspiType);
    }

    public synchronized String[] detachListener(RegistrationListener listener, String layer, String appContext) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AuthPermission("detachAuthListener"));
        }
        List<String> ids = new ArrayList<String>();
        for (Map.Entry<String, ConfigProviderInfo> entry : getRegistrations().entrySet()) {
            ConfigProviderInfo ctx = entry.getValue();
            if ((layer == null || layer.equals(ctx.getMessageLayer())) &&
                    (appContext == null || appContext.equals(ctx.getAppContext()))) {
                if (ctx.getListeners().remove(listener)) {
                    ids.add(entry.getKey());
                }
            }
        }
        return ids.toArray(new String[ids.size()]);
    }

    private Map<String, ConfigProviderInfo> getRegistrations() {
        return configProviders;
    }

    public synchronized AuthConfigProvider getConfigProvider(String layer, String appContext, RegistrationListener listener) {
        if (layer == null) {
            throw new NullPointerException("messageLayer");
        }
        if (appContext == null) {
            throw new NullPointerException("appContext");
        }
        ConfigProviderInfo ctx = getRegistrations().get(ConfigProviderType.getRegistrationKey(layer, appContext));
        if (ctx == null) {
            ctx = getRegistrations().get(ConfigProviderType.getRegistrationKey(null, appContext));
        }
        if (ctx == null) {
            ctx = getRegistrations().get(ConfigProviderType.getRegistrationKey(layer, null));
        }
        if (ctx == null) {
            ctx = getRegistrations().get(ConfigProviderType.getRegistrationKey(null, null));
        }
        if (ctx != null) {
            if (listener != null) {
                ctx.getListeners().add(listener);
            }
            return ctx.getAuthConfigProvider();
        }
        return null;
    }

    public synchronized RegistrationContext getRegistrationContext(String registrationID) {
        return getRegistrations().get(registrationID);
    }

    public synchronized String[] getRegistrationIDs(AuthConfigProvider provider) {
        List<String> ids = new ArrayList<String>();
        for (Map.Entry<String, ConfigProviderInfo> entry : getRegistrations().entrySet()) {
            ConfigProviderInfo ctx = entry.getValue();
            if (provider == null ||
                    provider.getClass().getName().equals(ctx.getAuthConfigProvider().getClass().getName())) {
                ids.add(entry.getKey());
            }
        }
        return ids.toArray(new String[ids.size()]);
    }

    public synchronized void refresh() throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AuthPermission("refreshAuth"));
        }
        loadConfig();
    }

    public String registerConfigProvider(AuthConfigProvider authConfigProvider, String layer, String appContext, String description) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AuthPermission("registerAuthConfigProvider"));
        }
        return registerConfigProvider(authConfigProvider, layer, appContext, description, false, null, null);
    }

    public synchronized String registerConfigProvider(final String className, final Map constructorParam, String layer, String appContext, String description) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AuthPermission("registerAuthConfigProvider"));
        }
        String key = registerConfigProvider(null, layer, appContext, description, true, constructorParam, className);
        saveConfig();
        return key;
    }

    private String registerConfigProvider(AuthConfigProvider provider, String layer, String appContext, String description, boolean persistent, Map<String, String> constructorParam, String className) {
        String key = ConfigProviderType.getRegistrationKey(layer, appContext);
        // Get or create context
        ConfigProviderInfo info = getRegistrations().get(key);
        List<RegistrationListener> listeners;
        if (info == null) {
            listeners = new ArrayList<RegistrationListener>();
        } else {
            if (persistent != info.isPersistent()) {
                throw new IllegalArgumentException("Cannot change the persistence state");
            }
            listeners = info.getListeners();
        }
        // Create provider
        ConfigProviderType ctx = new ConfigProviderType(layer, appContext, persistent, persistent? null: this);
        ctx.setDescription(description);
        if (persistent) {
            if (provider != null) {
                throw new IllegalStateException("Config provider supplied but should be created");
            }
            ctx.setClassName(className);
            ctx.setProperties(constructorParam);
            provider = ConfigProviderImpl.newConfigProvider(this, ctx);
        } else {
            if (provider == null) {
                throw new IllegalStateException("No config provider to set");
            }
        }
        info = new ConfigProviderInfo(provider, ctx, listeners, persistent);
        getRegistrations().put(key, info);

        // Notify listeners
        for (RegistrationListener listener : listeners) {
            listener.notify(info.getMessageLayer(), info.getAppContext());
        }
        // Return registration Id
        return key;
    }

    public synchronized boolean removeRegistration(String registrationID) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AuthPermission("removeAuthRegistration"));
        }
        ConfigProviderInfo ctx = getRegistrations().remove(registrationID);
        saveConfig();
        if (ctx != null) {
            List<RegistrationListener> listeners = ctx.getListeners();
            for (RegistrationListener listener : listeners) {
                listener.notify(ctx.getMessageLayer(), ctx.getAppContext());
            }
            return true;
        }
        return false;
    }

    private void loadConfig() {
        if (configFile != null && configFile.length() > 0) {
            JaspiType jaspiType;
            try {
                FileReader in = new FileReader(configFile);
                try {
                    jaspiType = JaspiXmlUtil.loadJaspi(in);
                } finally {
                    in.close();
                }
            } catch (ParserConfigurationException e) {
                throw new SecurityException("Could not read config", e);
            } catch (IOException e) {
                throw new SecurityException("Could not read config", e);
            } catch (SAXException e) {
                throw new SecurityException("Could not read config", e);
            } catch (JAXBException e) {
                throw new SecurityException("Could not read config", e);
            } catch (XMLStreamException e) {
                throw new SecurityException("Could not read config", e);
            }
            initialize(jaspiType);
        }
    }

    private void initialize(JaspiType jaspiType) {
        Map<String, ConfigProviderInfo> configProviderInfos = new HashMap<String, ConfigProviderInfo>();
        try {
            for (ConfigProviderType configProviderType: jaspiType.getConfigProvider()) {
                AuthConfigProvider authConfigProvider = ConfigProviderImpl.newConfigProvider(this, configProviderType);
                ConfigProviderInfo info = new ConfigProviderInfo(authConfigProvider, configProviderType, true);
                configProviderInfos.put(configProviderType.getKey(), info);
            }
        } catch (Exception e) {
            throw new SecurityException("Could not map config providers", e);
        }
        this.configProviders = configProviderInfos;
    }


    private void saveConfig() {
        if (configFile != null) {
            JaspiType jaspiType = new ObjectFactory().createJaspiType();

            try {
                for (ConfigProviderInfo info: configProviders.values()) {
                    if (info.isPersistent()) {
                        jaspiType.getConfigProvider().add(info.getConfigProviderType());
                    }
                }
                FileWriter out = new FileWriter(configFile);
                try {
                    JaspiXmlUtil.writeJaspi(jaspiType, out);
                } finally {
                    out.close();
                }
            } catch (IOException e) {
                throw new SecurityException("Could not write config", e);
            } catch (XMLStreamException e) {
                throw new SecurityException("Could not write config", e);
            } catch (JAXBException e) {
                throw new SecurityException("Could not write config", e);
            } catch (Exception e) {
                throw new SecurityException("Could not write config", e);
            }
        }
    }


    private static class ConfigProviderInfo implements AuthConfigFactory.RegistrationContext {
        private final AuthConfigProvider authConfigProvider;
        private final ConfigProviderType configProviderType;
        private final boolean persistent;
        private final List<RegistrationListener> listeners;

        private ConfigProviderInfo(AuthConfigProvider authConfigProvider, ConfigProviderType configProviderType, boolean persistent) {
            this.authConfigProvider = authConfigProvider;
            this.configProviderType = configProviderType;
            this.persistent = persistent;
            listeners = new ArrayList<RegistrationListener>();
        }

        private ConfigProviderInfo(AuthConfigProvider authConfigProvider, ConfigProviderType configProviderType, List<RegistrationListener> listeners, boolean persistent) {
            this.authConfigProvider = authConfigProvider;
            this.configProviderType = configProviderType;
            this.listeners = listeners;
            this.persistent = persistent;
        }

        public AuthConfigProvider getAuthConfigProvider() {
            return authConfigProvider;
        }

        public ConfigProviderType getConfigProviderType() {
            return configProviderType;
        }

        public List<RegistrationListener> getListeners() {
            return listeners;
        }

        @Override
        public String getAppContext() {
            return configProviderType.getAppContext();
        }

        @Override
        public String getDescription() {
            return configProviderType.getDescription();
        }

        @Override
        public String getMessageLayer() {
            return configProviderType.getMessageLayer();
        }

        @Override
        public boolean isPersistent() {
            return persistent;
        }
    }

}
