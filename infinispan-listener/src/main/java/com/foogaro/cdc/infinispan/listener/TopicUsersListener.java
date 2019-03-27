package com.foogaro.cdc.infinispan.listener;

import com.foogaro.cdc.infinispan.Caronte;
import com.foogaro.cdc.infinispan.transformer.VDBUserTransformer;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.Event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Listener
public class TopicUsersListener {

    public static final String VDB_USER_CACHE_NAME = System.getProperty("vdb.user.cache.name","vdbUsers");

    private Caronte caronte;
    private Cache targetCache;

    protected ExecutorService executor = Executors.newSingleThreadExecutor();

    public TopicUsersListener(EmbeddedCacheManager embeddedCacheManager) {
        this.caronte = new Caronte();
        this.targetCache = embeddedCacheManager.getCache(VDB_USER_CACHE_NAME);
    }

    @CacheEntryModified
    @CacheEntryCreated
    public void processEntry(Event event) {
        System.out.printf("processEntry - Event %s%n", event);
        System.out.println("processEntry - targetCache: " + targetCache);
        System.out.println("processEntry - caronte: " + caronte);
        processEvent(event);
    }

    public void transform(Event event) {
        System.out.printf("transform - ClientEvent %s%n", event);
        System.out.printf("transform - ClientEvent.getType %s%n", event.getType());
        System.out.println("targetCache: " + targetCache);
        System.out.println("caronte: " + caronte);
        switch (event.getType()) {
            case CACHE_ENTRY_CREATED:
                System.out.println("CLIENT_CACHE_ENTRY_CREATED");
                caronte.ship(new VDBUserTransformer().transform((String)((CacheEntryCreatedEvent)event).getValue()), targetCache);
                break;
            case CACHE_ENTRY_MODIFIED:
                System.out.println("CLIENT_CACHE_ENTRY_MODIFIED");
                caronte.ship(new VDBUserTransformer().transform((String)((CacheEntryModifiedEvent)event).getValue()), targetCache);
                break;
        }
        System.out.println("transform - DONE");
    }

    protected void processEvent(final Event e) {
        this.executor.submit(() -> transform(e));
    }

}
