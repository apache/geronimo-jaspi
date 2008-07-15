
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
import org.apache.geronimo.components.jaspi.model.TargetType;

@SuppressWarnings({
    "StringEquality"
})
public class TargetTypeJAXB
    extends JAXBObject<TargetType>
{

    public final static TargetTypeJAXB INSTANCE = new TargetTypeJAXB();
    private final static LifecycleCallback lifecycleCallback = new LifecycleCallback(TargetType.class);
    private final static FieldAccessor<TargetType, String> targetTypeClassName = new FieldAccessor<TargetType, String>(TargetType.class, "className");

    public TargetTypeJAXB() {
        super(TargetType.class, null, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "targetType".intern()));
    }

    public static TargetType readTargetType(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        return INSTANCE.read(reader, context);
    }

    public static void writeTargetType(XoXMLStreamWriter writer, TargetType targetType, RuntimeContext context)
        throws Exception
    {
        INSTANCE.write(writer, targetType, context);
    }

    public final TargetType read(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {

        // Check for xsi:nil
        if (reader.isXsiNil()) {
            return null;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        TargetType targetType = new TargetType();
        context.beforeUnmarshal(targetType, lifecycleCallback);


        // Check xsi:type
        QName xsiType = reader.getXsiType();
        if (xsiType!= null) {
            if (("targetType"!= xsiType.getLocalPart())||("http://geronimo.apache.org/xml/ns/geronimo-jaspi"!= xsiType.getNamespaceURI())) {
                return context.unexpectedXsiType(reader, TargetType.class);
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
                targetTypeClassName.setObject(reader, context, targetType, className);
            } else {
                context.unexpectedElement(elementReader, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "className"));
            }
        }

        context.afterUnmarshal(targetType, lifecycleCallback);

        return targetType;
    }

    public final void write(XoXMLStreamWriter writer, TargetType targetType, RuntimeContext context)
        throws Exception
    {
        if (targetType == null) {
            writer.writeXsiNil();
            return ;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        if (TargetType.class!= targetType.getClass()) {
            context.unexpectedSubclass(writer, targetType, TargetType.class);
            return ;
        }

        context.beforeMarshal(targetType, lifecycleCallback);


        // ELEMENT: className
        String className = targetTypeClassName.getObject(targetType, context, targetType);
        if (className!= null) {
            writer.writeStartElementWithAutoPrefix("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "className");
            writer.writeCharacters(className);
            writer.writeEndElement();
        } else {
            context.unexpectedNullValue(targetType, "className");
        }

        context.afterMarshal(targetType, lifecycleCallback);
    }

}
