package com.foogaro.cdc.infinispan;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryModified;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.event.ClientEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InfinispanKafkaRunner {

    public static void main(String[] args) {
        ConfigurationBuilder builder = new ConfigurationBuilder().addServer().host("localhost").port(11222).maxRetries(5);

        RemoteCacheManager rcm = new RemoteCacheManager(builder.build());
        RemoteCache rc = rcm.getCache("topicUsers");
        rc.addClientListener(new ClientTopicUsersListener(rcm));

        while (true) {}
    }

    @ClientListener
    public static class ClientTopicUsersListener {

        public static final String TOPIC_USER_CACHE_NAME = System.getProperty("topic.user.cache.name","topicUsers");
        public static final String VDB_USER_CACHE_NAME = System.getProperty("vdb.user.cache.name","vdbUsers");
        private RemoteCacheManager remoteCacheManager;
        private RemoteCache sourceCache;
        private RemoteCache targetCache;
        private Caronte caronte;

        public ClientTopicUsersListener(RemoteCacheManager remoteCacheManager) {
            this.remoteCacheManager = remoteCacheManager;
            this.sourceCache = this.remoteCacheManager.getCache(TOPIC_USER_CACHE_NAME);
            this.targetCache = this.remoteCacheManager.getCache(VDB_USER_CACHE_NAME);
            this.caronte = new Caronte();
        }

        protected ExecutorService executor = Executors.newSingleThreadExecutor();


        protected void processClientEvent(final ClientEvent ce) {
            this.executor.submit(new Runnable() {
                @Override public void run() {
                    transform(ce);
                }
            });
        }

        @ClientCacheEntryModified
        @ClientCacheEntryCreated
        public void processClientEntry(ClientEvent event) {
            System.out.printf("processClientEntry - Event %s%n", event);
            System.out.println("processClientEntry - sourceCache: " + sourceCache);
            System.out.println("processClientEntry - targetCache: " + targetCache);
            System.out.println("processClientEntry - caronte: " + caronte);
            processClientEvent(event);
        }

        public void transform(ClientEvent event) {
            System.out.printf("transform - ClientEvent %s%n", event);
            System.out.printf("transform - ClientEvent.getType %s%n", event.getType());
            System.out.println("sourceCache: " + sourceCache);
            System.out.println("targetCache: " + targetCache);
            System.out.println("caronte: " + caronte);
            switch (event.getType()) {
                case CLIENT_CACHE_ENTRY_CREATED:
                    System.out.println("CLIENT_CACHE_ENTRY_CREATED");
                    //caronte.ship(new VDBUserTransformer().transform((String)sourceCache.get(((ClientCacheEntryCreatedEvent)event).getKey())), targetCache);
                    break;
                case CLIENT_CACHE_ENTRY_MODIFIED:
                    System.out.println("CLIENT_CACHE_ENTRY_MODIFIED");
                    //caronte.ship(new VDBUserTransformer().transform((String)sourceCache.get(((ClientCacheEntryModifiedEvent)event).getKey())), targetCache);
                    break;
            }
            System.out.println("transform - DONE");
        }

    }

}
