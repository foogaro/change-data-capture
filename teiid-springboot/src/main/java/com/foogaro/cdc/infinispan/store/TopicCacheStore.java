package com.foogaro.cdc.infinispan.store;

import com.foogaro.cdc.infinispan.listener.TopicUsersListener;
import org.infinispan.marshall.core.MarshalledEntry;
import org.infinispan.persistence.spi.AdvancedLoadWriteStore;
import org.infinispan.persistence.spi.InitializationContext;

import java.util.concurrent.Executor;

public class TopicCacheStore implements /*CacheLoader, CacheWriter, AdvancedCacheLoader, AdvancedCacheWriter,*/ AdvancedLoadWriteStore {

    protected InitializationContext ctx;

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
    public MarshalledEntry load(Object o) {
        System.out.println("Loading... " + o);
        return null;
    }

    @Override
    public boolean contains(Object o) {
        System.out.println("Contains... " + o);
        return ctx.getCache().containsKey(o);
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
