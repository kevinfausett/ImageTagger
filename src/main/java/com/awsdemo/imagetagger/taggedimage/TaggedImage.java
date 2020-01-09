package com.awsdemo.imagetagger.taggedimage;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="imagetags")
public class TaggedImage {
	@DynamoDBRangeKey(attributeName="s3key")
	private String key;
	
    @DynamoDBHashKey(attributeName="user_id")
	private String userId;
	
    @DynamoDBAttribute(attributeName="labels")
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
