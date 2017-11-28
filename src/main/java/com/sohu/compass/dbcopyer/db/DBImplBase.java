package com.sohu.compass.dbcopyer.db;

import com.sohu.compass.dbcopyer.exceptions.DBException;
import com.sohu.compass.dbcopyer.model.Splitter;
import com.sohu.compass.dbcopyer.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by sparkchen on 2017/11/27.
 */
public abstract class DBImplBase implements IDB {
    protected DataSource dataSource= null;
    private Logger logger = LoggerFactory.getLogger(DBImplBase.class);
    protected String id = "";
    protected Splitter splitter = new Splitter();
    @Override
    public ResultSet execQuery(String sql) throws DBException {
        try {
            Connection connection = dataSource.getConnection();
            if (connection == null || !connection.isValid(10)) {
                throw new DBException("Can not get valid connection to DB " + id);
            }
            Statement statement = connection.createStatement();
            logger.info("Prepare to query sql {}", sql);
            return statement.executeQuery(sql);
        } catch(SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void exec(String sql) throws DBException {
        try {
            Connection connection = dataSource.getConnection();
            if (connection == null || !connection.isValid(10)) {
                throw new DBException("Can not get valid connection to DB " + id);
            }
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch(SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void insert(Task task, String values) throws DBException {
        if (task.getDestTable().isEmpty()) {
            throw new DBException("DestTable is needed for task " + task.getId());
        }
        StringBuilder sb = getInsertSqlHead(task);
        sb.append(values);
        logger.info("DBImpl prepare to exec sql: {}", sb.toString());
        this.exec(sb.toString());
    }

    @Override
    public Splitter getSplitter() {
        return splitter;
    }

    protected StringBuilder getInsertSqlHead(Task task) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(task.getDestTable());
        sb.append(" (");
        for (int i = 0; i < task.getColumns().size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(task.getColumns().get(i).getName());
        }
        sb.append(") VALUES ");
        return sb;
    }
}
