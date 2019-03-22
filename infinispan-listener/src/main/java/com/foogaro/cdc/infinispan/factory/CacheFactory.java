package com.foogaro.cdc.infinispan.factory;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.annotation.ManagedBean;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ManagedBean
public class CacheFactory {

    public static final String TOPIC_USER_CACHE_NAME = System.getProperty("topic.user.cache.name","default");
    public static final String VDB_USER_CACHE_NAME = System.getProperty("vdb.user.cache.name","vdbUser");

    @Inject
    private EmbeddedCacheManager embeddedCacheManager;

    @Produces
    @QCache(CacheType.TOPIC_USER)
    public Cache getTopicUserCache() {
        return embeddedCacheManager.getCache(TOPIC_USER_CACHE_NAME);
    }

    @Produces
    @QCache(CacheType.VDB_USER)
    public Cache getVDBUserCache() {
        return embeddedCacheManager.getCache(VDB_USER_CACHE_NAME);
    }

}
