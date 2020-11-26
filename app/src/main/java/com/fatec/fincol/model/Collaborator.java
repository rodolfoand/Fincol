package com.fatec.fincol.model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Collaborator {

    private String user;
    private String account;
    private StatusColl status;
    private TypeColl type;

    public Collaborator(String user, String account) {
        this.user = user;
        this.account = account;
    }

    public Collaborator(String user, String account, StatusColl status, TypeColl type) {
        this.user = user;
        this.account = account;
        this.status = status;
        this.type = type;
    }

    public enum StatusColl {
        CREATED("CREATED"), ACTIVE("ACTIVE"), PENDING("PENDING"), INACTIVE("INACTIVE");

        private String name;

        private static final Map<String,StatusColl> ENUM_MAP;

        StatusColl (String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        // Build an immutable map of String name to enum pairs.
        // Any Map impl can be used.

        static {
            Map<String,StatusColl> map = new ConcurrentHashMap<String, StatusColl>();
            for (StatusColl instance : StatusColl.values()) {
                map.put(instance.getName(),instance);
            }
            ENUM_MAP = Collections.unmodifiableMap(map);
        }

        public static StatusColl get (String name) {
            return ENUM_MAP.get(name);
        }
    }

    public enum TypeColl {
        ADMIN("ADMIN"), VIEWER("VIEWER");


        private String name;

        private static final Map<String,TypeColl> ENUM_MAP;

        TypeColl (String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        // Build an immutable map of String name to enum pairs.
        // Any Map impl can be used.

        static {
            Map<String,TypeColl> map = new ConcurrentHashMap<String, TypeColl>();
            for (TypeColl instance : TypeColl.values()) {
                map.put(instance.getName(),instance);
            }
            ENUM_MAP = Collections.unmodifiableMap(map);
        }

        public static TypeColl get (String name) {
            return ENUM_MAP.get(name);
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public StatusColl getStatus() {
        return status;
    }

    public void setStatus(StatusColl status) {
        this.status = status;
    }

    public TypeColl getType() {
        return type;
    }

    public void setType(TypeColl type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Collaborator{" +
                "user='" + user + '\'' +
                ", account='" + account + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}

