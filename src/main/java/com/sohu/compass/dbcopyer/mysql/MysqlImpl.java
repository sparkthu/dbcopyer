package com.sohu.compass.dbcopyer.mysql;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.sohu.compass.dbcopyer.db.DBImplBase;
import com.sohu.compass.dbcopyer.exceptions.DBException;
import com.sohu.compass.dbcopyer.model.DB;
import com.sohu.compass.dbcopyer.model.Task;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by sparkchen on 2017/11/27.
 */
public class MysqlImpl extends DBImplBase {
    @Override
    public void connect(DB db) throws DBException {
        if (db.getUrl().isEmpty()) {
            throw new DBException("URL is needed!");
        }
        if (!db.getUrl().startsWith("jdbc:mysql://")) {
            throw new DBException("URL should has format: jdbc:mysql//ip1:port1/dbname");
        }
        this.id = db.getId();

        try {
            dataSource = DruidDataSourceFactory.createDataSource(getMysqlProperties(db));
            dataSource.getConnection().close();  // test connection
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException(e);
        }

    }
    Properties getMysqlProperties(DB db) {
        Properties properties = new Properties();
        properties.setProperty("name", db.getId());
        properties.setProperty("url", db.getUrl());
        properties.setProperty("username", db.getUsername());
        properties.setProperty("password", db.getPassword());
        return properties;
    }

}
