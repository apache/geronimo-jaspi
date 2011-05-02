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

import java.util.List;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ClientAuthContext;
import javax.security.auth.message.module.ClientAuthModule;

/**
* @version $Rev:$ $Date:$
*/
public class ClientAuthContextImpl implements ClientAuthContext {

    private final List<ClientAuthModule> clientAuthModules;

    public ClientAuthContextImpl(List<ClientAuthModule> clientAuthModules) {
        this.clientAuthModules = clientAuthModules;
    }

    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        for (ClientAuthModule clientAuthModule : clientAuthModules) {
            clientAuthModule.cleanSubject(messageInfo, subject);
        }
    }

    public AuthStatus secureRequest(MessageInfo messageInfo, Subject clientSubject) throws AuthException {
        for (ClientAuthModule clientAuthModule : clientAuthModules) {
            AuthStatus result = clientAuthModule.secureRequest(messageInfo, clientSubject);

            //jaspi spec p 74
            if (result == AuthStatus.SUCCESS) {
                continue;
            }
            if (result == AuthStatus.SEND_CONTINUE || result == AuthStatus.FAILURE) {
                return result;
            }
            throw new AuthException("Invalid AuthStatus " + result + " from client auth module: " + clientAuthModule);
        }
        return AuthStatus.SUCCESS;
    }

    public AuthStatus validateResponse(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        for (ClientAuthModule clientAuthModule : clientAuthModules) {
            AuthStatus result = clientAuthModule.validateResponse(messageInfo, clientSubject, serviceSubject);

            //jaspi spec p 74
            if (result == AuthStatus.SUCCESS) {
                continue;
            }
            if (result == AuthStatus.SEND_CONTINUE || result == AuthStatus.FAILURE) {
                return result;
            }
            throw new AuthException("Invalid AuthStatus " + result + " from client auth module: " + clientAuthModule);
        }
        return AuthStatus.SUCCESS;
    }
}
