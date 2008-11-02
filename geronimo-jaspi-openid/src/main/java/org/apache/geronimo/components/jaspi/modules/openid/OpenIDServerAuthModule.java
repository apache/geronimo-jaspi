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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;

/**
 * @version $Rev$ $Date$
 */
public class OpenIDServerAuthModule implements ServerAuthModule {

    private static final Class[] SUPPORTED_MESSAGE_TYPES = new Class[]{HttpServletRequest.class, HttpServletResponse.class};
    public static final String MANDATORY_KEY = "javax.security.auth.message.MessagePolicy.isMandatory";
    public static final String AUTH_METHOD_KEY = "javax.servlet.http.authType";
    private static final String OPENID_IDENTIFIER = "openid_identifier";
    private static final String DISCOVERY_SESSION_KEY = "openid-disc";
    private static final String RETURN_ADDRESS = "/_openid_security_check";
    private static final String ORIGINAL_URI_KEY = "org.apache.geronimo.components.jaspi.openid.URI";
    private static final String RETURN_ADDRESS_KEY = "org.apache.geronimo.components.jaspi.openid.return.address";

    public static final String LOGIN_PAGE_KEY = "org.apache.geronimo.security.jaspi.openid.LoginPage";
    public static final String ERROR_PAGE_KEY = "org.apache.geronimo.security.jaspi.openid.ErrorPage";

    private String errorPage;
    private String errorPath;
    private String loginPage;
    private String loginPath;

    private CallbackHandler callbackHandler;
    private ConsumerManager consumerManager;
    private static final String ID_KEY = "org.apache.geronimo.components.jaspi.openid.ID";

    public Class[] getSupportedMessageTypes() {
        return SUPPORTED_MESSAGE_TYPES;
    }

