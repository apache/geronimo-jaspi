
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
import org.apache.geronimo.components.jaspi.model.ServerAuthContextType;


import static sxc.org.apache.geronimo.components.jaspi.model.AuthModuleTypeJAXB.readAuthModuleType;
import static sxc.org.apache.geronimo.components.jaspi.model.AuthModuleTypeJAXB.writeAuthModuleType;

@SuppressWarnings({
    "StringEquality"
})
public class ServerAuthContextTypeJAXB
    extends JAXBObject<ServerAuthContextType>
{

    public final static ServerAuthContextTypeJAXB INSTANCE = new ServerAuthContextTypeJAXB();
    private final static LifecycleCallback lifecycleCallback = new LifecycleCallback(ServerAuthContextType.class);
    private final static FieldAccessor<ServerAuthContextType, String> serverAuthContextTypeMessageLayer = new FieldAccessor<ServerAuthContextType, String>(ServerAuthContextType.class, "messageLayer");
    private final static FieldAccessor<ServerAuthContextType, String> serverAuthContextTypeAppContext = new FieldAccessor<ServerAuthContextType, String>(ServerAuthContextType.class, "appContext");
    private final static FieldAccessor<ServerAuthContextType, String> serverAuthContextTypeAuthenticationContextID = new FieldAccessor<ServerAuthContextType, String>(ServerAuthContextType.class, "authenticationContextID");
    private final static FieldAccessor<ServerAuthContextType, List<AuthModuleType>> serverAuthContextTypeServerAuthModule = new FieldAccessor<ServerAuthContextType, List<AuthModuleType>>(ServerAuthContextType.class, "serverAuthModule");

    public ServerAuthContextTypeJAXB() {
        super(ServerAuthContextType.class, null, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "serverAuthContextType".intern()), AuthModuleTypeJAXB.class);
    }

    public static ServerAuthContextType readServerAuthContextType(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        return INSTANCE.read(reader, context);
    }

    public static void writeServerAuthContextType(XoXMLStreamWriter writer, ServerAuthContextType serverAuthContextType, RuntimeContext context)
        throws Exception
    {
        INSTANCE.write(writer, serverAuthContextType, context);
    }

    public final ServerAuthContextType read(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {

        // Check for xsi:nil
        if (reader.isXsiNil()) {
            return null;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        ServerAuthContextType serverAuthContextType = new ServerAuthContextType();
        context.beforeUnmarshal(serverAuthContextType, lifecycleCallback);

        List<AuthModuleType> serverAuthModule = null;

        // Check xsi:type
        QName xsiType = reader.getXsiType();
        if (xsiType!= null) {
            if (("serverAuthContextType"!= xsiType.getLocalPart())||("http://geronimo.apache.org/xml/ns/geronimo-jaspi"!= xsiType.getNamespaceURI())) {
                return context.unexpectedXsiType(reader, ServerAuthContextType.class);
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
                serverAuthContextTypeMessageLayer.setObject(reader, context, serverAuthContextType, messageLayer);
            } else if (("appContext" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: appContext
                String appContext = elementReader.getElementAsString();
                serverAuthContextTypeAppContext.setObject(reader, context, serverAuthContextType, appContext);
            } else if (("authenticationContextID" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: authenticationContextID
                String authenticationContextID = elementReader.getElementAsString();
                serverAuthContextTypeAuthenticationContextID.setObject(reader, context, serverAuthContextType, authenticationContextID);
            } else if (("serverAuthModule" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: serverAuthModule
                AuthModuleType serverAuthModuleItem = readAuthModuleType(elementReader, context);
                if (serverAuthModule == null) {
                    serverAuthModule = serverAuthContextTypeServerAuthModule.getObject(reader, context, serverAuthContextType);
                    if (serverAuthModule!= null) {
                        serverAuthModule.clear();
                    } else {
                        serverAuthModule = new ArrayList<AuthModuleType>();
                    }
                }
                serverAuthModule.add(serverAuthModuleItem);
            } else {
                context.unexpectedElement(elementReader, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "messageLayer"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "appContext"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "authenticationContextID"), new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "serverAuthModule"));
            }
        }
        if (serverAuthModule!= null) {
            serverAuthContextTypeServerAuthModule.setObject(reader, context, serverAuthContextType, serverAuthModule);
        }

        context.afterUnmarshal(serverAuthContextType, lifecycleCallback);

        return serverAuthContextType;
    }

    public final void write(XoXMLStreamWriter writer, ServerAuthContextType serverAuthContextType, RuntimeContext context)
        throws Exception
    {
        if (serverAuthContextType == null) {
            writer.writeXsiNil();
            return ;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        String prefix = writer.getUniquePrefix("http://geronimo.apache.org/xml/ns/geronimo-jaspi");
        if (ServerAuthContextType.class!= serverAuthContextType.getClass()) {
            context.unexpectedSubclass(writer, serverAuthContextType, ServerAuthContextType.class);
            return ;
        }

        context.beforeMarshal(serverAuthContextType, lifecycleCallback);


        // ELEMENT: messageLayer
        String messageLayer = serverAuthContextTypeMessageLayer.getObject(serverAuthContextType, context, serverAuthContextType);
        if (messageLayer!= null) {
            writer.writeStartElement(prefix, "messageLayer", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(messageLayer);
            writer.writeEndElement();
        }

        // ELEMENT: appContext
        String appContext = serverAuthContextTypeAppContext.getObject(serverAuthContextType, context, serverAuthContextType);
        if (appContext!= null) {
            writer.writeStartElement(prefix, "appContext", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(appContext);
            writer.writeEndElement();
        }

        // ELEMENT: authenticationContextID
        String authenticationContextID = serverAuthContextTypeAuthenticationContextID.getObject(serverAuthContextType, context, serverAuthContextType);
        if (authenticationContextID!= null) {
            writer.writeStartElement(prefix, "authenticationContextID", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
            writer.writeCharacters(authenticationContextID);
            writer.writeEndElement();
        } else {
            context.unexpectedNullValue(serverAuthContextType, "authenticationContextID");
        }

        // ELEMENT: serverAuthModule
        List<AuthModuleType> serverAuthModule = serverAuthContextTypeServerAuthModule.getObject(serverAuthContextType, context, serverAuthContextType);
        if (serverAuthModule!= null) {
            for (AuthModuleType serverAuthModuleItem: serverAuthModule) {
                writer.writeStartElement(prefix, "serverAuthModule", "http://geronimo.apache.org/xml/ns/geronimo-jaspi");
                if (serverAuthModuleItem!= null) {
                    writeAuthModuleType(writer, serverAuthModuleItem, context);
                } else {
                    writer.writeXsiNil();
                }
                writer.writeEndElement();
            }
        }

        context.afterMarshal(serverAuthContextType, lifecycleCallback);
    }

}
