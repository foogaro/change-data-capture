package org.infinispan.archetypes;

import static org.infinispan.archetypes.CustomStoreConfiguration.SAMPLE_ATTRIBUTE;

import org.infinispan.configuration.cache.AbstractStoreConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;

public class CustomStoreConfigurationBuilder extends AbstractStoreConfigurationBuilder<CustomStoreConfiguration, CustomStoreConfigurationBuilder>{

    public CustomStoreConfigurationBuilder(
          PersistenceConfigurationBuilder builder) {
        super(builder, CustomStoreConfiguration.attributeDefinitionSet());
        // TODO Auto-generated constructor stub
    }

    public CustomStoreConfigurationBuilder sampleAttribute(String sampleAttribute) {
        // TODO Auto-generated method stub
        attributes.attribute(SAMPLE_ATTRIBUTE).set(sampleAttribute);
        return this;
    }

    @Override
    public CustomStoreConfiguration create() {
        // TODO Auto-generated method stub
        return new CustomStoreConfiguration(attributes.protect(), async.create(), singletonStore.create());
    }

    @Override
    public CustomStoreConfigurationBuilder self() {
        // TODO Auto-generated method stub
        return this;
    }
}
