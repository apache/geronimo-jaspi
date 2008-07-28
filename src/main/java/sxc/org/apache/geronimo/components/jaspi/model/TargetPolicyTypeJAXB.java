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
import org.apache.geronimo.components.jaspi.model.ProtectionPolicyType;
import org.apache.geronimo.components.jaspi.model.TargetPolicyType;
import org.apache.geronimo.components.jaspi.model.TargetType;


import static sxc.org.apache.geronimo.components.jaspi.model.ProtectionPolicyTypeJAXB.readProtectionPolicyType;
import static sxc.org.apache.geronimo.components.jaspi.model.ProtectionPolicyTypeJAXB.writeProtectionPolicyType;
import static sxc.org.apache.geronimo.components.jaspi.model.TargetTypeJAXB.readTargetType;
import static sxc.org.apache.geronimo.components.jaspi.model.TargetTypeJAXB.writeTargetType;

/**
 * @version $Rev$ $Date$
 */

@SuppressWarnings({
    "StringEquality"
})
public class TargetPolicyTypeJAXB
    extends JAXBObject<TargetPolicyType>
{

    public final static TargetPolicyTypeJAXB INSTANCE = new TargetPolicyTypeJAXB();
    private final static LifecycleCallback lifecycleCallback = new LifecycleCallback(TargetPolicyType.class);
    private final static FieldAccessor<TargetPolicyType, ProtectionPolicyType> targetPolicyTypeProtectionPolicy = new FieldAccessor<TargetPolicyType, ProtectionPolicyType>(TargetPolicyType.class, "protectionPolicy");
    private final static FieldAccessor<TargetPolicyType, List<TargetType>> targetPolicyTypeTarget = new FieldAccessor<TargetPolicyType, List<TargetType>>(TargetPolicyType.class, "target");

    public TargetPolicyTypeJAXB() {
        super(TargetPolicyType.class, null, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "targetPolicyType".intern()), ProtectionPolicyTypeJAXB.class, TargetTypeJAXB.class);
    }

    public static TargetPolicyType readTargetPolicyType(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        return INSTANCE.read(reader, context);
    }

    public static void writeTargetPolicyType(XoXMLStreamWriter writer, TargetPolicyType targetPolicyType, RuntimeContext context)
        throws Exception
    {
        INSTANCE.write(writer, targetPolicyType, context);
    }

    public final TargetPolicyType read(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {

        // Check for xsi:nil
        if (reader.isXsiNil()) {
            return null;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        TargetPolicyType targetPolicyType = new TargetPolicyType();
        context.beforeUnmarshal(targetPolicyType, lifecycleCallback);

        List<TargetType> target = null;

        // Check xsi:type
        QName xsiType = reader.getXsiType();
        if (xsiType!= null) {
            if (("targetPolicyType"!= xsiType.getLocalPart())||("http://geronimo.apache.org/xml/ns/geronimo-jaspi"!= xsiType.getNamespaceURI())) {
                return context.unexpectedXsiType(reader, TargetPolicyType.class);
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
            if (("protectionPolicy" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: protectionPolicy
                ProtectionPolicyType protectionPolicy = readProtectionPolicyType(elementReader, context);
                targetPolicyTypeProtectionPolicy.setObject(reader, context, targetPolicyType, protectionPolicy);
            } else if (("target" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: target
                TargetType targetItem = readTargetType(elementReader, context);
                if (target == null) {
                    target = targetPolicyTypeTarget.getObject(reader, context, targetPolicyType);
                    if (target!= null) {
                        target.clear();
                    } else {
                        target = new ArrayList<TargetType>();
                    }
                }
                target.add(targetItem);
            } else {
                context.unexpectedElement(elementReader, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "protectionPolicy"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "target"));
            }
        }
        if (target!= null) {
            targetPolicyTypeTarget.setObject(reader, context, targetPolicyType, target);
        }

        context.afterUnmarshal(targetPolicyType, lifecycleCallback);

        return targetPolicyType;
    }

    public final void write(XoXMLStreamWriter writer, TargetPolicyType targetPolicyType, RuntimeContext context)
        throws Exception
    {
        if (targetPolicyType == null) {
            writer.writeXsiNil();
            return ;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        String prefix = writer.getUniquePrefix("http://geronimo.apache.org/xml/ns/geronimo-jaspi");
        if (TargetPolicyType.class!= targetPolicyType.getClass()) {
            context.unexpectedSubclass(writer, targetPolicyType, TargetPolicyType.class);
            return ;
        }

        context.beforeMarshal(targetPolicyType, lifecycleCallback);


        // ELEMENT: protectionPolicy
        ProtectionPolicyType protectionPolicy = targetPolicyTypeProtectionPolicy.getObject(targetPolicyType, context, targetPolicyType);
        if (protectionPolicy!= null) {
            writer.writeStartElement(prefix, "protectionPolicy", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writeProtectionPolicyType(writer, protectionPolicy, context);
            writer.writeEndElement();
        } else {
            context.unexpectedNullValue(targetPolicyType, "protectionPolicy");
        }

        // ELEMENT: target
        List<TargetType> target = targetPolicyTypeTarget.getObject(targetPolicyType, context, targetPolicyType);
        if (target!= null) {
            for (TargetType targetItem: target) {
                writer.writeStartElement(prefix, "target", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
                if (targetItem!= null) {
                    writeTargetType(writer, targetItem, context);
                } else {
                    writer.writeXsiNil();
                }
                writer.writeEndElement();
            }
        }

        context.afterMarshal(targetPolicyType, lifecycleCallback);
    }

}
