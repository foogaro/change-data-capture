package org.infinispan.archetypes; 

import java.util.concurrent.Executor;

import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.commons.persistence.Store;
import org.infinispan.marshall.core.MarshalledEntry;
import org.infinispan.persistence.spi.AdvancedCacheWriter;
import org.infinispan.persistence.spi.InitializationContext;
import org.kohsuke.MetaInfServices;

@Store
@MetaInfServices
@ConfiguredBy(CustomStoreConfiguration.class)
public class CustomAdvancedCacheWriter<K,V> implements AdvancedCacheWriter<K, V> {
   @Override
   public void init(InitializationContext ctx) {
       /*
        * This method will be invoked by the PersistenceManager during initialization. The InitializationContext
        * contains:
        * - this CacheWriter's configuration
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
        * This method will be invoked by the PersistenceManager to start the CacheWriter. At this stage configuration
        * is complete and the loader can perform operations such as opening a connection to the external storage,
        * initialize internal data structures, etc.
        */
   }

   @Override
   public void stop() {
       /*
        * This method will be invoked by the PersistenceManager to stop the CacheWriter. The CacheWriter should close any
        * connections to the external storage and perform any needed cleanup.
        */
   }

    @Override
    public boolean delete(Object key) {
        /*
         * The CacheWriter should remove from the external storage the entry identified by the specified key.
         * 
         * Note that keys will be in the cache's native format, which means that if the cache is being used by a remoting protocol
         * such as HotRod or REST and compatibility mode has not been enabled, then they will be encoded in a byte[]. 
         */
        return false;
    }

    @Override
    public void write(MarshalledEntry<? extends K, ? extends V> marshalledEntry) {
        /*
         * The CacheWriter should write the specified entry to the external storage. 
         * 
         * The PersistenceManager uses MarshalledEntry as the default format so that CacheWriters can efficiently store data coming 
         * from a remote node, thus avoiding any additional transformation steps.
         * 
         * Note that keys and values will be in the cache's native format, which means that if the cache is being used by a remoting protocol
         * such as HotRod or REST and compatibility mode has not been enabled, then they will be encoded in a byte[].
         */
    }

    @Override
    public void clear() {
        /*
         * The CacheWriter should efficiently remove all entries from the store
         */
    }

    @Override
    public void purge(
            Executor executor,
            org.infinispan.persistence.spi.AdvancedCacheWriter.PurgeListener<? super K> purgeListener) {
        /*
         * Using the thread in the pool, remove all the expired data from the persistence storage. For each removed entry,
         * the supplied listener must be invoked.
         */
    }

}
