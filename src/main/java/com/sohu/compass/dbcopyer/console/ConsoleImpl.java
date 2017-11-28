package com.sohu.compass.dbcopyer.console;

import com.sohu.compass.dbcopyer.db.IDB;
import com.sohu.compass.dbcopyer.exceptions.DBException;
import com.sohu.compass.dbcopyer.exceptions.NotSupportedException;
import com.sohu.compass.dbcopyer.model.DB;
import com.sohu.compass.dbcopyer.model.Splitter;
import com.sohu.compass.dbcopyer.model.Task;

import java.sql.ResultSet;

/**
 * Created by sparkchen on 2017/11/27.
 */
public class ConsoleImpl implements IDB {
    DB db;
    Splitter splitter = null;
    @Override
    public void connect(DB db) throws DBException {
        this.db = db;
        if (this.db.getFormat().equals("csv")) {
            splitter = new Splitter(",", "\n","", "", "");
        } else if (this.db.getFormat().equals("tsv")) {
            splitter = new Splitter("\t", "\n","", "", "");
        } else if (this.db.getFormat().equals("sql")) {
            splitter = new Splitter();
        }

    }

    @Override
    public ResultSet execQuery(String sql) throws DBException {
        throw new NotSupportedException("Console does not support query");
    }

    @Override
    public void exec(String sql) throws DBException {
        throw new NotSupportedException("Console does not support exec");
    }

    @Override
    public void insert(Task task, String values) throws DBException {
        System.out.println(values);
    }

    @Override
    public Splitter getSplitter() {
        return this.splitter;
    }
}
