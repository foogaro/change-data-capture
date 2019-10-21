package org.infinispan.archetypes;

import java.util.concurrent.Executor;

import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.commons.persistence.Store;
import org.infinispan.filter.KeyFilter;
import org.infinispan.marshall.core.MarshalledEntry;
import org.infinispan.persistence.spi.AdvancedCacheLoader;
import org.infinispan.persistence.spi.InitializationContext;
import org.kohsuke.MetaInfServices;

@Store
@MetaInfServices
@ConfiguredBy(CustomStoreConfiguration.class)
public class CustomAdvancedCacheLoader<K,V> implements AdvancedCacheLoader<K, V> {

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

    @Override
    public void process(
            KeyFilter<? super K> filter,
            org.infinispan.persistence.spi.AdvancedCacheLoader.CacheLoaderTask<K, V> task,
            Executor executor, boolean fetchValue, boolean fetchMetadata) {
        /*
         * The PersistenceManager will invoke this method to iterate in parallel over the entries in the storage using
         * the threads from the <b>executor</b> pool. For each entry, invoke the task.processEntry(MarshalledEntry, TaskContext)
         * method. Before passing an entry to the callback task, the entry should be validated against the <b>filter</b>, e.g.:
         *
         * if (filter.accept(key)) {
         *     task.processEntry(MarshalledEntry, TaskContext)
         * }
         *
         * Implementors should build an {@link TaskContext} instance (implementation) that is fed to the {@link
         * CacheLoaderTask} on every invocation. The {@link CacheLoaderTask} might invoke {@link
         * org.infinispan.persistence.spi.AdvancedCacheLoader.TaskContext#stop()} at any time, so implementors of this method
         * should verify TaskContext's state for early termination of iteration. The method should only return once the
         * iteration is complete or as soon as possible in the case TaskContext.stop() is invoked.
         * The parameters are as follows:
         * - filter        to validate which entries should be feed into the task. Might be null.
         * - task          callback to be invoked in parallel for each stored entry that passes the filter check
         * - executor      an external thread pool to be used for parallel iteration
         * - fetchValue    whether or not to fetch the value from the persistent store. E.g. if the iteration is
         *                 intended only over the key set, no point fetching the values from the persistent store as
         *                 well
         * - fetchMetadata whether or not to fetch the metadata from the persistent store. E.g. if the iteration is
         *                 intended only over the key set, then no pint fetching the metadata from the persistent store
         *                 as well
         * This method should throw a PersistenceException in case of an error, e.g. communicating with the external storage
         */
    }

    @Override
    public int size() {
        /*
         * The CacheLoader should efficiently compute the number of entries in the store
         */
        return 0;
    }

}
