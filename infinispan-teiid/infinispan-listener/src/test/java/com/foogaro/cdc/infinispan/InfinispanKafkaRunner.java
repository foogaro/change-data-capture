package com.foogaro.cdc.infinispan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foogaro.cdc.infinispan.model.Protable;
import com.foogaro.cdc.infinispan.model.VDBUser;
import com.foogaro.cdc.infinispan.model.VDBUserMarshaller;
import com.foogaro.cdc.infinispan.model.VDBUserTransformer;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryModified;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.infinispan.client.hotrod.event.ClientCacheEntryModifiedEvent;
import org.infinispan.client.hotrod.event.ClientEvent;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.annotations.ProtoSchemaBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.infinispan.query.remote.client.ProtobufMetadataManagerConstants.*;

public class InfinispanKafkaRunner {

    public static void main(String[] args) {
        ConfigurationBuilder sourceBuilder = new ConfigurationBuilder().addServer().host("localhost").port(11222).maxRetries(50);
        ConfigurationBuilder targetBuilder = new ConfigurationBuilder().addServer().host("localhost").port(11222).marshaller(new ProtoStreamMarshaller());


        RemoteCacheManager sourceRemoteCacheManager = new RemoteCacheManager(sourceBuilder.build());
        RemoteCacheManager targetRemoteCacheManager = new RemoteCacheManager(targetBuilder.build(),true);
        registerSchemasAndMarshallers(targetRemoteCacheManager);
        RemoteCache sourceCache = sourceRemoteCacheManager.getCache("topicUsers");
        sourceCache.addClientListener(new ClientTopicUsersListener(sourceRemoteCacheManager, targetRemoteCacheManager));

        Object o = targetRemoteCacheManager.getCache("vdbUsers").get("vdb-3042");
        System.out.println("oooo: " + o);

        while (true) {}
    }

    private static void registerSchemasAndMarshallers(RemoteCacheManager remoteCacheManager) {
        SerializationContext serCtx = ProtoStreamMarshaller.getSerializationContext(remoteCacheManager);
        ProtoSchemaBuilder protoSchemaBuilder = new ProtoSchemaBuilder();
        String memoSchemaFile = null;
        Class<?> clazz = VDBUser.class;
        System.out.println("clazz: " + clazz);
        String typeName = clazz.getTypeName();
        System.out.println("typeName: " + typeName);
        String packageName = clazz.getPackage().getName();
        System.out.println("packageName: " + packageName);
        String filename = clazz.getSimpleName() + PROTO_KEY_SUFFIX;
        System.out.println("filename: " + filename);
        try {
            //memoSchemaFile = protoSchemaBuilder.fileName("file.proto").packageName("test").addClass(marshaller)
            memoSchemaFile = protoSchemaBuilder.fileName(filename).packageName(packageName).addClass(clazz).build(serCtx);
            //memoSchemaFile = protoSchemaBuilder.fileName(filename).addClass(clazz).build(serCtx);
            System.out.println("memoSchemaFile: " + memoSchemaFile);
            serCtx.registerProtoFiles(FileDescriptorSource.fromString(filename,memoSchemaFile));
            serCtx.registerMarshaller(new VDBUserMarshaller());
        } catch (Exception e) {
            System.err.println("Error during building of Protostream Schema: " + e.getMessage());
            e.printStackTrace();
        }

        RemoteCache<String, String> metadataCache = remoteCacheManager.getCache(PROTOBUF_METADATA_CACHE_NAME);
        metadataCache.put(filename, memoSchemaFile);

        String filesWithErrors = metadataCache.get(ERRORS_KEY_SUFFIX);
        if (filesWithErrors != null) {
            System.err.println("Error in proto file(s): " + filesWithErrors);
            //throw new AssertionError("Error in proto file(s): " + filesWithErrors);
        } else {
            System.out.println("Added schema file: " + filename);
        }

        /*
        // Register entity marshallers on the client side ProtoStreamMarshaller instance associated with the remote cache manager.
        SerializationContext ctx = ProtoStreamMarshaller.getSerializationContext(cacheManager);
        ctx.registerProtoFiles(FileDescriptorSource.fromResources(PROTOBUF_DEFINITION_RESOURCE));
        ctx.registerMarshaller(new TaskMarshaller());

        // register the schemas with the server too
        RemoteCache<String, String> metadataCache = cacheManager.getCache(ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME);
        //Actually register the proto file
        try (Scanner s = new Scanner(Config.class.getResourceAsStream(PROTOBUF_DEFINITION_RESOURCE), "UTF-8")) {
            String text = s.useDelimiter("\\A").next();
            log.log(Level.INFO, "Registering proto file:\n" + text);
            try {
                metadataCache.put("task.proto", text);
            } catch (Exception e) {
                log.log(Level.SEVERE, "Error registering proto file");
            }

            String errors = metadataCache.get(ProtobufMetadataManagerConstants.ERRORS_KEY_SUFFIX);

            if (errors != null) {
                throw new IllegalStateException("Some Protobuf schema files contain errors:\n" + errors);
            }
        }
        */

    }

    @ClientListener
    public static class ClientTopicUsersListener {

        public static final String TOPIC_USER_CACHE_NAME = System.getProperty("topic.user.cache.name","topicUsers");
        public static final String VDB_USER_CACHE_NAME = System.getProperty("vdb.user.cache.name","vdbUsers");
        private RemoteCacheManager sourceRemoteCacheManager;
        private RemoteCacheManager targetRemoteCacheManager;
        private RemoteCache sourceCache;
        //private RemoteCache basicTargetCache;
        //private AdvancedCache targetCache;
        private RemoteCache targetCache;
        private Caronte caronte;

        public ClientTopicUsersListener(RemoteCacheManager sourceRemoteCacheManager, RemoteCacheManager targetRemoteCacheManager) {
            this.sourceRemoteCacheManager = sourceRemoteCacheManager;
            this.targetRemoteCacheManager = targetRemoteCacheManager;
            this.sourceCache = this.sourceRemoteCacheManager.getCache(TOPIC_USER_CACHE_NAME);
            this.targetCache = this.targetRemoteCacheManager.getCache(VDB_USER_CACHE_NAME); //((AdvancedCache)basicTargetCache.).withEncoding(IdentityEncoder.class);
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
            Protable protable;
            switch (event.getType()) {
                case CLIENT_CACHE_ENTRY_CREATED:
                    System.out.println("CLIENT_CACHE_ENTRY_CREATED");
                    ClientCacheEntryCreatedEvent realEvent = (ClientCacheEntryCreatedEvent)event;
                    String jsonKey = (String)realEvent.getKey();
                    JsonNode node = null;
                    try {
                        node = new ObjectMapper().readTree(jsonKey).get("payload").get("user_id");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String realVal = (String)sourceCache.get(jsonKey);
                    protable = new VDBUserTransformer().transform(realVal);
                    System.out.println("Protable: " + protable);
                    targetCache.put(protable.getKey(),protable);
                    System.out.println("Stored into cache");
                    break;
                case CLIENT_CACHE_ENTRY_MODIFIED:
                    System.out.println("CLIENT_CACHE_ENTRY_MODIFIED");
                    protable = new VDBUserTransformer().transform((String)sourceCache.get(((ClientCacheEntryModifiedEvent)event).getKey()));
                    System.out.println("Protable: " + protable);
                    targetCache.put(protable.getKey(),protable);
                    System.out.println("Stored into cache");
                    break;
            }
            System.out.println("transform - DONE");
        }

    }

}
