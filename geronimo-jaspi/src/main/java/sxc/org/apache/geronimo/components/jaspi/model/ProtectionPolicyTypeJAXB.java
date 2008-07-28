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

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import com.envoisolutions.sxc.jaxb.FieldAccessor;
import com.envoisolutions.sxc.jaxb.JAXBObject;
import com.envoisolutions.sxc.jaxb.LifecycleCallback;
import com.envoisolutions.sxc.jaxb.RuntimeContext;
import com.envoisolutions.sxc.util.Attribute;
import com.envoisolutions.sxc.util.XoXMLStreamReader;
import com.envoisolutions.sxc.util.XoXMLStreamWriter;
import org.apache.geronimo.components.jaspi.model.ProtectionPolicyType;

/**
 * @version $Rev$ $Date$
 */

@SuppressWarnings({
    "StringEquality"
})
public class ProtectionPolicyTypeJAXB
    extends JAXBObject<ProtectionPolicyType>
{

    public final static ProtectionPolicyTypeJAXB INSTANCE = new ProtectionPolicyTypeJAXB();
    private final static LifecycleCallback lifecycleCallback = new LifecycleCallback(ProtectionPolicyType.class);
    private final static FieldAccessor<ProtectionPolicyType, String> protectionPolicyTypeClassName = new FieldAccessor<ProtectionPolicyType, String>(ProtectionPolicyType.class, "className");

    public ProtectionPolicyTypeJAXB() {
        super(ProtectionPolicyType.class, null, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "protectionPolicyType".intern()));
    }

    public static ProtectionPolicyType readProtectionPolicyType(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        return INSTANCE.read(reader, context);
    }

    public static void writeProtectionPolicyType(XoXMLStreamWriter writer, ProtectionPolicyType protectionPolicyType, RuntimeContext context)
        throws Exception
    {
        INSTANCE.write(writer, protectionPolicyType, context);
    }

    public final ProtectionPolicyType read(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {

        // Check for xsi:nil
        if (reader.isXsiNil()) {
            return null;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        ProtectionPolicyType protectionPolicyType = new ProtectionPolicyType();
        context.beforeUnmarshal(protectionPolicyType, lifecycleCallback);


        // Check xsi:type
        QName xsiType = reader.getXsiType();
        if (xsiType!= null) {
            if (("protectionPolicyType"!= xsiType.getLocalPart())||("http://geronimo.apache.org/xml/ns/geronimo-jaspi"!= xsiType.getNamespaceURI())) {
                return context.unexpectedXsiType(reader, ProtectionPolicyType.class);
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
                protectionPolicyTypeClassName.setObject(reader, context, protectionPolicyType, className);
            } else {
                context.unexpectedElement(elementReader, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "className"));
            }
        }

        context.afterUnmarshal(protectionPolicyType, lifecycleCallback);

        return protectionPolicyType;
    }

    public final void write(XoXMLStreamWriter writer, ProtectionPolicyType protectionPolicyType, RuntimeContext context)
        throws Exception
    {
        if (protectionPolicyType == null) {
            writer.writeXsiNil();
            return ;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        if (ProtectionPolicyType.class!= protectionPolicyType.getClass()) {
            context.unexpectedSubclass(writer, protectionPolicyType, ProtectionPolicyType.class);
            return ;
        }

        context.beforeMarshal(protectionPolicyType, lifecycleCallback);


        // ELEMENT: className
        String className = protectionPolicyTypeClassName.getObject(protectionPolicyType, context, protectionPolicyType);
        if (className!= null) {
            writer.writeStartElementWithAutoPrefix("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "className");
            writer.writeCharacters(className);
            writer.writeEndElement();
        } else {
            context.unexpectedNullValue(protectionPolicyType, "className");
        }

        context.afterMarshal(protectionPolicyType, lifecycleCallback);
    }

}
