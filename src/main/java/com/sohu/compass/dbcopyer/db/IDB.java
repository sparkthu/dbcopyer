package com.sohu.compass.dbcopyer.db;

import com.sohu.compass.dbcopyer.exceptions.DBException;
import com.sohu.compass.dbcopyer.model.Column;
import com.sohu.compass.dbcopyer.model.DB;
import com.sohu.compass.dbcopyer.model.Splitter;
import com.sohu.compass.dbcopyer.model.Task;

import javax.sql.RowSet;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by sparkchen on 2017/11/27.
 */
public interface IDB {

    void connect(DB db) throws DBException;
    ResultSet execQuery(String sql) throws DBException;
    void exec(String sql) throws DBException;
    void insert(Task task, String values) throws DBException;
    Splitter getSplitter();
}
