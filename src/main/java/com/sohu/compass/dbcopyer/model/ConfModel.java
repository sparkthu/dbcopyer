package com.sohu.compass.dbcopyer.model;

import java.util.List;

/**
 * Created by sparkchen on 2017/11/27.
 */
public class ConfModel {
    private boolean debug;
    private List<Task> tasks;
    private List<DB> databases;

    public void Merge(ConfModel other) {
        if (other == null) {
            return;
        }
        this.debug = other.debug;
        if (this.tasks == null) {
            this.tasks = other.tasks;
        } else {
            this.tasks.addAll(other.tasks);
        }
        if (this.getDatabases() == null) {
            this.databases = other.databases;
        } else {
            this.databases.addAll(other.databases);
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<DB> getDatabases() {
        return databases;
    }

    public void setDatabases(List<DB> databases) {
        this.databases = databases;
    }

    @Override
    public String toString() {
        return "ConfModel{" +
            "debug=" + debug +
            ", tasks=" + tasks +
            ", databases=" + databases +
            '}';
    }
}

