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

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import com.envoisolutions.sxc.jaxb.JAXBObject;
import com.envoisolutions.sxc.jaxb.JAXBObjectFactory;
import org.apache.geronimo.components.jaspi.model.ObjectFactory;

/**
 * @version $Rev$ $Date$
 */

public class ObjectFactoryJAXB
    extends JAXBObjectFactory<ObjectFactory>
{

    public final static ObjectFactoryJAXB INSTANCE = new ObjectFactoryJAXB();
    private final Map<QName, Class<? extends JAXBObject>> rootElements = new HashMap<QName, Class<? extends JAXBObject>>();

    public ObjectFactoryJAXB() {
        super(ObjectFactory.class, JaspiTypeJAXB.class, ServerAuthContextTypeJAXB.class, AuthModuleTypeJAXB.class, ServerAuthConfigTypeJAXB.class, TargetTypeJAXB.class, MessagePolicyTypeJAXB.class, ClientAuthContextTypeJAXB.class, ConfigProviderTypeJAXB.class, TargetPolicyTypeJAXB.class, ClientAuthConfigTypeJAXB.class, ProtectionPolicyTypeJAXB.class);
        rootElements.put(new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi".intern(), "jaspi".intern()), JaspiTypeJAXB.class);
    }

    public Map<QName, Class<? extends JAXBObject>> getRootElements() {
        return rootElements;
    }

}
