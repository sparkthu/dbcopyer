package com.sohu.compass.dbcopyer.yaml;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sparkchen on 2017/11/28.
 */
public class YamlParserTest extends TestCase {

    public void testReplace() throws Exception {
        Map<String, String> envs = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(Calendar.getInstance().getTime());
        envs.putAll(System.getenv());
        if (!envs.containsKey("TODAY")) {
            envs.put("TODAY", today);
        }
        for (String keys : envs.keySet()) {
            System.out.println(keys + ":" + envs.get(keys));
        }
        Assert.assertEquals(YamlParser.replace(envs, "{{.TODAY}} is a good day, but {{.SYSTEM}} error"), today + " is a good day, but  error");
        envs.put("SYSTEM", "MAC");
        Assert.assertEquals(YamlParser.replace(envs, "{{.TODAY}} is a good day, but {{.SYSTEM}} error"), today + " is a good day, but MAC error");
        Assert.assertEquals("nothing", YamlParser.replace(envs, "nothing"));
        Assert.assertEquals("", YamlParser.replace(envs, ""));
        Assert.assertEquals("{{TODAY}}" + today + "\n" + today, YamlParser.replace(envs, "{{TODAY}}{{.TODAY}}\n{{.TODAY}}"));
        Assert.assertEquals(envs.get("JAVA_HOME"), YamlParser.replace(envs, "{{.JAVA_HOME}}"));

    }
}