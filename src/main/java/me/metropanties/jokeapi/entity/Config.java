package me.metropanties.jokeapi.entity;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("unused")
public class Config implements Serializable {

    private int port;
    private Map<String, Object> database;

    public Config() { }

    public Config(int port, Map<String, Object> database) {
        this.port = port;
        this.database = database;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDatabase(Map<String, Object> database) {
        this.database = database;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return database.get("host") != null ? (String) database.get("host") : "localhost";
    }

    public String getUsername() {
        return database.get("username") != null ? (String) database.get("username") : "postgres";
    }

    public String getPassword() {
        return database.get("password") != null ? (String) database.get("password") : "";
    }

    public String getDatabase() {
        return database.get("database") != null ? (String) database.get("database") : "postgres";
    }

    public int getDatabasePort() {
        return database.get("port") != null ? (int) database.get("port") : 5432;
    }

}
