package com.foogaro.cdc.infinispan.transformer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foogaro.cdc.infinispan.model.VDBUser;

import java.io.IOException;

public class VDBUserTransformer {

    public VDBUser transform(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(jsonString);
            System.out.println("JsonNode: " + jsonNode);
            JsonNode payload = jsonNode.get("payload");
            if (payload == null) return null;
            System.out.println("payload: " + payload);
            JsonNode before = payload.get("before");
            System.out.println("before: " + before);
            JsonNode after = payload.get("after");
            if (after == null) return null;
            System.out.println("after: " + after);
            JsonNode userId = after.get("user_id");
            if (userId == null) return null;
            System.out.println("userId: " + userId);

            VDBUser vdbUser = new VDBUser();
            vdbUser.setUserId("vdb-" + after.get("user_id").asText());
            vdbUser.setName(after.get("name").asText());
            vdbUser.setLastname(after.get("lastname").asText());
            vdbUser.setUsername(after.get("username").asText());
            vdbUser.setEmail(after.get("email").asText());
            System.out.println("VDBUser: " + vdbUser.toString());
            return vdbUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
