package com.xpcomrade.example;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenCsvExample {
	public static void main(String[] args) {

		try {
			//readLineByLineExample();

			/*for (int i = 73; i <= 134; i++) {
				readAllExample("D:\\ddmap\\GOOGLE_OFFSET_BACK\\offset_" + i + ".csv");
			}*/
			readAllExample("D:\\ddmap\\GOOGLE_OFFSET_BACK\\offset_" + 121 + ".csv");
			//writeCSVExample();

			//writeAllExample();

			//mapJavaBeanExample();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readAllExample(String csvFilename) throws IOException {
		System.out.println("\n read " + csvFilename + "  begin .....");
		long start = System.currentTimeMillis();
		CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
		List content = csvReader.readAll();
		String[] row = null;
		for (Object object : content) {
			row = (String[]) object;
			String key = row[0] + "-" + row[1];
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("lonOffSet", row[0]);
			map.put("latOffSet", row[1]);
			map.put("xOffSet", row[4]);
			map.put("yOffSet", row[5]);
			System.out.println(JsonUtil.toJSONString(map));
		}

		csvReader.close();
		System.out.println("time:" + (System.currentTimeMillis() - start));
		System.out.println("\n read " + csvFilename + "  end .....");

	}

	private static void readLineByLineExample() throws IOException {
		System.out.println("\n**** readLineByLineExample ****");
		String csvFilename = "C:\\work\\sample.csv";
		CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
		String[] row = null;
		while ((row = csvReader.readNext()) != null) {
			System.out.println(row[0] + " # " + row[1] + " #  " + row[2]);
		}

		csvReader.close();
	}

	private static void writeCSVExample() throws IOException {
		System.out.println("\n**** writeCSVExample ****");

		String csv = "C:\\work\\output.csv";

		CSVWriter writer = new CSVWriter(new FileWriter(csv));

		String[] country = "India#China#United States".split("#");
		writer.writeNext(country);
		System.out.println("CSV written successfully.");
		writer.close();
	}

	private static void writeAllExample() throws IOException {
		System.out.println("\n**** writeAllExample ****");

		String csv = "C:\\work\\output2.csv";
		CSVWriter writer = new CSVWriter(new FileWriter(csv));

		List<String[]> data = new ArrayList<String[]>();
		data.add(new String[] { "India", "New Delhi" });
		data.add(new String[] { "United States", "Washington D.C" });
		data.add(new String[] { "Germany", "Berlin" });

		writer.writeAll(data);
		System.out.println("CSV written successfully.");
		writer.close();
	}

	public static void mapJavaBeanExample() throws FileNotFoundException {
		System.out.println("\n**** mapJavaBeanExample ****");

		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(Country.class);
		String[] columns = new String[] { "countryName", "capital" }; 
		strat.setColumnMapping(columns);

		CsvToBean csv = new CsvToBean();

		String csvFilename = "C:\\work\\sample.csv";
		CSVReader csvReader = new CSVReader(new FileReader(csvFilename));

		List list = csv.parse(strat, csvReader);
		for (Object object : list) {
			Country country = (Country) object;
			System.out.println(country.getCapital());
		}
	}
}
