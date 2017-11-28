package com.sohu.compass.dbcopyer;

import com.sohu.compass.dbcopyer.executor.Executor;
import com.sohu.compass.dbcopyer.model.ConfModel;
import com.sohu.compass.dbcopyer.yaml.YamlParser;

/**
 * Created by sparkchen on 2017/11/27.
 */
public class Main {
    public static void main(String[] args) {
        String yaml = "";
        String task = "";
        for (int i = 0; i< args.length; i++) {
            if (args[i].equals("--help")) {
                System.out.println("Usage: java -jar this.jar --conf task.yml,task2.yml [--task taskname]");
                System.out.println("all task will be executed without --task");
                return;
            } else if (args[i].equals("--conf")) {
                i++;
                yaml = args[i];
            } else if (args[i].equals("--task")) {
                i++;
                task = args[i];
            }
        }
        String[] yamls = yaml.split(",");
        ConfModel confModel = new ConfModel();
        for (String filename : yamls) {
            confModel.Merge(YamlParser.parse(filename));
        }
        String errorInfo = YamlParser.check(confModel);
        if (errorInfo != null && !errorInfo.isEmpty()) {
            System.out.println("Error when checking conf:" + errorInfo);
            return;
        }
        if (confModel.getTasks() != null) {
            Executor executor = new Executor();
            executor.run(confModel, task);
        }
    }
}
