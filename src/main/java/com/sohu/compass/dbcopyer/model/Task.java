package com.sohu.compass.dbcopyer.model;

import java.util.List;

/**
 * Created by sparkchen on 2017/11/27.
 */
public class Task {
    private String id;
    private String source;
    private String dest;
    private String sourceSql;
    private int batchSize = 1000;
    private String destTable = "";
    private List<Column> columns = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSourceSql() {
        return sourceSql;
    }

    public void setSourceSql(String sourceSql) {
        this.sourceSql = sourceSql;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getDestTable() {
        return destTable;
    }

    public void setDestTable(String destTable) {
        this.destTable = destTable;
    }

    @Override
    public String toString() {
        return "Task{" +
            "id='" + id + '\'' +
            ", source='" + source + '\'' +
            ", dest='" + dest + '\'' +
            ", sourceSql='" + sourceSql + '\'' +
            ", batchSize=" + batchSize +
            ", destTable='" + destTable + '\'' +
            ", columns=" + columns +
            '}';
    }
}
