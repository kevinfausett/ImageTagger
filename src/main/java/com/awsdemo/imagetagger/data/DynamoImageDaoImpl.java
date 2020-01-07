package com.awsdemo.imagetagger.data;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;

@Service
public class DynamoImageDaoImpl {
	
	@Autowired
	AmazonDynamoDB amazonDynamoDB;
	
	@Value("${dynamodb.table_name}")
	String tableName;
	
	public void putItem(String key, List<String> labels) {
		PutItemRequest req = new PutItemRequest();
		req.addItemEntry("s3key", new AttributeValue(key));
		req.addItemEntry("labels", new AttributeValue(labels));
		req.setTableName(tableName);
		amazonDynamoDB.putItem(req);
	}
	
	public String getItem(String key) {
		GetItemRequest req = new GetItemRequest();
		HashMap<String, AttributeValue> keys = new HashMap<>();
		keys.put("s3key", new AttributeValue(key));
		req.setKey(keys);
		req.setTableName(tableName);
		return amazonDynamoDB.getItem(req).getItem().get("labels").toString();//. .toString();
	}
}
