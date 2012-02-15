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
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;

/**
* @version $Rev:$ $Date:$
*/
public class ServerAuthContextImpl implements ServerAuthContext {

    private final List<ServerAuthModule> serverAuthModules;

    public ServerAuthContextImpl(List<ServerAuthModule> serverAuthModules) {
        this.serverAuthModules = serverAuthModules;
    }

    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        for (ServerAuthModule serverAuthModule : serverAuthModules) {
            serverAuthModule.cleanSubject(messageInfo, subject);
        }
    }

    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        for (ServerAuthModule serverAuthModule : serverAuthModules) {
            AuthStatus result = serverAuthModule.secureResponse(messageInfo, serviceSubject);

            //jaspi spec p 86
            if (result == AuthStatus.SEND_SUCCESS) {
                continue;
            }
            if (result == AuthStatus.SEND_CONTINUE || result == AuthStatus.SEND_FAILURE) {
                return result;
            }
            throw new AuthException("Invalid AuthStatus " + result + " from server auth module secureResponse: " + serverAuthModule);
        }
        return AuthStatus.SEND_SUCCESS;
    }

    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        for (ServerAuthModule serverAuthModule : serverAuthModules) {
            AuthStatus result = serverAuthModule.validateRequest(messageInfo, clientSubject, serviceSubject);

            //jaspi spec p 88
            if (result == AuthStatus.SUCCESS) {
                continue;
            }
            if (result == AuthStatus.SEND_SUCCESS || result == AuthStatus.SEND_CONTINUE || result == AuthStatus.SEND_FAILURE) {
                return result;
            }
            throw new AuthException("Invalid AuthStatus " + result + " from server auth module validateRequest: " + serverAuthModule);
        }
        return AuthStatus.SUCCESS;
    }

}
