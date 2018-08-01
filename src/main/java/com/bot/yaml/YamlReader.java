package com.bot.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlReader {
	
	private String filePath;
	
	public YamlReader(String filePath) {
		this.filePath = filePath;
	}
	
	public Map<String, Object> retrieveYaml() throws FileNotFoundException {
		
		File file = new File(filePath);
		FileInputStream stream = new FileInputStream(file);
		
		return convertToMap(stream);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> convertToMap(FileInputStream stream) {
		
		Yaml yaml = new Yaml();
		Map<String, Object> values = (Map<String, Object>) yaml.load(stream);
		
		return values;
	}
}
