
package sxc.org.apache.geronimo.components.jaspi.model;

import java.util.Map;
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
import org.apache.geronimo.components.jaspi.model.KeyedObjectMapAdapter;
import org.apache.geronimo.components.jaspi.model.ConfigProviderType;
import org.apache.geronimo.components.jaspi.model.JaspiType;
import static sxc.org.apache.geronimo.components.jaspi.model.ConfigProviderTypeJAXB.readConfigProviderType;
import static sxc.org.apache.geronimo.components.jaspi.model.ConfigProviderTypeJAXB.writeConfigProviderType;

@SuppressWarnings({
    "StringEquality"
})
public class JaspiTypeJAXB
    extends JAXBObject<JaspiType>
{

    public final static JaspiTypeJAXB INSTANCE = new JaspiTypeJAXB();
    private final static LifecycleCallback lifecycleCallback = new LifecycleCallback(JaspiType.class);
    private final static FieldAccessor<JaspiType, Map<String, ConfigProviderType>> jaspiTypeConfigProvider = new FieldAccessor<JaspiType, Map<String, ConfigProviderType>>(JaspiType.class, "configProvider");
    private final static KeyedObjectMapAdapter<ConfigProviderType> configProviderMapAdapterAdapter = new KeyedObjectMapAdapter<ConfigProviderType>(ConfigProviderType.class);

    public JaspiTypeJAXB() {
        super(JaspiType.class, null, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "jaspiType".intern()));
    }

    public static JaspiType readJaspiType(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {
        return INSTANCE.read(reader, context);
    }

    public static void writeJaspiType(XoXMLStreamWriter writer, JaspiType jaspiType, RuntimeContext context)
        throws Exception
    {
        INSTANCE.write(writer, jaspiType, context);
    }

    public final JaspiType read(XoXMLStreamReader reader, RuntimeContext context)
        throws Exception
    {

        // Check for xsi:nil
        if (reader.isXsiNil()) {
            return null;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        JaspiType jaspiType = new JaspiType();
        context.beforeUnmarshal(jaspiType, lifecycleCallback);

        List<ConfigProviderType> configProviderRaw = null;

        // Check xsi:type
        QName xsiType = reader.getXsiType();
        if (xsiType!= null) {
            if (("jaspiType"!= xsiType.getLocalPart())||("http://geronimo.apache.org/xml/ns/geronimo-jaspi"!= xsiType.getNamespaceURI())) {
                return context.unexpectedXsiType(reader, JaspiType.class);
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
            if (("configProvider" == elementReader.getLocalName())&&("http://geronimo.apache.org/xml/ns/geronimo-jaspi" == elementReader.getNamespaceURI())) {
                // ELEMENT: configProvider
                ConfigProviderType configProviderItem = readConfigProviderType(elementReader, context);
                if (configProviderRaw == null) {
                    configProviderRaw = new ArrayList<ConfigProviderType>();
                }
                configProviderRaw.add(configProviderItem);

            } else {
                context.unexpectedElement(elementReader, new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "configProvider"));
            }
        }

        Map<String, ConfigProviderType> configProvider;
        try {
            ConfigProviderType[] configProviderArray = configProviderRaw == null? null: configProviderRaw.toArray(new ConfigProviderType[configProviderRaw.size()]);
            configProvider = configProviderMapAdapterAdapter.unmarshal(configProviderArray);
            jaspiTypeConfigProvider.setObject(reader, context, jaspiType, configProvider);
        } catch (Exception e) {
//            context.xmlAdapterError(null, KeyedObjectMapAdapter.class, Map.class, Map.class, e);
            throw e;
        }


        context.afterUnmarshal(jaspiType, lifecycleCallback);

        return jaspiType;
    }

    public final void write(XoXMLStreamWriter writer, JaspiType jaspiType, RuntimeContext context)
        throws Exception
    {
        if (jaspiType == null) {
            writer.writeXsiNil();
            return ;
        }

        if (context == null) {
            context = new RuntimeContext();
        }

        if (JaspiType.class!= jaspiType.getClass()) {
            context.unexpectedSubclass(writer, jaspiType, JaspiType.class);
            return ;
        }

        context.beforeMarshal(jaspiType, lifecycleCallback);


        // ELEMENT: configProvider
        Map<String, ConfigProviderType> configProviderRaw = jaspiTypeConfigProvider.getObject(jaspiType, context, jaspiType);
        ConfigProviderType[] configProvider = null;
        try {
            configProvider = configProviderMapAdapterAdapter.marshal(configProviderRaw);
        } catch (Exception e) {
            context.xmlAdapterError(jaspiType, "configProvider", KeyedObjectMapAdapter.class, Map.class, Map.class, e);
        }
        if (configProvider!= null) {
            for (ConfigProviderType configProviderItem: configProvider) {
                writer.writeStartElementWithAutoPrefix("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "configProvider");
                if (configProviderItem!= null) {
                    writeConfigProviderType(writer, configProviderItem, context);
                } else {
                    writer.writeXsiNil();
                }
                writer.writeEndElement();
            }
        }

        context.afterMarshal(jaspiType, lifecycleCallback);
    }

}
