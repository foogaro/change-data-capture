package com.foogaro.cdc.infinispan.store;

import com.foogaro.cdc.infinispan.listener.TopicUsersListener;
import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.commons.io.ByteBufferImpl;
import org.infinispan.commons.persistence.Store;
import org.infinispan.marshall.core.MarshalledEntry;
import org.infinispan.marshall.core.MarshalledEntryImpl;
import org.infinispan.metadata.InternalMetadata;
import org.infinispan.persistence.spi.AdvancedLoadWriteStore;
import org.infinispan.persistence.spi.CacheLoader;
import org.infinispan.persistence.spi.CacheWriter;
import org.infinispan.persistence.spi.InitializationContext;

import java.util.concurrent.Executor;

@Store
public class TopicCacheStore implements CacheLoader, CacheWriter {

    protected InitializationContext ctx;

/*
    @Override
    public int size() {
        System.out.println("Size: " + ctx.getCache().size());
        return ctx.getCache().size();
    }

    @Override
    public void clear() {
        System.out.println("Clearing...");
        ctx.getCache().clear();
    }

    @Override
    public void purge(Executor executor, PurgeListener purgeListener) {

    }
*/

    @Override
    public void init(InitializationContext initializationContext) {
        this.ctx = initializationContext;
        ctx.getCache().addListener(new TopicUsersListener(ctx.getCache().getCacheManager()));
        System.out.println("TopicUsersListener added to its cache " + ctx.getCache().getName());
    }

    @Override
    public void write(MarshalledEntry marshalledEntry) {
        System.out.println("Writing... " + marshalledEntry);
    }

    @Override
    public boolean delete(Object o) {
        System.out.println("Deleting... " + o);
        return ctx.getCache().remove(o) != null;
    }

    @Override
    public void deleteBatch(Iterable keys) {
        System.out.println("Deleting batch on keys: " + keys);
    }

    @Override
    public void writeBatch(Iterable iterable) {
        System.out.println("Writing batch on: " + iterable);
    }

    @Override
    public MarshalledEntry load(Object o) {
        System.out.println("Loading... " + o);
        String s = (String)ctx.getCache().get(o);
        System.out.println("S: " + s);
        MarshalledEntry<String,String> marshalledEntry = new MarshalledEntry<String, String>() {
            @Override
            public ByteBuffer getKeyBytes() {
                return new ByteBufferImpl(((String)o).getBytes());
            }

            @Override
            public ByteBuffer getValueBytes() {
                return new ByteBufferImpl(s.getBytes());
            }

            @Override
            public ByteBuffer getMetadataBytes() {
                return null;
            }

            @Override
            public String getKey() {
                return (String)o;
            }

            @Override
            public String getValue() {
                return s;
            }

            @Override
            public InternalMetadata getMetadata() {
                return null;
            }
        };
        System.out.println("marshalledEntry: " + marshalledEntry);
        return marshalledEntry;
    }

    @Override
    public boolean contains(Object o) {
        System.out.println("Contains... " + o);
        return ctx.getCache().containsKey(o);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void start() {
        System.out.println("TopicCacheStore started.");
    }

    @Override
    public void stop() {
        System.out.println("TopicCacheStore stopped.");
    }

}
