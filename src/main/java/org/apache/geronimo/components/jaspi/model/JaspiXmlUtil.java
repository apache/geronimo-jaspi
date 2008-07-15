/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package org.apache.geronimo.components.jaspi.model;

import java.io.Writer;
import java.io.Reader;
import java.io.IOException;
import java.util.Collections;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.apache.geronimo.components.jaspi.ClassLoaderLookup;

/**
 * @version $Rev$ $Date$
 */
public class JaspiXmlUtil {
    public static final XMLInputFactory XMLINPUT_FACTORY = XMLInputFactory.newInstance();
    public static final JAXBContext JASPI_CONTEXT;
    private static ConfigProviderMapAdapter configProviderMapAdapter = new ConfigProviderMapAdapter();

    static {
        try {
//            JASPI_CONTEXT = JAXBContext.newInstance(JaspiType.class);
            JASPI_CONTEXT = com.envoisolutions.sxc.jaxb.JAXBContextImpl.newInstance(new Class[] {JaspiType.class}, Collections.singletonMap("com.envoisolutions.sxc.generate", "false"));

        } catch (JAXBException e) {
            throw new RuntimeException("Could not create jaxb contexts for plugin types", e);
        }
    }

    public static void registerClassLoaderLookup(ClassLoaderLookup classLoaderLookup) {
        configProviderMapAdapter = new ConfigProviderMapAdapter();
    }

    public static void writeJaspi(JaspiType metadata, Writer out) throws XMLStreamException, JAXBException {
        Marshaller marshaller = JASPI_CONTEXT.createMarshaller();
        marshaller.setAdapter(configProviderMapAdapter);
        marshaller.setProperty("jaxb.formatted.output", true);
        JAXBElement<JaspiType> element = new ObjectFactory().createJaspi(metadata);
        marshaller.marshal(element, out);
    }


    public static JaspiType loadJaspi(Reader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        XMLStreamReader xmlStream = XMLINPUT_FACTORY.createXMLStreamReader(in);
        return loadJaspi(xmlStream);
    }

    public static JaspiType loadJaspi(XMLStreamReader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        Unmarshaller unmarshaller = JASPI_CONTEXT.createUnmarshaller();
        unmarshaller.setAdapter(configProviderMapAdapter);
        JAXBElement<JaspiType> element = unmarshaller.unmarshal(in, JaspiType.class);
        JaspiType rbac = element.getValue();
        return rbac;
    }

}
