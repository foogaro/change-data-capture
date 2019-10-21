package org.infinispan.archetypes;
import static org.infinispan.commons.util.StringPropertyReplacer.replaceProperties;

import javax.xml.stream.XMLStreamException;

import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;
import org.infinispan.configuration.parsing.ConfigurationBuilderHolder;
import org.infinispan.configuration.parsing.ConfigurationParser;
import org.infinispan.configuration.parsing.Namespaces;
import org.infinispan.configuration.parsing.Namespace;
import org.infinispan.configuration.parsing.ParseUtils;
import org.infinispan.configuration.parsing.XMLExtendedStreamReader;

@Namespaces({
   // A version-specific parser for a cache store. If a parser is capable of parsing configuration for multiple versions
   // just add multiple @Namespace annotations, one for each version
   @Namespace(uri = "urn:infinispan:config:my-custom-store:0.0", root = "my-custom-store"),
   // The default parser. This namespace should be applied to the latest version of the parser
   @Namespace(root = "my-custom-store")
})
public class CustomStoreConfigurationParser implements ConfigurationParser {

    @Override
    public Namespace[] getNamespaces() {
        /* 
         * Return the namespaces for which this parser should be used.
         */
        return ParseUtils.getNamespaceAnnotations(this.getClass());
    }

    @Override
    public void readElement(XMLExtendedStreamReader reader,
            ConfigurationBuilderHolder configurationHolder) throws XMLStreamException {
        ConfigurationBuilder builder = configurationHolder.getCurrentConfigurationBuilder();

        Element element = Element.forName(reader.getLocalName());
        switch (element) {
        case SAMPLE_ELEMENT: {
           parseSampleElement(reader, builder.persistence());
           break;
        }
        default: {
           throw ParseUtils.unexpectedElement(reader);
        }
        }
    }
    
    private void parseSampleElement(XMLExtendedStreamReader reader, PersistenceConfigurationBuilder persistenceBuilder)
          throws XMLStreamException {
        CustomStoreConfigurationBuilder storeBuilder = new CustomStoreConfigurationBuilder(persistenceBuilder);
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            ParseUtils.requireNoNamespaceAttribute(reader, i);
            String value = replaceProperties(reader.getAttributeValue(i));
            Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
            case SAMPLE_ATTRIBUTE: {
                storeBuilder.sampleAttribute(value);
                break;
            }
            default: {
                throw ParseUtils.unexpectedAttribute(reader, i);
            }
            }
        }
        ParseUtils.requireNoContent(reader);
    }

}
