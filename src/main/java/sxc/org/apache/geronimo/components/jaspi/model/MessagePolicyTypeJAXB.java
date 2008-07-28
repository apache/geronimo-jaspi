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
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import com.envoisolutions.sxc.jaxb.FieldAccessor;
import com.envoisolutions.sxc.jaxb.JAXBObject;
import com.envoisolutions.sxc.jaxb.LifecycleCallback;
import com.envoisolutions.sxc.jaxb.RuntimeContext;
import com.envoisolutions.sxc.util.Attribute;
import com.envoisolutions.sxc.util.XoXMLStreamReader;
import com.envoisolutions.sxc.util.XoXMLStreamWriter;
import org.apache.geronimo.components.jaspi.model.MessagePolicyType;
import org.apache.geronimo.components.jaspi.model.TargetPolicyType;


import static sxc.org.apache.geronimo.components.jaspi.model.TargetPolicyTypeJAXB.readTargetPolicyType;
import static sxc.org.apache.geronimo.components.jaspi.model.TargetPolicyTypeJAXB.writeTargetPolicyType;

/**
 * @version $Rev$ $Date$
 */

@SuppressWarnings({
    "StringEquality"
})
public class MessagePolicyTypeJAXB
    extends JAXBObject<MessagePolicyType>
{

    public final static MessagePolicyTypeJAXB INSTANCE = new MessagePolicyTypeJAXB();
    private final static LifecycleCallback lifecycleCallback = new LifecycleCallback(MessagePolicyType.class);
    private final static FieldAccessor<MessagePolicyType, List<TargetPolicyType>> messagePolicyTypeTargetPolicy = new FieldAccessor<MessagePolicyType, List<TargetPolicyType>>(MessagePolicyType.class, "targetPolicy");
    private final static FieldAccessor<MessagePolicyType, Boolean> messagePolicyTypeMandatory = new FieldAccessor<MessagePolicyType, Boolean>(MessagePolicyType.class, "mandatory");

    public MessagePolicyTypeJAXB() {
        super(MessagePolicyType.class, null, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "messagePolicyType".intern()), TargetPolicyTypeJAXB.class);
    }

    public static MessagePolicyType readMessagePolicyType(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        return INSTANCE.read(reader, context);
    }

    public static void writeMessagePolicyType(XoXMLStreamWriter writer, MessagePolicyType messagePolicyType, RuntimeContext context)
        throws Exception
    {
        INSTANCE.write(writer, messagePolicyType, context);
    }

    public final MessagePolicyType read(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {

        // Check for xsi:nil
        if (reader.isXsiNil()) {
            return null;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        MessagePolicyType messagePolicyType = new MessagePolicyType();
        context.beforeUnmarshal(messagePolicyType, lifecycleCallback);

        List<TargetPolicyType> targetPolicy = null;

        // Check xsi:type
        QName xsiType = reader.getXsiType();
        if (xsiType!= null) {
            if (("messagePolicyType"!= xsiType.getLocalPart())||("http://geronimo.apache.org/xml/ns/geronimo-jaspi"!= xsiType.getNamespaceURI())) {
                return context.unexpectedXsiType(reader, MessagePolicyType.class);
            }
        }

        // Read attributes
        for (Attribute attribute: reader.getAttributes()) {
            if (("mandatory" == attribute.getLocalName())&&(("" == attribute.getNamespace())||(attribute.getNamespace() == null))) {
                // ATTRIBUTE: mandatory
                Boolean mandatory = ("1".equals(attribute.getValue())||"true".equals(attribute.getValue()));
                messagePolicyTypeMandatory.setObject(reader, context, messagePolicyType, mandatory);
            } else if (XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI!= attribute.getNamespace()) {
                context.unexpectedAttribute(attribute, new QName("", "mandatory"));
            }
        }

        // Read elements
        for (XoXMLStreamReader elementReader: reader.getChildElements()) {
            if (("targetPolicy" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: targetPolicy
                TargetPolicyType targetPolicyItem = readTargetPolicyType(elementReader, context);
                if (targetPolicy == null) {
                    targetPolicy = messagePolicyTypeTargetPolicy.getObject(reader, context, messagePolicyType);
                    if (targetPolicy!= null) {
                        targetPolicy.clear();
                    } else {
                        targetPolicy = new ArrayList<TargetPolicyType>();
                    }
                }
                targetPolicy.add(targetPolicyItem);
            } else {
                context.unexpectedElement(elementReader, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "targetPolicy"));
            }
        }
        if (targetPolicy!= null) {
            messagePolicyTypeTargetPolicy.setObject(reader, context, messagePolicyType, targetPolicy);
        }

        context.afterUnmarshal(messagePolicyType, lifecycleCallback);

        return messagePolicyType;
    }

    public final void write(XoXMLStreamWriter writer, MessagePolicyType messagePolicyType, RuntimeContext context)
        throws Exception
    {
        if (messagePolicyType == null) {
            writer.writeXsiNil();
            return ;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        if (MessagePolicyType.class!= messagePolicyType.getClass()) {
            context.unexpectedSubclass(writer, messagePolicyType, MessagePolicyType.class);
            return ;
        }

        context.beforeMarshal(messagePolicyType, lifecycleCallback);


        // ATTRIBUTE: mandatory
        Boolean mandatory = messagePolicyTypeMandatory.getObject(messagePolicyType, context, messagePolicyType);
        if (mandatory!= null) {
            writer.writeAttribute("", "", "mandatory", Boolean.toString(mandatory));
        }

        // ELEMENT: targetPolicy
        List<TargetPolicyType> targetPolicy = messagePolicyTypeTargetPolicy.getObject(messagePolicyType, context, messagePolicyType);
        if (targetPolicy!= null) {
            for (TargetPolicyType targetPolicyItem: targetPolicy) {
                writer.writeStartElementWithAutoPrefix("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "targetPolicy");
                if (targetPolicyItem!= null) {
                    writeTargetPolicyType(writer, targetPolicyItem, context);
                } else {
                    writer.writeXsiNil();
                }
                writer.writeEndElement();
            }
        }

        context.afterMarshal(messagePolicyType, lifecycleCallback);
    }

}
