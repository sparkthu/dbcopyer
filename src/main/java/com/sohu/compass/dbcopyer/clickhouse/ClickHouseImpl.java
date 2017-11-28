package com.sohu.compass.dbcopyer.clickhouse;

import com.sohu.compass.dbcopyer.db.DBImplBase;
import com.sohu.compass.dbcopyer.exceptions.DBException;
import com.sohu.compass.dbcopyer.model.DB;
import ru.yandex.clickhouse.BalancedClickhouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

/**
 * Created by sparkchen on 2017/11/27.
 */
public class ClickHouseImpl extends DBImplBase {

    @Override
    public void connect(DB db) throws DBException {
        if (db.getUrl().isEmpty()) {
            throw new DBException("URL is needed!");
        }
        if (!db.getUrl().startsWith("jdbc:clickhouse://")) {
            throw new DBException("URL should has format: jdbc:clickhouse//ip1:port1,ip2:port2/dbname");
        }
        this.id = db.getId();
        ClickHouseProperties properties = getClickHouseProperties(db);
        try {
            dataSource = new BalancedClickhouseDataSource(db.getUrl(), getClickHouseProperties(db));
            dataSource.getConnection().close();
        } catch(Exception e) {
            throw new DBException(e);
        }
    }

    public ClickHouseProperties getClickHouseProperties(DB db) {
        ClickHouseProperties clickHouseProperties = new ClickHouseProperties();
        clickHouseProperties.setUser(db.getUsername().isEmpty()? "default": db.getUsername());
        clickHouseProperties.setPassword(db.getPassword());
        clickHouseProperties.setMaxExecutionTime(60*60); // sql执行时长
        clickHouseProperties.setSocketTimeout(900*1000);
        clickHouseProperties.setBufferSize(64*64*1024);
        clickHouseProperties.setMaxCompressBufferSize(5*1024*1024);
        clickHouseProperties.setMaxBytesBeforeExternalGroupBy(10*1024*1024*1024L);
        return clickHouseProperties;
    }



}
