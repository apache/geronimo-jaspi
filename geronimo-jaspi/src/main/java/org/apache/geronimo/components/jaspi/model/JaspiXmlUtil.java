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
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.module.ServerAuthModule;
import javax.security.auth.message.module.ClientAuthModule;

import org.xml.sax.SAXException;
import org.apache.geronimo.components.jaspi.ClassLoaderLookup;

/**
 * @version $Rev$ $Date$
 */
public class JaspiXmlUtil {
    public static final XMLInputFactory XMLINPUT_FACTORY = XMLInputFactory.newInstance();
    public static final JAXBContext JASPI_CONTEXT;
//    private static KeyedObjectMapAdapter configProviderMapAdapter = new KeyedObjectMapAdapter(ConfigProviderType.class);

    static {
        try {
//            JASPI_CONTEXT = JAXBContext.newInstance(JaspiType.class);
            JASPI_CONTEXT = com.envoisolutions.sxc.jaxb.JAXBContextImpl.newInstance(new Class[] {JaspiType.class, ConfigProviderType.class, ClientAuthConfigType.class, ClientAuthContextType.class, ServerAuthConfigType.class, ServerAuthContextType.class, AuthModuleType.class}, Collections.singletonMap("com.envoisolutions.sxc.generate", "false"));

        } catch (JAXBException e) {
            throw new RuntimeException("Could not create jaxb contexts for plugin types", e);
        }
    }

    public static void initialize(ClassLoaderLookup classLoaderLookup, CallbackHandler callbackHandler) {
//        configProviderMapAdapter = new KeyedObjectMapAdapter<ConfigProviderType>(classLoaderLookup, callbackHandler, ConfigProviderType.class);
        KeyedObjectMapAdapter.staticClassLoaderLookup = classLoaderLookup;
        KeyedObjectMapAdapter.staticCallbackHandler = callbackHandler;
    }

    public static <T> void write(JAXBElement<T> element, Writer out) throws JAXBException {
        Marshaller marshaller = JASPI_CONTEXT.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output", true);
        marshaller.marshal(element, out);
    }

    public static <T>T load(Reader in, Class<T> clazz) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        XMLStreamReader xmlStream = XMLINPUT_FACTORY.createXMLStreamReader(in);
        try {
            return load(xmlStream, clazz);
        } finally {
            xmlStream.close();
        }
    }

    public static Object untypedLoad(Reader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        XMLStreamReader xmlStream = XMLINPUT_FACTORY.createXMLStreamReader(in);
        try {
            return untypedLoad(xmlStream);
        } finally {
            xmlStream.close();
        }
    }

    public static <T>T load(XMLStreamReader in, Class<T> clazz) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        Unmarshaller unmarshaller = JASPI_CONTEXT.createUnmarshaller();
        JAXBElement<T> element = unmarshaller.unmarshal(in, clazz);
        return element.getValue();
    }

    public static Object untypedLoad(XMLStreamReader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        Unmarshaller unmarshaller = JASPI_CONTEXT.createUnmarshaller();
        return unmarshaller.unmarshal(in);
    }

    public static void writeJaspi(JaspiType metadata, Writer out) throws XMLStreamException, JAXBException {
        write(new ObjectFactory().createJaspi(metadata), out);
    }

    public static JaspiType loadJaspi(Reader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, JaspiType.class);
    }

    public static JaspiType loadJaspi(XMLStreamReader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, JaspiType.class);
    }

    public static void writeConfigProvider(ConfigProviderType metadata, Writer out) throws XMLStreamException, JAXBException {
        write(new ObjectFactory().createConfigProvider(metadata), out);
    }

    public static ConfigProviderType loadConfigProvider(Reader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, ConfigProviderType.class);
    }

    public static ConfigProviderType loadConfigProvider(XMLStreamReader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, ConfigProviderType.class);
    }


    public static void writeClientAuthConfig(ClientAuthConfigType metadata, Writer out) throws XMLStreamException, JAXBException {
        write(new ObjectFactory().createClientAuthConfig(metadata), out);
    }

    public static ClientAuthConfigType loadClientAuthConfig(Reader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, ClientAuthConfigType.class);
    }

    public static ClientAuthConfigType loadClientAuthConfig(XMLStreamReader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, ClientAuthConfigType.class);
    }


    public static void writeClientAuthContext(ClientAuthContextType metadata, Writer out) throws XMLStreamException, JAXBException {
        write(new ObjectFactory().createClientAuthContext(metadata), out);
    }

    public static ClientAuthContextType loadClientAuthContext(Reader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, ClientAuthContextType.class);
    }

    public static ClientAuthContextType loadClientAuthContext(XMLStreamReader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, ClientAuthContextType.class);
    }

    
    public static void writeClientAuthModule(AuthModuleType metadata, Writer out) throws XMLStreamException, JAXBException {
        write(new ObjectFactory().createClientAuthModule(metadata), out);
    }

    public static AuthModuleType<ClientAuthModule> loadClientAuthModule(Reader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, AuthModuleType.class);
    }

    public static AuthModuleType<ClientAuthModule> loadClientAuthModule(XMLStreamReader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, AuthModuleType.class);
    }



    public static void writeServerAuthConfig(ServerAuthConfigType metadata, Writer out) throws XMLStreamException, JAXBException {
        write(new ObjectFactory().createServerAuthConfig(metadata), out);
    }

    public static ServerAuthConfigType loadServerAuthConfig(Reader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, ServerAuthConfigType.class);
    }

    public static ServerAuthConfigType loadServerAuthConfig(XMLStreamReader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, ServerAuthConfigType.class);
    }


    public static void writeServerAuthContext(ServerAuthContextType metadata, Writer out) throws XMLStreamException, JAXBException {
        write(new ObjectFactory().createServerAuthContext(metadata), out);
    }

    public static ServerAuthContextType loadServerAuthContext(Reader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, ServerAuthContextType.class);
    }

    public static ServerAuthContextType loadServerAuthContext(XMLStreamReader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, ServerAuthContextType.class);
    }


    public static void writeServerAuthModule(AuthModuleType metadata, Writer out) throws XMLStreamException, JAXBException {
        write(new ObjectFactory().createServerAuthModule(metadata), out);
    }

    public static AuthModuleType<ServerAuthModule> loadServerAuthModule(Reader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, AuthModuleType.class);
    }

    public static AuthModuleType<ServerAuthModule> loadServerAuthModule(XMLStreamReader in) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        return load(in, AuthModuleType.class);
    }




}
