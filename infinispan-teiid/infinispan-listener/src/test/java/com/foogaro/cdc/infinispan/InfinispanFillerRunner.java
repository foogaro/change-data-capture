package com.foogaro.cdc.infinispan;

import com.foogaro.cdc.infinispan.model.VDBUser;
import com.github.javafaker.Faker;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryModified;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.infinispan.client.hotrod.event.ClientCacheEntryModifiedEvent;
import org.infinispan.client.hotrod.event.ClientEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InfinispanFillerRunner {

    public static void main(String[] args) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().addServer().host("localhost").port(11222).maxRetries(50);


        RemoteCacheManager remoteCacheManager = new RemoteCacheManager(configurationBuilder.build(), true);
        RemoteCache topicCache = remoteCacheManager.getCache("topicUsers");
        topicCache.addClientListener(new ClientTopicUsersListener(remoteCacheManager));

        Faker faker = new Faker();
        VDBUser vdbUser;

        long id = 1;
        while (id<1000) {

            vdbUser = new VDBUser();
            vdbUser.setUserId(""+id++);
            vdbUser.setName(faker.name().firstName());
            vdbUser.setLastname(faker.name().lastName());
            vdbUser.setUsername(faker.name().username());
            vdbUser.setEmail(faker.internet().emailAddress());
            topicCache.put(vdbUser.getKey(),vdbUser);

            try {
                Thread.sleep(faker.number().numberBetween(0l,1000l));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @ClientListener
    public static class ClientTopicUsersListener {

        public static final String TOPIC_USER_CACHE_NAME = System.getProperty("topic.user.cache.name","topicUsers");
        public static final String VDB_USER_CACHE_NAME = System.getProperty("vdb.user.cache.name","vdbUsers");
        private RemoteCacheManager remoteCacheManager;
        private RemoteCache sourceCache;
        private RemoteCache targetCache;

        public ClientTopicUsersListener(RemoteCacheManager remoteCacheManager) {
            this.remoteCacheManager = remoteCacheManager;
            this.sourceCache = this.remoteCacheManager.getCache(TOPIC_USER_CACHE_NAME);
            this.targetCache = this.remoteCacheManager.getCache(VDB_USER_CACHE_NAME);
        }

        protected ExecutorService executor = Executors.newSingleThreadExecutor();


        protected void processClientEvent(final ClientEvent ce) {
            this.executor.submit(() -> transform(ce));
        }

        @ClientCacheEntryModified
        @ClientCacheEntryCreated
        public void processClientEntry(ClientEvent event) {
            System.out.printf("processClientEntry - Event %s%n", event);
            System.out.println("processClientEntry - sourceCache: " + sourceCache);
            System.out.println("processClientEntry - targetCache: " + targetCache);
            processClientEvent(event);
        }

        public void transform(ClientEvent event) {
            System.out.printf("transform - ClientEvent %s%n", event);
            System.out.printf("transform - ClientEvent.getType %s%n", event.getType());
            System.out.println("sourceCache: " + sourceCache);
            System.out.println("targetCache: " + targetCache);

            String key;
            VDBUser vdbUser;
            switch (event.getType()) {
                case CLIENT_CACHE_ENTRY_CREATED:
                    System.out.println("CLIENT_CACHE_ENTRY_CREATED");
                    ClientCacheEntryCreatedEvent clientCacheEntryCreatedEvent = (ClientCacheEntryCreatedEvent)event;
                    key = (String)clientCacheEntryCreatedEvent.getKey();
                    vdbUser = (VDBUser) sourceCache.get(key);
                    System.out.println("VDBUser: " + vdbUser);
                    vdbUser.setUserId("vdb-" + vdbUser.getUserId());
                    System.out.println("VDBUser: " + vdbUser);
                    targetCache.put(vdbUser.getKey(), vdbUser);
                    System.out.println("Stored into cache");
                    break;
                case CLIENT_CACHE_ENTRY_MODIFIED:
                    System.out.println("CLIENT_CACHE_ENTRY_MODIFIED");
                    ClientCacheEntryModifiedEvent clientCacheEntryModifiedEvent = (ClientCacheEntryModifiedEvent)event;
                    key = (String)clientCacheEntryModifiedEvent.getKey();
                    vdbUser = (VDBUser) sourceCache.get(key);
                    System.out.println("VDBUser: " + vdbUser);
                    vdbUser.setUserId("vdb-" + vdbUser.getUserId());
                    System.out.println("VDBUser: " + vdbUser);
                    targetCache.put(vdbUser.getKey(), vdbUser);
                    System.out.println("Stored into cache");
                    break;
            }
            System.out.println("transform - DONE");
        }

    }

}
