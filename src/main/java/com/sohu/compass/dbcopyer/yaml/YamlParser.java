package com.sohu.compass.dbcopyer.yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sohu.compass.dbcopyer.model.Column;
import com.sohu.compass.dbcopyer.model.ConfModel;
import com.sohu.compass.dbcopyer.model.DB;
import com.sohu.compass.dbcopyer.model.Task;
import org.yaml.snakeyaml.Yaml;
/**
 * Created by sparkchen on 2017/11/27.
 */
public class YamlParser {
    public static ConfModel parse(String filename) {
        Yaml yaml = new Yaml();
        try {
            InputStream inputStream = null;
            if (filename.isEmpty()) {
                inputStream = YamlParser.class.getResourceAsStream("/tasks.yml");
            } else {
                inputStream = new FileInputStream(filename);
            }
            ConfModel confModel = yaml.loadAs(inputStream, ConfModel.class);
            System.out.println("model:" + confModel);

            return confModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String check(ConfModel confModel) {
        Set<String> dbs = new HashSet<>();
        for (DB db : confModel.getDatabases()) {
            if (dbs.contains(db.getId())) {
                return "DB appear more than once, id=" + db.getId();
            } else {
                dbs.add(db.getId());
            }
        }
        for (Task task: confModel.getTasks()) {
            if (task.getDest().isEmpty() || task.getSource().isEmpty()) {
                return "Task must have source and dest";
            }
            if (task.getId().isEmpty()) {
                return "Task must have id";
            }
            if (!dbs.contains(task.getDest())) {
                return "Do not find DB config for id:" + task.getDest();
            }
            if (! dbs.contains(task.getSource())) {
                return "Do not find DB config for id:" + task.getSource();
            }
            Set<String> columnTypes = new HashSet<String>(){{
                add("string");
                add("int");
                add("number");
                add("float");
                add("enum");
                add("double");
                add("long");
            }};
            for (Column column : task.getColumns()) {
                if (column.getIndexInResult() == 0) {
                    return "IndexInResult is 1 based!";
                }
                if (!columnTypes.contains(column.getType())) {
                    return "Column type " + column.getType() + " can not be recognized, use string or number";
                }
                if (!column.getGroovy().isEmpty() && !column.getGroovy().contains("convert")) {
                    return "Using groovy but convert function not found, " +
                        "try def convert(x){def map=[\"a\":\"A\"], \"b\":\"B\"]; " +
                        "map.get(x) == null? x.toUpperCase(): x}";
                }
            }

        }
        return "";
    }
    private void preprocess(ConfModel confModel) {
        Map<String, String> envs = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(Calendar.getInstance().getTime());
        envs.putAll(System.getenv());
        if (!envs.containsKey("TODAY")) {
            envs.put("TODAY", today);
        }
        for (Task task : confModel.getTasks()) {
            task.setSourceSql(replace(envs, task.getSourceSql()));
            task.setDestTable(replace(envs, task.getDestTable()));

        }
        for (DB db: confModel.getDatabases()) {
            db.setFilepath(replace(envs, db.getFilepath()));
            db.setPassword(replace(envs, db.getPassword()));
            db.setUrl(replace(envs, db.getUrl()));
            db.setUsername(replace(envs, db.getUsername()));
            db.setFormat(replace(envs, db.getFormat()));

        }

    }
    public static String replace(Map<String,String> envs, String value) {
        String regex = "[{][{]\\.([a-zA-Z0-9_]+)[}][}]";
        StringBuffer stringBuffer = new StringBuffer();
        Matcher m =  Pattern.compile(regex).matcher(value);
        while (m.find()) {
            m.appendReplacement(stringBuffer, envs.get(m.group(1)) == null? "": envs.get(m.group(1)));
        }
        m.appendTail(stringBuffer);
        return stringBuffer.toString();
    }
}
