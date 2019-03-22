package com.foogaro.cdc.infinispan;

import com.foogaro.cdc.infinispan.factory.CacheType;
import com.foogaro.cdc.infinispan.factory.QCache;
import com.foogaro.cdc.infinispan.listener.TopicUsersListener;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.annotation.ManagedBean;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@Startup
@ManagedBean
@ApplicationScoped
public class InfinispanKafka {

    public static final String TOPIC_USER_CACHE_NAME = System.getProperty("topic.user.cache.name","topicUsers");
    public static final String VDB_USER_CACHE_NAME = System.getProperty("vdb.user.cache.name","vdbUsers");

    @Inject
    private EmbeddedCacheManager embeddedCacheManager;
    //private RemoteCacheManager remoteCacheManager;

    public InfinispanKafka() {
        System.out.println("Initializing...");
        embeddedCacheManager.getCache(TOPIC_USER_CACHE_NAME).addListener(new TopicUsersListener());
        System.out.println("TopicUsersListener added to cache " + TOPIC_USER_CACHE_NAME);
    }

    @Produces
    @QCache(CacheType.TOPIC_USER)
    public Cache getTopicUserCache() { return getCache(TOPIC_USER_CACHE_NAME); }

    @Produces
    @QCache(CacheType.VDB_USER)
    public Cache getVDBUserCache() { return getCache(VDB_USER_CACHE_NAME); }

    private Cache getCache(String cacheName) { return embeddedCacheManager.getCache(cacheName); }

}
