package ch.born.wte.impl.service;

import java.util.Map;

import ch.born.wte.WteDataModel;

public class WteMapModel implements WteDataModel {

	private final Map<String, Object> dataMap;

	public WteMapModel(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	@Override
	public Object getValue(String key) {
		return dataMap.get(key);
	}

	public Map<String, Object> getWrapped() {
		return dataMap;
	}

}