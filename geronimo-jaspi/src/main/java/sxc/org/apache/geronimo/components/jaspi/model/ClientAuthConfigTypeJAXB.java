
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
import org.apache.geronimo.components.jaspi.model.ClientAuthConfigType;
import org.apache.geronimo.components.jaspi.model.ClientAuthContextType;


import static sxc.org.apache.geronimo.components.jaspi.model.ClientAuthContextTypeJAXB.readClientAuthContextType;
import static sxc.org.apache.geronimo.components.jaspi.model.ClientAuthContextTypeJAXB.writeClientAuthContextType;

@SuppressWarnings({
    "StringEquality"
})
public class ClientAuthConfigTypeJAXB
    extends JAXBObject<ClientAuthConfigType>
{

    public final static ClientAuthConfigTypeJAXB INSTANCE = new ClientAuthConfigTypeJAXB();
    private final static LifecycleCallback lifecycleCallback = new LifecycleCallback(ClientAuthConfigType.class);
    private final static FieldAccessor<ClientAuthConfigType, String> clientAuthConfigTypeMessageLayer = new FieldAccessor<ClientAuthConfigType, String>(ClientAuthConfigType.class, "messageLayer");
    private final static FieldAccessor<ClientAuthConfigType, String> clientAuthConfigTypeAppContext = new FieldAccessor<ClientAuthConfigType, String>(ClientAuthConfigType.class, "appContext");
    private final static FieldAccessor<ClientAuthConfigType, String> clientAuthConfigTypeAuthenticationContextID = new FieldAccessor<ClientAuthConfigType, String>(ClientAuthConfigType.class, "authenticationContextID");
    private final static FieldAccessor<ClientAuthConfigType, Boolean> clientAuthConfigType_protected = new FieldAccessor<ClientAuthConfigType, Boolean>(ClientAuthConfigType.class, "_protected");
    private final static FieldAccessor<ClientAuthConfigType, List<ClientAuthContextType>> clientAuthConfigTypeClientAuthContext = new FieldAccessor<ClientAuthConfigType, List<ClientAuthContextType>>(ClientAuthConfigType.class, "clientAuthContext");

    public ClientAuthConfigTypeJAXB() {
        super(ClientAuthConfigType.class, null, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "clientAuthConfigType".intern()), ClientAuthContextTypeJAXB.class);
    }

    public static ClientAuthConfigType readClientAuthConfigType(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        return INSTANCE.read(reader, context);
    }

    public static void writeClientAuthConfigType(XoXMLStreamWriter writer, ClientAuthConfigType clientAuthConfigType, RuntimeContext context)
        throws Exception
    {
        INSTANCE.write(writer, clientAuthConfigType, context);
    }

    public final ClientAuthConfigType read(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {

        // Check for xsi:nil
        if (reader.isXsiNil()) {
            return null;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        ClientAuthConfigType clientAuthConfigType = new ClientAuthConfigType();
        context.beforeUnmarshal(clientAuthConfigType, lifecycleCallback);

        List<ClientAuthContextType> clientAuthContext = null;

        // Check xsi:type
        QName xsiType = reader.getXsiType();
        if (xsiType!= null) {
            if (("clientAuthConfigType"!= xsiType.getLocalPart())||("http://geronimo.apache.org/xml/ns/geronimo-jaspi"!= xsiType.getNamespaceURI())) {
                return context.unexpectedXsiType(reader, ClientAuthConfigType.class);
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
                clientAuthConfigTypeMessageLayer.setObject(reader, context, clientAuthConfigType, messageLayer);
            } else if (("appContext" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: appContext
                String appContext = elementReader.getElementAsString();
                clientAuthConfigTypeAppContext.setObject(reader, context, clientAuthConfigType, appContext);
            } else if (("authenticationContextID" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: authenticationContextID
                String authenticationContextID = elementReader.getElementAsString();
                clientAuthConfigTypeAuthenticationContextID.setObject(reader, context, clientAuthConfigType, authenticationContextID);
            } else if (("protected" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: _protected
                Boolean _protected = ("1".equals(elementReader.getElementAsString())||"true".equals(elementReader.getElementAsString()));
                clientAuthConfigType_protected.setBoolean(reader, context, clientAuthConfigType, _protected);
            } else if (("clientAuthContext" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: clientAuthContext
                ClientAuthContextType clientAuthContextItem = readClientAuthContextType(elementReader, context);
                if (clientAuthContext == null) {
                    clientAuthContext = clientAuthConfigTypeClientAuthContext.getObject(reader, context, clientAuthConfigType);
                    if (clientAuthContext!= null) {
                        clientAuthContext.clear();
                    } else {
                        clientAuthContext = new ArrayList<ClientAuthContextType>();
                    }
                }
                clientAuthContext.add(clientAuthContextItem);
            } else {
                context.unexpectedElement(elementReader, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "messageLayer"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "appContext"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "authenticationContextID"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "protected"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "clientAuthContext"));
            }
        }
        if (clientAuthContext!= null) {
            clientAuthConfigTypeClientAuthContext.setObject(reader, context, clientAuthConfigType, clientAuthContext);
        }

        context.afterUnmarshal(clientAuthConfigType, lifecycleCallback);

        return clientAuthConfigType;
    }

    public final void write(XoXMLStreamWriter writer, ClientAuthConfigType clientAuthConfigType, RuntimeContext context)
        throws Exception
    {
        if (clientAuthConfigType == null) {
            writer.writeXsiNil();
            return ;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        String prefix = writer.getUniquePrefix("http://geronimo.apache.org/xml/ns/geronimo-jaspi");
        if (ClientAuthConfigType.class!= clientAuthConfigType.getClass()) {
            context.unexpectedSubclass(writer, clientAuthConfigType, ClientAuthConfigType.class);
            return ;
        }

        context.beforeMarshal(clientAuthConfigType, lifecycleCallback);


        // ELEMENT: messageLayer
        String messageLayer = clientAuthConfigTypeMessageLayer.getObject(clientAuthConfigType, context, clientAuthConfigType);
        if (messageLayer!= null) {
            writer.writeStartElement(prefix, "messageLayer", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(messageLayer);
            writer.writeEndElement();
        }

        // ELEMENT: appContext
        String appContext = clientAuthConfigTypeAppContext.getObject(clientAuthConfigType, context, clientAuthConfigType);
        if (appContext!= null) {
            writer.writeStartElement(prefix, "appContext", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(appContext);
            writer.writeEndElement();
        }

        // ELEMENT: authenticationContextID
        String authenticationContextID = clientAuthConfigTypeAuthenticationContextID.getObject(clientAuthConfigType, context, clientAuthConfigType);
        if (authenticationContextID!= null) {
            writer.writeStartElement(prefix, "authenticationContextID", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(authenticationContextID);
            writer.writeEndElement();
        } else {
            context.unexpectedNullValue(clientAuthConfigType, "authenticationContextID");
        }

        // ELEMENT: _protected
        Boolean _protected = clientAuthConfigType_protected.getBoolean(clientAuthConfigType, context, clientAuthConfigType);
        writer.writeStartElement(prefix, "protected", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
        writer.writeCharacters(Boolean.toString(_protected));
        writer.writeEndElement();

        // ELEMENT: clientAuthContext
        List<ClientAuthContextType> clientAuthContext = clientAuthConfigTypeClientAuthContext.getObject(clientAuthConfigType, context, clientAuthConfigType);
        if (clientAuthContext!= null) {
            for (ClientAuthContextType clientAuthContextItem: clientAuthContext) {
                writer.writeStartElement(prefix, "clientAuthContext", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
                if (clientAuthContextItem!= null) {
                    writeClientAuthContextType(writer, clientAuthContextItem, context);
                } else {
                    writer.writeXsiNil();
                }
                writer.writeEndElement();
            }
        }

        context.afterMarshal(clientAuthConfigType, lifecycleCallback);
    }

}
