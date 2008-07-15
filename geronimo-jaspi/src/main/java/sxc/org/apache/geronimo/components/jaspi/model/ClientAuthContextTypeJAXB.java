
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
import org.apache.geronimo.components.jaspi.model.AuthModuleType;
import org.apache.geronimo.components.jaspi.model.ClientAuthContextType;


import static sxc.org.apache.geronimo.components.jaspi.model.AuthModuleTypeJAXB.readAuthModuleType;
import static sxc.org.apache.geronimo.components.jaspi.model.AuthModuleTypeJAXB.writeAuthModuleType;

@SuppressWarnings({
    "StringEquality"
})
public class ClientAuthContextTypeJAXB
    extends JAXBObject<ClientAuthContextType>
{

    public final static ClientAuthContextTypeJAXB INSTANCE = new ClientAuthContextTypeJAXB();
    private final static LifecycleCallback lifecycleCallback = new LifecycleCallback(ClientAuthContextType.class);
    private final static FieldAccessor<ClientAuthContextType, String> clientAuthContextTypeMessageLayer = new FieldAccessor<ClientAuthContextType, String>(ClientAuthContextType.class, "messageLayer");
    private final static FieldAccessor<ClientAuthContextType, String> clientAuthContextTypeAppContext = new FieldAccessor<ClientAuthContextType, String>(ClientAuthContextType.class, "appContext");
    private final static FieldAccessor<ClientAuthContextType, String> clientAuthContextTypeAuthenticationContextID = new FieldAccessor<ClientAuthContextType, String>(ClientAuthContextType.class, "authenticationContextID");
    private final static FieldAccessor<ClientAuthContextType, List<AuthModuleType>> clientAuthContextTypeClientAuthModule = new FieldAccessor<ClientAuthContextType, List<AuthModuleType>>(ClientAuthContextType.class, "clientAuthModule");

    public ClientAuthContextTypeJAXB() {
        super(ClientAuthContextType.class, null, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "clientAuthContextType".intern()), AuthModuleTypeJAXB.class);
    }

    public static ClientAuthContextType readClientAuthContextType(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        return INSTANCE.read(reader, context);
    }

    public static void writeClientAuthContextType(XoXMLStreamWriter writer, ClientAuthContextType clientAuthContextType, RuntimeContext context)
        throws Exception
    {
        INSTANCE.write(writer, clientAuthContextType, context);
    }

    public final ClientAuthContextType read(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {

        // Check for xsi:nil
        if (reader.isXsiNil()) {
            return null;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        ClientAuthContextType clientAuthContextType = new ClientAuthContextType();
        context.beforeUnmarshal(clientAuthContextType, lifecycleCallback);

        List<AuthModuleType> clientAuthModule = null;

        // Check xsi:type
        QName xsiType = reader.getXsiType();
        if (xsiType!= null) {
            if (("clientAuthContextType"!= xsiType.getLocalPart())||("http://geronimo.apache.org/xml/ns/geronimo-jaspi"!= xsiType.getNamespaceURI())) {
                return context.unexpectedXsiType(reader, ClientAuthContextType.class);
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
                clientAuthContextTypeMessageLayer.setObject(reader, context, clientAuthContextType, messageLayer);
            } else if (("appContext" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: appContext
                String appContext = elementReader.getElementAsString();
                clientAuthContextTypeAppContext.setObject(reader, context, clientAuthContextType, appContext);
            } else if (("authenticationContextID" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: authenticationContextID
                String authenticationContextID = elementReader.getElementAsString();
                clientAuthContextTypeAuthenticationContextID.setObject(reader, context, clientAuthContextType, authenticationContextID);
            } else if (("clientAuthModule" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: clientAuthModule
                AuthModuleType clientAuthModuleItem = readAuthModuleType(elementReader, context);
                if (clientAuthModule == null) {
                    clientAuthModule = clientAuthContextTypeClientAuthModule.getObject(reader, context, clientAuthContextType);
                    if (clientAuthModule!= null) {
                        clientAuthModule.clear();
                    } else {
                        clientAuthModule = new ArrayList<AuthModuleType>();
                    }
                }
                clientAuthModule.add(clientAuthModuleItem);
            } else {
                context.unexpectedElement(elementReader, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "messageLayer"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "appContext"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "authenticationContextID"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "clientAuthModule"));
            }
        }
        if (clientAuthModule!= null) {
            clientAuthContextTypeClientAuthModule.setObject(reader, context, clientAuthContextType, clientAuthModule);
        }

        context.afterUnmarshal(clientAuthContextType, lifecycleCallback);

        return clientAuthContextType;
    }

    public final void write(XoXMLStreamWriter writer, ClientAuthContextType clientAuthContextType, RuntimeContext context)
        throws Exception
    {
        if (clientAuthContextType == null) {
            writer.writeXsiNil();
            return ;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        String prefix = writer.getUniquePrefix("http://geronimo.apache.org/xml/ns/geronimo-jaspi");
        if (ClientAuthContextType.class!= clientAuthContextType.getClass()) {
            context.unexpectedSubclass(writer, clientAuthContextType, ClientAuthContextType.class);
            return ;
        }

        context.beforeMarshal(clientAuthContextType, lifecycleCallback);


        // ELEMENT: messageLayer
        String messageLayer = clientAuthContextTypeMessageLayer.getObject(clientAuthContextType, context, clientAuthContextType);
        if (messageLayer!= null) {
            writer.writeStartElement(prefix, "messageLayer", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(messageLayer);
            writer.writeEndElement();
        }

        // ELEMENT: appContext
        String appContext = clientAuthContextTypeAppContext.getObject(clientAuthContextType, context, clientAuthContextType);
        if (appContext!= null) {
            writer.writeStartElement(prefix, "appContext", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(appContext);
            writer.writeEndElement();
        }

        // ELEMENT: authenticationContextID
        String authenticationContextID = clientAuthContextTypeAuthenticationContextID.getObject(clientAuthContextType, context, clientAuthContextType);
        if (authenticationContextID!= null) {
            writer.writeStartElement(prefix, "authenticationContextID", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(authenticationContextID);
            writer.writeEndElement();
        } else {
            context.unexpectedNullValue(clientAuthContextType, "authenticationContextID");
        }

        // ELEMENT: clientAuthModule
        List<AuthModuleType> clientAuthModule = clientAuthContextTypeClientAuthModule.getObject(clientAuthContextType, context, clientAuthContextType);
        if (clientAuthModule!= null) {
            for (AuthModuleType clientAuthModuleItem: clientAuthModule) {
                writer.writeStartElement(prefix, "clientAuthModule", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
                if (clientAuthModuleItem!= null) {
                    writeAuthModuleType(writer, clientAuthModuleItem, context);
                } else {
                    writer.writeXsiNil();
                }
                writer.writeEndElement();
            }
        }

        context.afterMarshal(clientAuthContextType, lifecycleCallback);
    }

}
