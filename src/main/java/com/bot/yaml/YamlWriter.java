package com.bot.yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlWriter {
	
	private String filePath;
	
	public YamlWriter(String filePath) {
		this.filePath = filePath;
	}
	
	public void write(Map<String, Object> newData) throws IOException {
		
		Yaml yaml = new Yaml();
		
		Map<String, Object> serverData = loadFile();
		
		Map<String,Object> updatedData = updateData(newData, serverData);
		
		FileWriter writer = new FileWriter(filePath);
		
		if(updatedData != null) {
			yaml.dump(updatedData, writer);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> loadFile() throws FileNotFoundException {
		
		Yaml yaml = new Yaml();
		
		FileInputStream stream = new FileInputStream(filePath);
		Map<String, Object> loadedFile = (Map<String,Object>) yaml.load(stream);
		
		return loadedFile;	
	}
	
	/*
	 * Currently all data which is used by this program is formatted Map<String, List<Map<String,String>>>
	 * This method will work for this data format recursively, unsure if I want to make it able to be more general yet
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> updateData(Map<String, Object> newData, Map<String, Object> serverData) {
		
		for (Map.Entry<String, Object> newEntry : newData.entrySet()) {
			
			for (Map.Entry<String, Object> serverEntry : serverData.entrySet()) {
				
				if (serverEntry.getKey().equals(newEntry.getKey())) {
					
					if (serverEntry.getValue() instanceof ArrayList) {
						
						for(Map<String, Object> map : (ArrayList<Map<String, Object>>) serverEntry.getValue()) {
							
							Map<String, Object> subMap = (Map<String, Object>) newEntry.getValue();
							updateData(subMap, map);							
						}
					}
					
					else if(serverEntry.getValue() instanceof String || serverEntry.getValue() == null) {
						
						serverData.put(serverEntry.getKey(), newEntry.getValue());
						return serverData;
					}
				}
			}
		}		
		return null;
	}
}