    public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler, Map options) throws AuthException {
        if (options == null) {
            throw new AuthException("No options supplied");
        }
        this.callbackHandler = handler;
        try {
            consumerManager = new ConsumerManager();
        } catch (ConsumerException e) {
            throw (AuthException) new AuthException("Unable to create ConsumerManager").initCause(e);
        }
        consumerManager.setAssociations(new InMemoryConsumerAssociationStore());
        consumerManager.setNonceVerifier(new InMemoryNonceVerifier(5000));

        //??
        consumerManager.getRealmVerifier().setEnforceRpId(false);
        setLoginPage((String) options.get(LOGIN_PAGE_KEY));
        setErrorPage((String) options.get(ERROR_PAGE_KEY));
    }

    private void setLoginPage(String path) throws AuthException {
        if (path == null) {
            throw new AuthException("No login page specified with key " + LOGIN_PAGE_KEY);
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        loginPage = path;
        loginPath = path;
        if (loginPath.indexOf('?') > 0) {
            loginPath = loginPath.substring(0, loginPath.indexOf('?'));
        }
    }

    private void setErrorPage(String path) throws AuthException {
        if (path == null) {
            throw new AuthException("No error page specified with key " + ERROR_PAGE_KEY);
        }
        if (path == null || path.trim().length() == 0) {
            errorPath = null;
            errorPage = null;
        } else {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            errorPage = path;
            errorPath = path;

            if (errorPath.indexOf('?') > 0) {
                errorPath = errorPath.substring(0, errorPath.indexOf('?'));
            }
        }
    }

    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
    }

    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
        HttpServletResponse response = (HttpServletResponse) messageInfo.getResponseMessage();
        boolean isMandatory = isMandatory(messageInfo);
        HttpSession session = request.getSession(isMandatory);
        String uri = request.getPathInfo();
        if (session == null || isLoginOrErrorPage(uri)) {
            //auth not mandatory and not logged in.
            return AuthStatus.SUCCESS;
        }

        //are we returning from the OP redirect?
        if (uri.endsWith(RETURN_ADDRESS)) {
            ParameterList parameterList = new ParameterList(request.getParameterMap());
            DiscoveryInformation discovered = (DiscoveryInformation) session.getAttribute(DISCOVERY_SESSION_KEY);
            String returnAddress = (String) session.getAttribute(RETURN_ADDRESS_KEY);
            session.removeAttribute(RETURN_ADDRESS_KEY);
            try {
                VerificationResult verification = consumerManager.verify(returnAddress, parameterList, discovered);
                Identifier identifier = verification.getVerifiedId();

                if (identifier != null) {
                    session.setAttribute(ID_KEY, identifier);
                    //redirect back to original page
                    response.setContentLength(0);
                    String originalURI = (String) session.getAttribute(ORIGINAL_URI_KEY);
                    session.removeAttribute(ORIGINAL_URI_KEY);
                    if (originalURI == null || originalURI.length() == 0) {
                        originalURI = request.getContextPath();
                        if (originalURI.length() == 0) {
                            originalURI = "/";
                        }
                    }
                    response.sendRedirect(response.encodeRedirectURL(originalURI));
                    return AuthStatus.SEND_CONTINUE;
                }
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Response verification failed: " + verification.getStatusMsg());

//            } catch (MessageException e) {
//
//            } catch (DiscoveryException e) {
//
//            } catch (AssociationException e) {
//
//            } catch (IOException e) {
            } catch (Exception e) {
                try {
                    //TODO redirect to error page or just send error
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                } catch (IOException e1) {

                }
            }
            return AuthStatus.SEND_FAILURE;
        }

        //are we already logged in, and not expired?
        Identifier identifier = (Identifier) session.getAttribute(ID_KEY);
        if (identifier != null) {
            //TODO set up subject and callback handler.
            final IdentifierPrincipal principal = new IdentifierPrincipal(identifier.getIdentifier());
            clientSubject.getPrincipals().add(principal);
            clientSubject.getPrincipals().add(new AuthenticatedPrincipal());
            DiscoveryInformation discovered = (DiscoveryInformation) session.getAttribute(DISCOVERY_SESSION_KEY);
            URL opEndpoint = discovered.getOPEndpoint();
            clientSubject.getPrincipals().add(new OpenIDProviderPrincipal(opEndpoint.toString()));
            CallerPrincipalCallback cpCallback = new CallerPrincipalCallback(clientSubject, principal);
            GroupPrincipalCallback gpCallback = new GroupPrincipalCallback(clientSubject, new String[]{"authenticated"});
            try {
                callbackHandler.handle(new Callback[]{cpCallback, gpCallback});
            } catch (IOException e) {

            } catch (UnsupportedCallbackException e) {

            }
            return AuthStatus.SUCCESS;
        }

        //if request is not mandatory, we don't authenticate.
        if (!isMandatory) {
            return AuthStatus.SUCCESS;
        }
        //assume not...

        String openidIdentifier = request.getParameter(OPENID_IDENTIFIER);
        try {
            //redirect to login page here...
            if (openidIdentifier == null) {
                // redirect to login page
                session.setAttribute(ORIGINAL_URI_KEY, getFullRequestURI(request).toString());
                response.setContentLength(0);
                response.sendRedirect(response.encodeRedirectURL(addPaths(request.getContextPath(), loginPage)));
                return AuthStatus.SEND_CONTINUE;

            }
            List<DiscoveryInformation> discoveries = consumerManager.discover(openidIdentifier);
            //associate with one OP
            DiscoveryInformation discovered = consumerManager.associate(discoveries);
            //save association info in session
            session.setAttribute(DISCOVERY_SESSION_KEY, discovered);

            String returnAddress = request.getRequestURL().append(RETURN_ADDRESS).toString();
            AuthRequest authRequest = consumerManager.authenticate(discovered, returnAddress);
            session.setAttribute(RETURN_ADDRESS_KEY, authRequest.getReturnTo());

            //save original uri in response, to be retrieved after redirect returns
            if (session.getAttribute(ORIGINAL_URI_KEY) == null) {
                session.setAttribute(ORIGINAL_URI_KEY, getFullRequestURI(request).toString());
            }

            //TODO openid 2.0 form redirect
            response.sendRedirect(authRequest.getDestinationUrl(true));
            return AuthStatus.SEND_CONTINUE;

        } catch (DiscoveryException e) {
            throw (AuthException) new AuthException("Could not authenticate").initCause(e);
        } catch (ConsumerException e) {
            throw (AuthException) new AuthException("Could not authenticate").initCause(e);
        } catch (MessageException e) {
            throw (AuthException) new AuthException("Could not authenticate").initCause(e);
        } catch (IOException e) {
            throw (AuthException) new AuthException("Could not authenticate").initCause(e);
        }

//        return null;
    }

    private boolean isLoginOrErrorPage(String uri) {
        return (uri != null &&
                (uri.equals(loginPage) || uri.equals(errorPage)));
    }

    private String addPaths(String p1, String p2) {
        StringBuilder b = new StringBuilder(p1);
        if (p1.endsWith("/")) {
            if (p2.startsWith("/")) {
                b.append(p2, 1, p2.length() - 1);
            } else {
                b.append(p2);
            }
        } else {
            if (!p2.startsWith("/")) {
                b.append("/");
            }
            b.append(p2);
        }

        return b.toString();
    }

    private StringBuilder getFullRequestURI(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme()).append("://");
        builder.append(request.getServerName()).append(":");
        builder.append(request.getServerPort());
        //TODO jetty combines this with the uri and query string.  Can this have query params?
        builder.append(request.getContextPath());
        builder.append(request.getPathInfo());
        if (request.getQueryString() != null && request.getQueryString().length() > 0) {
            builder.append("?").append(request.getQueryString());
        }
        return builder;
    }

    private boolean isMandatory(MessageInfo messageInfo) {
        String mandatory = (String) messageInfo.getMap().get(MANDATORY_KEY);
        if (mandatory == null) {
            return false;
        }
        return Boolean.valueOf(mandatory);
    }

    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return AuthStatus.SEND_SUCCESS;
    }

}
