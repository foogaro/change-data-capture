package org.infinispan.archetypes;

import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.marshall.core.MarshalledEntry;
import org.infinispan.persistence.spi.CacheLoader;
import org.infinispan.persistence.spi.InitializationContext;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@ConfiguredBy(CustomStoreConfiguration.class)
public class CustomCacheLoader<K,V> implements CacheLoader<K, V> {
   
    @Override
    public void init(InitializationContext ctx) {
        /*
         * This method will be invoked by the PersistenceManager during initialization. The InitializationContext
         * contains:
         * - this CacheLoader's configuration
         * - the cache to which this loader is applied. Your loader might want to use the cache's name to construct 
         *   cache-specific identifiers
         * - the StreamingMarshaller that needs to be used to marshall/unmarshall the entries
         * - a TimeService which the loader can use to determine expired entries
         * - a ByteBufferFactory which needs to be used to construct ByteBuffers
         * - a MarshalledEntryFactory which needs to be used to construct entries from the data retrieved by the loader 
         */
    }

    @Override
    public void start() {
        /*
         * This method will be invoked by the PersistenceManager to start the CacheLoader. At this stage configuration
         * is complete and the loader can perform operations such as opening a connection to the external storage,
         * initialize internal data structures, etc.
         */
        
    }

    @Override
    public void stop() {
        /*
         * This method will be invoked by the PersistenceManager to stop the CacheLoader. The CacheLoader should close any
         * connections to the external storage and perform any needed cleanup.
         */
    }

    @Override
    public boolean contains(Object key) {
        /*
         * This method will be invoked by the PersistenceManager to determine if the loader contains the specified key.
         * The implementation should be as fast as possible, e.g. it should strive to transfer the least amount of data possible
         * from the external storage to perform the check. Also, if possible, make sure the field is indexed on the external storage
         * so that its existence can be determined as quickly as possible.
         * 
         * Note that keys will be in the cache's native format, which means that if the cache is being used by a remoting protocol
         * such as HotRod or REST and compatibility mode has not been enabled, then they will be encoded in a byte[]. 
         */
        return false;
    }

    

    @Override
    public MarshalledEntry<K, V> load(Object key) {
       /*
        * Fetches an entry from the storage using the specified key. The CacheLoader should retrieve from the external storage all
        * of the data that is needed to reconstruct the entry in memory, i.e. the value and optionally the metadata. This method
        * needs to return a MarshalledEntry which can be constructed as follows:
        * 
        * ctx.getMarshalledEntryFactory().newMarshalledEntry(key, value, metadata);
        *
        * If the entry does not exist or has expired, this method should return null.
        * If an error occurs while retrieving data from the external storage, this method should throw a PersistenceException
        * 
        * 

        * 
        * Note that keys and values will be in the cache's native format, which means that if the cache is being used by a remoting protocol
        * such as HotRod or REST and compatibility mode has not been enabled, then they will be encoded in a byte[].
        * If the loader needs to have knowledge of the key/value data beyond their binary representation, then it needs access to the key's and value's
        * classes and the marshaller used to encode them.
        */
        return null;
    }

}
