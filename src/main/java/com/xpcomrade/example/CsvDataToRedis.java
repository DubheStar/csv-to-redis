package com.xpcomrade.example;

import au.com.bytecode.opencsv.CSVReader;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xpcomrade on 2016/2/24.
 * Copyright (c) 2016, xpcomrade@gmail.com All Rights Reserved.
 * Description: TODO(这里用一句话描述这个类的作用). <br/>
 */
public class CsvDataToRedis {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (int i = 122; i<=134; i++) {
            final String filename = "D:\\ddmap\\GOOGLE_OFFSET_BACK\\offset_"+i+".csv";
            executorService.execute(new Runnable() {
                public void run() {
                    try {
                        loadDataFromCSV(filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }



    }

    private static String pattern (String str) {

        return str.replaceAll("\"", "");
    }

    private static String buqi (String str) {
        int index = str.indexOf(".");
        if (index < 0) {
            str += ".00";
        } else {
            int len = str.substring(index+1).length();
            while (len < 2) {
                str += "0";
                len++;
            }
        }
        return str;
    }

    private static void loadDataFromTxt(String filename) throws Exception{
        System.out.println(filename + " begin ....");

        long start = System.currentTimeMillis();
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line = reader.readLine();
        String[] row = null;
        Map<String, Object> map = null;
        String lonOffSet = null;
        String latOffSet = null;
        Jedis jedis = RedisClientWrapper.getInstance().getResource();
        final Pipeline pipeline = jedis.pipelined();
        int i = 0;
        while (line != null) {
            row = line.replaceAll("\"", "").split(",");
            map = new HashMap<String, Object>();
            lonOffSet = buqi(row[0]);
            latOffSet = buqi(row[1]);
            map.put("lonOffSet", lonOffSet);
            map.put("latOffSet", latOffSet);
            map.put("xOffSet", row[4]);
            map.put("yOffSet", row[5]);
            RedisClientWrapper.getInstance().set(lonOffSet + "-" + latOffSet, JsonUtil.toJSONString(map));
            pipeline.set(lonOffSet + "-" + latOffSet, JsonUtil.toJSONString(map));
            if (i == 10000) {
                pipeline.sync();
                i = 0;
            }
            line = reader.readLine();
            i++;
        }
        if (i > 0) {
            pipeline.sync();
        }

        reader.close();
        RedisClientWrapper.getInstance().retrunResource(jedis);
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
        System.out.println(filename + " end ....");
    }

    private static void loadDataFromCSV(String csvFilename) throws IOException {
        System.out.println("\n read " + csvFilename + "  begin .....");
        long start = System.currentTimeMillis();
        CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
        List content = csvReader.readAll();
        String[] row = null;
        String lonOffSet = null;
        String latOffSet = null;
        Map<String, Object> map = null;
        Jedis jedis = RedisClientWrapper.getInstance().getResource();
        final Pipeline pipeline = jedis.pipelined();
        int i = 0;
        for (Object object : content) {
            row = (String[]) object;
            lonOffSet = buqi(row[0]);
            latOffSet = buqi(row[1]);
            map = new HashMap<String, Object>();
            map.put("lonOffSet", lonOffSet);
            map.put("latOffSet", latOffSet);
            map.put("xOffSet", row[4]);
            map.put("yOffSet", row[5]);
            pipeline.set(lonOffSet + "-" + latOffSet, JsonUtil.toJSONString(map));
            if (i == 10000) {
                pipeline.sync();
                i = 0;
            }
            i++;
            //RedisClientWrapper.getInstance().set(lonOffSet + "-" + latOffSet, JsonUtil.toJSONString(map));
        }
        pipeline.sync();
        csvReader.close();
        RedisClientWrapper.getInstance().retrunResource(jedis);
        System.out.println("\n read " + csvFilename + "  end, time consuming:" + (System.currentTimeMillis() - start));

    }
}
