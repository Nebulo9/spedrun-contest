package fr.nebulo9.speedruncontest.util;

import java.util.HashMap;
import java.util.Map;

public class Config {
	private final Map<String,String> stringValues = new HashMap<>();
	
	private final Map<String,Boolean> booleanValues = new HashMap<>();
	
	public void addValue(String key, String value) {
		stringValues.put(key, value);
	}
	
	public void addValue(String key,boolean value) {
		booleanValues.put(key, value);
	}
	
	public Map<String,String> getStringValues() {
		return this.stringValues;
	}
	
	public Map<String,Boolean> getBooleanValues() {
		return this.booleanValues;
	}
	
	public String getStringValue(String key) {
		return stringValues.get(key);
	}
	
	public boolean getBooleanValue(String key) {
		return booleanValues.get(key);
	}
}
