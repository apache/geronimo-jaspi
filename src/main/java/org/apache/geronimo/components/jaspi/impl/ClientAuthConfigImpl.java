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

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ClientAuthContext;
import org.apache.geronimo.components.jaspi.model.ClientAuthConfigType;

/**
* @version $Rev:$ $Date:$
*/
public class ClientAuthConfigImpl implements ClientAuthConfig {

    private final ClientAuthConfigType clientAuthConfigType;
    private final Map<String, ClientAuthContext> clientAuthContextMap;

    public ClientAuthConfigImpl(ClientAuthConfigType clientAuthConfigType, Map<String, ClientAuthContext> clientAuthContextMap) {
        this.clientAuthConfigType = clientAuthConfigType;
        this.clientAuthContextMap = clientAuthContextMap;
    }

    public ClientAuthContext getAuthContext(String authContextID, Subject clientSubject, Map properties) throws AuthException {
        return clientAuthContextMap.get(authContextID);
    }

    public String getAppContext() {
        return clientAuthConfigType.getAppContext();
    }

    public String getAuthContextID(MessageInfo messageInfo) throws IllegalArgumentException {
        return clientAuthConfigType.getAuthContextID(messageInfo);
    }

    public String getMessageLayer() {
        return clientAuthConfigType.getMessageLayer();
    }

    public boolean isProtected() {
        return clientAuthConfigType.isProtected();
    }

    public void refresh() throws SecurityException {
    }
}
