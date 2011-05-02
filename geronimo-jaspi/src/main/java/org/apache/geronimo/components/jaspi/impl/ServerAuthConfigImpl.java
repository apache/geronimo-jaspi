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
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import org.apache.geronimo.components.jaspi.model.ServerAuthConfigType;

/**
* @version $Rev:$ $Date:$
*/
public class ServerAuthConfigImpl implements ServerAuthConfig {

    private final ServerAuthConfigType serverAuthConfigType;
    private final Map<String, ServerAuthContext> serverAuthContextMap;

    public ServerAuthConfigImpl(ServerAuthConfigType serverAuthConfigType, Map<String, ServerAuthContext> serverAuthContextMap) {
        this.serverAuthConfigType = serverAuthConfigType;
        this.serverAuthContextMap = serverAuthContextMap;
    }

    public ServerAuthContext getAuthContext(String authContextID, Subject serverSubject, Map properties) throws AuthException {
        return serverAuthContextMap.get(authContextID);
    }

    public String getAppContext() {
        return serverAuthConfigType.getAppContext();
    }

    public String getAuthContextID(MessageInfo messageInfo) throws IllegalArgumentException {
        return serverAuthConfigType.getAuthContextID(messageInfo);
    }

    public String getMessageLayer() {
        return serverAuthConfigType.getMessageLayer();
    }

    public boolean isProtected() {
        return serverAuthConfigType.isProtected();
    }

    public void refresh() throws SecurityException {
    }
}
