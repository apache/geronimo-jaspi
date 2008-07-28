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

package sxc.org.apache.geronimo.components.jaspi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import com.envoisolutions.sxc.jaxb.FieldAccessor;
import com.envoisolutions.sxc.jaxb.JAXBObject;
import com.envoisolutions.sxc.jaxb.LifecycleCallback;
import com.envoisolutions.sxc.jaxb.RuntimeContext;
import com.envoisolutions.sxc.util.Attribute;
import com.envoisolutions.sxc.util.XoXMLStreamReader;
import com.envoisolutions.sxc.util.XoXMLStreamWriter;
import org.apache.geronimo.components.jaspi.model.ServerAuthConfigType;
import org.apache.geronimo.components.jaspi.model.ServerAuthContextType;
import org.apache.geronimo.components.jaspi.model.KeyedObjectMapAdapter;


import static sxc.org.apache.geronimo.components.jaspi.model.ServerAuthContextTypeJAXB.readServerAuthContextType;
import static sxc.org.apache.geronimo.components.jaspi.model.ServerAuthContextTypeJAXB.writeServerAuthContextType;

/**
 * @version $Rev$ $Date$
 */

@SuppressWarnings({
    "StringEquality"
})
public class ServerAuthConfigTypeJAXB
    extends JAXBObject<ServerAuthConfigType>
{

    public final static ServerAuthConfigTypeJAXB INSTANCE = new ServerAuthConfigTypeJAXB();
    private final static LifecycleCallback lifecycleCallback = new LifecycleCallback(ServerAuthConfigType.class);
    private final static FieldAccessor<ServerAuthConfigType, String> serverAuthConfigTypeMessageLayer = new FieldAccessor<ServerAuthConfigType, String>(ServerAuthConfigType.class, "messageLayer");
    private final static FieldAccessor<ServerAuthConfigType, String> serverAuthConfigTypeAppContext = new FieldAccessor<ServerAuthConfigType, String>(ServerAuthConfigType.class, "appContext");
    private final static FieldAccessor<ServerAuthConfigType, String> serverAuthConfigTypeAuthenticationContextID = new FieldAccessor<ServerAuthConfigType, String>(ServerAuthConfigType.class, "authenticationContextID");
    private final static FieldAccessor<ServerAuthConfigType, Boolean> serverAuthConfigType_protected = new FieldAccessor<ServerAuthConfigType, Boolean>(ServerAuthConfigType.class, "_protected");
    private final static FieldAccessor<ServerAuthConfigType, Map<String, ServerAuthContextType>> serverAuthConfigTypeServerAuthContext = new FieldAccessor<ServerAuthConfigType, Map<String, ServerAuthContextType>>(ServerAuthConfigType.class, "serverAuthContext");
    private final static KeyedObjectMapAdapter<ServerAuthContextType> serverAuthContextMapAdapter = new KeyedObjectMapAdapter<ServerAuthContextType>(ServerAuthContextType.class);

    public ServerAuthConfigTypeJAXB() {
        super(ServerAuthConfigType.class, null, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "serverAuthConfigType".intern()), ServerAuthContextTypeJAXB.class);
    }

    public static ServerAuthConfigType readServerAuthConfigType(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        return INSTANCE.read(reader, context);
    }

    public static void writeServerAuthConfigType(XoXMLStreamWriter writer, ServerAuthConfigType serverAuthConfigType, RuntimeContext context)
        throws Exception
    {
        INSTANCE.write(writer, serverAuthConfigType, context);
    }

    public final ServerAuthConfigType read(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {

        // Check for xsi:nil
        if (reader.isXsiNil()) {
            return null;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        ServerAuthConfigType serverAuthConfigType = new ServerAuthConfigType();
        context.beforeUnmarshal(serverAuthConfigType, lifecycleCallback);

        List<ServerAuthContextType> serverAuthContextRaw = new ArrayList<ServerAuthContextType>();

        // Check xsi:type
        QName xsiType = reader.getXsiType();
        if (xsiType!= null) {
            if (("serverAuthConfigType"!= xsiType.getLocalPart())||("http://geronimo.apache.org/xml/ns/geronimo-jaspi"!= xsiType.getNamespaceURI())) {
                return context.unexpectedXsiType(reader, ServerAuthConfigType.class);
            }
        }

        // Read attributes
        for (Attribute attribute: reader.getAttributes()) {
            if (XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI!= attribute.getNamespace()) {
                context.unexpectedAttribute(attribute);
            }
        }

        // Read elements
        for (XoXMLStreamReader elementReader: reader.getChildElements()) {
            if (("messageLayer" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: messageLayer
                String messageLayer = elementReader.getElementAsString();
                serverAuthConfigTypeMessageLayer.setObject(reader, context, serverAuthConfigType, messageLayer);
            } else if (("appContext" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: appContext
                String appContext = elementReader.getElementAsString();
                serverAuthConfigTypeAppContext.setObject(reader, context, serverAuthConfigType, appContext);
            } else if (("authenticationContextID" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: authenticationContextID
                String authenticationContextID = elementReader.getElementAsString();
                serverAuthConfigTypeAuthenticationContextID.setObject(reader, context, serverAuthConfigType, authenticationContextID);
            } else if (("protected" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: _protected
                Boolean _protected = ("1".equals(elementReader.getElementAsString())||"true".equals(elementReader.getElementAsString()));
                serverAuthConfigType_protected.setBoolean(reader, context, serverAuthConfigType, _protected);
            } else if (("serverAuthContext" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: serverAuthContext
                ServerAuthContextType serverAuthContextItem = readServerAuthContextType(elementReader, context);
//                if (serverAuthContextRaw == null) {
//                    serverAuthContextRaw = serverAuthConfigTypeServerAuthContext.getObject(reader, context, serverAuthConfigType);
//                    if (serverAuthContextRaw != null) {
//                        serverAuthContextRaw.clear();
//                    } else {
//                        serverAuthContextRaw = new ArrayList<ServerAuthContextType>();
//                    }
//                }
                serverAuthContextRaw.add(serverAuthContextItem);
            } else {
                context.unexpectedElement(elementReader, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "messageLayer"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "appContext"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "authenticationContextID"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "protected"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "serverAuthContext"));
            }
        }
        if (serverAuthContextRaw != null) {
            Map<String, ServerAuthContextType> serverAuthContext = serverAuthContextMapAdapter.unmarshal(serverAuthContextRaw.toArray(new ServerAuthContextType[serverAuthContextRaw.size()]));
            serverAuthConfigTypeServerAuthContext.setObject(reader, context, serverAuthConfigType, serverAuthContext);
        }

        context.afterUnmarshal(serverAuthConfigType, lifecycleCallback);

        return serverAuthConfigType;
    }

    public final void write(XoXMLStreamWriter writer, ServerAuthConfigType serverAuthConfigType, RuntimeContext context)
        throws Exception
    {
        if (serverAuthConfigType == null) {
            writer.writeXsiNil();
            return ;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        String prefix = writer.getUniquePrefix("http://geronimo.apache.org/xml/ns/geronimo-jaspi");
        if (ServerAuthConfigType.class!= serverAuthConfigType.getClass()) {
            context.unexpectedSubclass(writer, serverAuthConfigType, ServerAuthConfigType.class);
            return ;
        }

        context.beforeMarshal(serverAuthConfigType, lifecycleCallback);


        // ELEMENT: messageLayer
        String messageLayer = serverAuthConfigTypeMessageLayer.getObject(serverAuthConfigType, context, serverAuthConfigType);
        if (messageLayer!= null) {
            writer.writeStartElement(prefix, "messageLayer", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(messageLayer);
            writer.writeEndElement();
        }

        // ELEMENT: appContext
        String appContext = serverAuthConfigTypeAppContext.getObject(serverAuthConfigType, context, serverAuthConfigType);
        if (appContext!= null) {
            writer.writeStartElement(prefix, "appContext", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(appContext);
            writer.writeEndElement();
        }

        // ELEMENT: authenticationContextID
        String authenticationContextID = serverAuthConfigTypeAuthenticationContextID.getObject(serverAuthConfigType, context, serverAuthConfigType);
        if (authenticationContextID!= null) {
            writer.writeStartElement(prefix, "authenticationContextID", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(authenticationContextID);
            writer.writeEndElement();
        } else {
            context.unexpectedNullValue(serverAuthConfigType, "authenticationContextID");
        }

        // ELEMENT: _protected
        Boolean _protected = serverAuthConfigType_protected.getBoolean(serverAuthConfigType, context, serverAuthConfigType);
        writer.writeStartElement(prefix, "protected", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
        writer.writeCharacters(Boolean.toString(_protected));
        writer.writeEndElement();

        // ELEMENT: serverAuthContext
        Map<String, ServerAuthContextType> serverAuthContextMap = serverAuthConfigTypeServerAuthContext.getObject(serverAuthConfigType, context, serverAuthConfigType);
        ServerAuthContextType[] serverAuthContext = serverAuthContextMapAdapter.marshal(serverAuthContextMap);
        if (serverAuthContext!= null) {
            for (ServerAuthContextType serverAuthContextItem: serverAuthContext) {
                writer.writeStartElement(prefix, "serverAuthContext", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
                if (serverAuthContextItem!= null) {
                    writeServerAuthContextType(writer, serverAuthContextItem, context);
                } else {
                    writer.writeXsiNil();
                }
                writer.writeEndElement();
            }
        }

        context.afterMarshal(serverAuthConfigType, lifecycleCallback);
    }

}
