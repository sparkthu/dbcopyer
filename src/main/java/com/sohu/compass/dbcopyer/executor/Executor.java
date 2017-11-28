package com.sohu.compass.dbcopyer.executor;

import com.sohu.compass.dbcopyer.clickhouse.ClickHouseImpl;
import com.sohu.compass.dbcopyer.console.ConsoleImpl;
import com.sohu.compass.dbcopyer.db.IDB;
import com.sohu.compass.dbcopyer.exceptions.DBException;
import com.sohu.compass.dbcopyer.model.*;
import com.sohu.compass.dbcopyer.mysql.MysqlImpl;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sparkchen on 2017/11/27.
 */
public class Executor {
    private Logger logger = LoggerFactory.getLogger(Executor.class);
    List<DB> dbList;
    Map<String, Script> scriptMap = new HashMap<>();
    Map<String, IDB> dbMap = new HashMap<>();
    public void run(ConfModel confModel, String taskName) {
        dbList = confModel.getDatabases();
        for (Task task: confModel.getTasks()) {
            if (task.getId().equals(taskName) || taskName == null || taskName.isEmpty()) {
                runOne(task);
            }
        }

    }
    private void runOne(Task task) {
        if (task.getSource().isEmpty() || task.getDest().isEmpty()) {
            logger.error("Task:{} does not have source or dest", task.getId());
            return;
        }
        IDB source = null, dest = null;
        for (DB db: dbList) {
            if (db.getId().equals(task.getSource())) {
                source = getDB(db);
            }
            if (db.getId().equals(task.getDest())) {
                dest = getDB(db);
            }
        }
        if (source == null || dest == null) {
            logger.error("Task:{} source or dest db cannot be initialized", task.getId());
            return;
        }
        String querySql = task.getSourceSql();
        // querySql = Template.replace(querySql);
        logger.info("Prepared SQL:{}", querySql);
        try {
            ResultSet resultSet = source.execQuery(querySql);
            int count = 0;
            StringBuilder sb = new StringBuilder();

            while(resultSet.next()) {
                if (count != 0) {
                    sb.append(dest.getSplitter().betweenLine);
                }
                sb.append(getValues(task, resultSet, dest.getSplitter()));
                count ++;
                if (count == task.getBatchSize()) {
                    logger.info("Insert content generated:{}", sb.toString());
                    dest.insert(task, sb.toString());
                    sb = new StringBuilder();
                    count = 0;
                }
            }
            logger.info("Insert content generated:{}, now call dest.insert", sb.toString());
            dest.insert(task, sb.toString());
            resultSet.close();

            logger.info("Finish read resultSet, count={}", count);
        } catch (Exception e) {
            logger.error("ERROR when executing task,Exception:{}", e);
        }
    }


    Script getScript(String groovy) {
        if (scriptMap.containsKey(groovy)) {
            return scriptMap.get(groovy);
        }
        GroovyShell groovyShell = new GroovyShell();
        scriptMap.put(groovy, groovyShell.parse(groovy));
        return scriptMap.get(groovy);
    }
    private String getValues(Task task, ResultSet resultSet, Splitter splitter) throws SQLException {
        StringBuilder sb = new StringBuilder(splitter.beforeLine);
        for (int index = 0; index < task.getColumns().size(); index++) {
            Column column = task.getColumns().get(index);
            int resultIndex = column.getIndexInResult();
            if (resultIndex <= 0) {
                resultIndex = index+1;
            }
            String columnStringValue;
            columnStringValue = resultSet.getString(resultIndex);
            if (column.isString()) {
                sb.append(splitter.wrapString);
            }
            if (column.getGroovy().trim().isEmpty()) {
                sb.append(columnStringValue);
            } else {
                String shell =  column.getGroovy() ;
                String out = (String)(getScript(shell).invokeMethod("convert", columnStringValue));
                sb.append(out);
            }
            if (column.isString()) {
                sb.append(splitter.wrapString);
            }
            if (index != task.getColumns().size() - 1) {
                sb.append(splitter.betweenValue);
            }
        }
        sb.append(splitter.afterLine);
        return sb.toString();
    }

    private IDB getDB(DB db) {
        IDB executor = null;
        if (dbMap.containsKey(db.getId())) {
            return dbMap.get(db.getId());
        }
        try {
            if (db.getType() == DBType.clickhouse) {
                executor = new ClickHouseImpl();
                executor.connect(db);
            } else if (db.getType() == DBType.mysql) {
                executor = new MysqlImpl();
                executor.connect(db);
            } else if (db.getType() == DBType.console) {
                executor = new ConsoleImpl();
                executor.connect(db);
            } else if (db.getType() == DBType.file) {
                executor = new ConsoleImpl();
                executor.connect(db);
            }
            dbMap.put(db.getId(), executor);
        } catch (DBException e) {
            logger.error("Failed to getDB:{}, exception:{}", db.getId(), e);
            return null;
        }
        return executor;

    }
}
