package com.sohu.compass.dbcopyer.model;

/**
 * Created by sparkchen on 2017/11/27.
 */
public class DB {
    private String id = "";
    private String url = "";
    private DBType type = DBType.console;
    private String username = "";
    private String password = "";
    private String filepath = "";
    private String format = "tsv";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DBType getType() {
        return type;
    }

    public void setType(DBType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "DB{" +
            "id='" + id + '\'' +
            ", url='" + url + '\'' +
            ", type='" + type + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", filepath='" + filepath + '\'' +
            ", format='" + format + '\'' +
            '}';
    }
}
