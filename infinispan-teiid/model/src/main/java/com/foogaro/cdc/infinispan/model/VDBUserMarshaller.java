package com.foogaro.cdc.infinispan.model;

import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public class VDBUserMarshaller implements MessageMarshaller<VDBUser> {

    @Override
    public VDBUser readFrom(ProtoStreamReader reader) throws IOException {
        return new VDBUser(reader.readString("userId"),
                reader.readString("name"),
                reader.readString("lastname"),
                reader.readString("username"),
                reader.readString("email"));
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, VDBUser vdbUser) throws IOException {
        writer.writeString("userId", vdbUser.getUserId());
        writer.writeString("name", vdbUser.getName());
        writer.writeString("lastname", vdbUser.getLastname());
        writer.writeString("username", vdbUser.getUsername());
        writer.writeString("email", vdbUser.getEmail());
    }

    @Override
    public Class<? extends VDBUser> getJavaClass() {
        return VDBUser.class;
    }

    @Override
    public String getTypeName() {
        return VDBUser.class.getTypeName();
    }
}
