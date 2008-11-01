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

import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.security.auth.message.module.ClientAuthModule;
import javax.security.auth.message.module.ServerAuthModule;

import com.envoisolutions.sxc.jaxb.FieldAccessor;
import com.envoisolutions.sxc.jaxb.JAXBObject;
import com.envoisolutions.sxc.jaxb.LifecycleCallback;
import com.envoisolutions.sxc.jaxb.RuntimeContext;
import com.envoisolutions.sxc.util.Attribute;
import com.envoisolutions.sxc.util.XoXMLStreamReader;
import com.envoisolutions.sxc.util.XoXMLStreamWriter;
import org.apache.geronimo.components.jaspi.model.AuthModuleType;
import org.apache.geronimo.components.jaspi.model.MessagePolicyType;
import org.apache.geronimo.components.jaspi.model.StringMapAdapter;


import static sxc.org.apache.geronimo.components.jaspi.model.MessagePolicyTypeJAXB.readMessagePolicyType;
import static sxc.org.apache.geronimo.components.jaspi.model.MessagePolicyTypeJAXB.writeMessagePolicyType;

/**
 * @version $Rev: $ $Date: $
 */

@SuppressWarnings({
    "StringEquality"
})
public class AuthModuleTypeJAXB<T>
    extends JAXBObject<AuthModuleType>
{

    public final static AuthModuleTypeJAXB<ClientAuthModule> CLIENT_INSTANCE = new AuthModuleTypeJAXB<ClientAuthModule>();
    public final static AuthModuleTypeJAXB<ServerAuthModule> SERVER_INSTANCE = new AuthModuleTypeJAXB<ServerAuthModule>();
    private final static LifecycleCallback lifecycleCallback = new LifecycleCallback(AuthModuleType.class);
    private final static FieldAccessor<AuthModuleType, String> authModuleTypeClassName = new FieldAccessor<AuthModuleType, String>(AuthModuleType.class, "className");
    private final static FieldAccessor<AuthModuleType, String> authModuleTypeClassLoaderName = new FieldAccessor<AuthModuleType, String>(AuthModuleType.class, "classLoaderName");
    private final static FieldAccessor<AuthModuleType, MessagePolicyType> authModuleTypeRequestPolicy = new FieldAccessor<AuthModuleType, MessagePolicyType>(AuthModuleType.class, "requestPolicy");
    private final static FieldAccessor<AuthModuleType, MessagePolicyType> authModuleTypeResponsePolicy = new FieldAccessor<AuthModuleType, MessagePolicyType>(AuthModuleType.class, "responsePolicy");
    private final static FieldAccessor<AuthModuleType, Map<String, String>> authModuleTypeOptions = new FieldAccessor<AuthModuleType, Map<String, String>>(AuthModuleType.class, "options");
    private final static StringMapAdapter stringMapAdapterAdapter = new StringMapAdapter();

    public AuthModuleTypeJAXB() {
        super(AuthModuleType.class, null, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "authModuleType".intern()), MessagePolicyTypeJAXB.class);
    }

    public static AuthModuleType<ClientAuthModule> readClientAuthModuleType(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        return CLIENT_INSTANCE.read(reader, context);
    }

    public static void writeClientAuthModuleType(XoXMLStreamWriter writer, AuthModuleType authModuleType, RuntimeContext context)
        throws Exception
    {
        CLIENT_INSTANCE.write(writer, authModuleType, context);
    }

    public static AuthModuleType<ServerAuthModule> readServerAuthModuleType(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        return SERVER_INSTANCE.read(reader, context);
    }

    public static void writeServerAuthModuleType(XoXMLStreamWriter writer, AuthModuleType authModuleType, RuntimeContext context)
        throws Exception
    {
        SERVER_INSTANCE.write(writer, authModuleType, context);
    }

    public final AuthModuleType<T> read(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        // Check for xsi:nil
        if (reader.isXsiNil()) {
            return null;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        AuthModuleType<T> authModuleType = new AuthModuleType<T>();
        context.beforeUnmarshal(authModuleType, lifecycleCallback);


        // Check xsi:type
        QName xsiType = reader.getXsiType();
        if (xsiType!= null) {
            if (("authModuleType"!= xsiType.getLocalPart())||("http://geronimo.apache.org/xml/ns/geronimo-jaspi"!= xsiType.getNamespaceURI())) {
                return context.unexpectedXsiType(reader, AuthModuleType.class);
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
            if (("className" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: className
                String className = elementReader.getElementAsString();
                authModuleTypeClassName.setObject(reader, context, authModuleType, className);
            } else if (("classLoaderName" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: classLoaderName
                String classLoaderName = elementReader.getElementAsString();
                authModuleTypeClassLoaderName.setObject(reader, context, authModuleType, classLoaderName);
            } else if (("requestPolicy" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: requestPolicy
                MessagePolicyType requestPolicy = readMessagePolicyType(elementReader, context);
                authModuleTypeRequestPolicy.setObject(reader, context, authModuleType, requestPolicy);
            } else if (("responsePolicy" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: responsePolicy
                MessagePolicyType responsePolicy = readMessagePolicyType(elementReader, context);
                authModuleTypeResponsePolicy.setObject(reader, context, authModuleType, responsePolicy);
            } else if (("options" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: options
                String optionsRaw = elementReader.getElementAsString();

                Map<String, String> options;
                try {
                    options = stringMapAdapterAdapter.unmarshal(optionsRaw);
                } catch (Exception e) {
                    context.xmlAdapterError(elementReader, StringMapAdapter.class, Map.class, Map.class, e);
                    continue;
                }

                authModuleTypeOptions.setObject(reader, context, authModuleType, options);
            } else {
                context.unexpectedElement(elementReader, new QName(elementReader.getNamespaceURI(), elementReader.getLocalName()), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "requestPolicy"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "responsePolicy"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "options"));
            }
        }

        context.afterUnmarshal(authModuleType, lifecycleCallback);

        return authModuleType;
    }

    public final void write(XoXMLStreamWriter writer, AuthModuleType authModuleType, RuntimeContext context)
        throws Exception
    {
        if (authModuleType == null) {
            writer.writeXsiNil();
            return ;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        String prefix = writer.getUniquePrefix("http://geronimo.apache.org/xml/ns/geronimo-jaspi");
        if (AuthModuleType.class!= authModuleType.getClass()) {
            context.unexpectedSubclass(writer, authModuleType, AuthModuleType.class);
            return ;
        }

        context.beforeMarshal(authModuleType, lifecycleCallback);


        // ELEMENT: className
        String className = authModuleTypeClassName.getObject(authModuleType, context, authModuleType);
        if (className!= null) {
            writer.writeStartElement(prefix, "className", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(className);
            writer.writeEndElement();
        } else {
            context.unexpectedNullValue(authModuleType, "className");
        }

        // ELEMENT: requestPolicy
        MessagePolicyType requestPolicy = authModuleTypeRequestPolicy.getObject(authModuleType, context, authModuleType);
        if (requestPolicy!= null) {
            writer.writeStartElement(prefix, "requestPolicy", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writeMessagePolicyType(writer, requestPolicy, context);
            writer.writeEndElement();
        } else {
            context.unexpectedNullValue(authModuleType, "requestPolicy");
        }

        // ELEMENT: responsePolicy
        MessagePolicyType responsePolicy = authModuleTypeResponsePolicy.getObject(authModuleType, context, authModuleType);
        if (responsePolicy!= null) {
            writer.writeStartElement(prefix, "responsePolicy", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writeMessagePolicyType(writer, responsePolicy, context);
            writer.writeEndElement();
        } else {
            context.unexpectedNullValue(authModuleType, "responsePolicy");
        }

        // ELEMENT: options
        Map<String, String> optionsRaw = authModuleTypeOptions.getObject(authModuleType, context, authModuleType);
        String options = null;
        try {
            options = stringMapAdapterAdapter.marshal(optionsRaw);
        } catch (Exception e) {
            context.xmlAdapterError(authModuleType, "options", StringMapAdapter.class, Map.class, Map.class, e);
        }
        if (options!= null) {
            writer.writeStartElement(prefix, "options", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(options);
            writer.writeEndElement();
        }

        context.afterMarshal(authModuleType, lifecycleCallback);
    }

}
