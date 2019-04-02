package com.foogaro.cdc.infinispan.model;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoMessage;

@ProtoDoc("@Indexed")
@ProtoMessage(name = "VDBUser")
public class VDBUser implements Protable {

    private String userId;
    private String name;
    private String lastname;
    private String username;
    private String email;

    public VDBUser() {
    }

    public VDBUser(String userId, String name, String lastname, String username, String email) {
        this.userId = userId;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
    }

    @ProtoDoc("@Id")
    @ProtoField(number = 1, required = true)
    public String getUserId() {
        return userId;
    }

    @ProtoDoc("@Field")
    @ProtoField(number = 2, required = true)
    public String getName() {
        return name;
    }

    @ProtoDoc("@Field")
    @ProtoField(number = 3, required = true)
    public String getLastname() {
        return lastname;
    }

    @ProtoDoc("@Field")
    @ProtoField(number = 4, required = true)
    public String getUsername() {
        return username;
    }

    @ProtoDoc("@Field")
    @ProtoField(number = 5, required = true)
    public String getEmail() {
        return email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "VDBUser{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public String getKey() {
        return getUserId();
    }
}
