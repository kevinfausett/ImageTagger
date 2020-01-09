package com.awsdemo.imagetagger.taggedimage;

import java.util.List;

public class TaggedImage {
	private String key;
	
	private String userId;
	
	private List<String> labels;
	
	public TaggedImage(String key, String userId, List<String> labels) {
		this.key = key;
		this.userId = userId;
		this.labels = labels;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public List<String> getLabels() {
		return this.labels;
	}
}
