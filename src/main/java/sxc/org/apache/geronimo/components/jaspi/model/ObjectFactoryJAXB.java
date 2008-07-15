
package sxc.org.apache.geronimo.components.jaspi.model;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import com.envoisolutions.sxc.jaxb.JAXBObject;
import com.envoisolutions.sxc.jaxb.JAXBObjectFactory;
import org.apache.geronimo.components.jaspi.model.ObjectFactory;

public class ObjectFactoryJAXB
    extends JAXBObjectFactory<ObjectFactory>
{

    public final static ObjectFactoryJAXB INSTANCE = new ObjectFactoryJAXB();
    private final Map<QName, Class<? extends JAXBObject>> rootElements = new HashMap<QName, Class<? extends JAXBObject>>();

    public ObjectFactoryJAXB() {
        super(ObjectFactory.class, ConfigProviderTypeJAXB.class, JaspiTypeJAXB.class);
        rootElements.put(new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "jaspi".intern()), JaspiTypeJAXB.class);
    }

    public Map<QName, Class<? extends JAXBObject>> getRootElements() {
        return rootElements;
    }

}
