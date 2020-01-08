package com.awsdemo.imagetagger.data;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;

@Service
public class DynamoImageDaoImpl {
	
	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	@Value("${dynamodb.table_name}")
	private String tableName;

	@Value("${dynamodb.image_partition_key}")
	private String primaryKey;

	@Value("${dynamodb.image_sort_key}")
	private String sortKey;

	public void putItem(@Value("kevin") String p_key, String s_key, List<String> labels) {
		PutItemRequest req = new PutItemRequest();
		req.addItemEntry(primaryKey, new AttributeValue(p_key));
		req.addItemEntry(sortKey, new AttributeValue(s_key));
		req.addItemEntry("labels", new AttributeValue(labels));
		req.setTableName(tableName);
		amazonDynamoDB.putItem(req);
	}

	public String getItem(@Value("kevin") String p_key, String s_key) {
		GetItemRequest req = new GetItemRequest();
		HashMap<String, AttributeValue> keys = new HashMap<>();
		keys.put(primaryKey, new AttributeValue(p_key));
		keys.put(sortKey, new AttributeValue(s_key));
		req.setKey(keys);
		req.setTableName(tableName);
		return amazonDynamoDB.getItem(req).getItem().get("labels").toString();//. .toString();
	}

	public String deleteItem(@Value("kevin") String p_key, String s_key) {
		DeleteItemRequest req = new DeleteItemRequest();
		HashMap<String, AttributeValue> keys = new HashMap<>();
		keys.put(primaryKey, new AttributeValue(p_key));
		keys.put(sortKey, new AttributeValue(s_key));
		req.setKey(keys);
		req.setTableName(tableName);
		return amazonDynamoDB.deleteItem(req).toString();
	}
}
