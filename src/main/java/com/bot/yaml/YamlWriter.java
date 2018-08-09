package com.bot.yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlWriter {
	
	private String filePath;
	
	public YamlWriter(String filePath) {
		this.filePath = filePath;
	}
	
	public void replace(Map<String, Object> newData) throws IOException {
		
		Yaml yaml = new Yaml();
		
		Map<String, Object> oldData = loadFile();
		Map<String,Object> updatedData = replaceData(newData, oldData);
		
		FileWriter writer = new FileWriter(filePath);
		yaml.dump(updatedData, writer);
	}
	
	public void add(Map<String, Object> newData) throws IOException {
		
		Yaml yaml = new Yaml();
		
		Map<String, Object> oldData = loadFile();
		Map<String,Object> updatedData = addData(newData, oldData);
		
		FileWriter writer = new FileWriter(filePath);
		yaml.dump(updatedData, writer);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> loadFile() throws FileNotFoundException {
		
		Yaml yaml = new Yaml();
		
		FileInputStream stream = new FileInputStream(filePath);
		Map<String, Object> loadedFile = (Map<String,Object>) yaml.load(stream);
		
		return loadedFile;	
	}
	
	/*
	 * Currently all data which can be replaced is formatted Map<String, List<Map<String,String>>>
	 * This method will work for this data format recursively, unsure if I want to make it able to be more general yet
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> replaceData(Map<String, Object> newData, Map<String, Object> data) {
		
		for (Map.Entry<String, Object> newEntry : newData.entrySet()) {
			
			for (Map.Entry<String, Object> serverEntry : data.entrySet()) {
				
				if (serverEntry.getKey().equals(newEntry.getKey())) {
					
					if (serverEntry.getValue() instanceof ArrayList) {
						
						for(Map<String, Object> map : (ArrayList<Map<String, Object>>) serverEntry.getValue()) {
							
							Map<String, Object> subMap = (Map<String, Object>) newEntry.getValue();
							replaceData(subMap, map);							
						}
					}
					
					else if(serverEntry.getValue() instanceof String || serverEntry.getValue() == null) {
						
						data.put(serverEntry.getKey(), newEntry.getValue());
						return data;
					}
				}
			}
		}		
		return data;
	}
	
	/*
	 * Currently all data which can be replaced is formatted Map<String, List<Map<String,List<String>>>
	 * This method will work for this data format recursively, unsure if I want to make it able to be more general yet
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> addData(Map<String, Object> newData, Map<String, Object> data) {
		
		for (Map.Entry<String, Object> newEntry : newData.entrySet()) {
			
			for (Map.Entry<String, Object> serverEntry : data.entrySet()) {
				
				if (serverEntry.getKey().equals(newEntry.getKey())) {
					
					if (serverEntry.getValue() instanceof ArrayList) {
						
						try {
							
							if( ((List<String>) serverEntry.getValue()).isEmpty()) {
								throw new ClassCastException("");
							}
							
							for(Map<String, Object> map : (ArrayList<Map<String, Object>>) serverEntry.getValue()) {
								
								Map<String, Object> subMap = (Map<String, Object>) newEntry.getValue();
								addData(subMap, map);							
							}
							
						} catch(ClassCastException e) {
							List<String> newList = new ArrayList<String>();
							newList.addAll( (List<String>) serverEntry.getValue());
							
							newList.addAll( (List<String>) newEntry.getValue());
						
							data.put(serverEntry.getKey(), newList);
							return data;
						}
					}
				}
			}
		}		
		return data;
	}
}
