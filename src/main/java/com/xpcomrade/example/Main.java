package com.xpcomrade.example;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.util.List;

/**
 * Created by xpcomrade on 2017/9/12.
 * Copyright (c) 2017, zhongping.wang@xqchuxing.com All Rights Reserved.
 * Desc: TODO(这里用一句话描述这个类的作用). <br/>
 */
@SuppressWarnings("all")
public class Main {
    public static void main(String[] args) throws Exception {
        String csvFilename = "/Users/xpcomrade/Desktop/employeeTemplet.csv";
        System.out.println("\n read " + csvFilename + "  begin .....");
        long start = System.currentTimeMillis();
        CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
        List content = csvReader.readAll();
        String[] row = null;
        for (int i=1;i<content.size();i++){
            row = (String[]) content.get(i);

            System.out.println(JsonUtil.toJSONString(row));
        }
        csvReader.close();
        System.out.println("time:" + (System.currentTimeMillis() - start));
        System.out.println("\n read " + csvFilename + "  end .....");

    }
}
