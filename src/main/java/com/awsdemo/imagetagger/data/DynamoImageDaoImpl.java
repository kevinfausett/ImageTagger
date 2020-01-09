package com.awsdemo.imagetagger.data;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.awsdemo.imagetagger.taggedimage.TaggedImage;

@Service
public class DynamoImageDaoImpl implements ImageDao {
	
	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	@Value("${dynamodb.table_name}")
	private String tableName;

	@Value("${dynamodb.image_partition_key}")
	private String primaryKey;

	@Value("${dynamodb.image_sort_key}")
	private String sortKey;
	
	private DynamoDBMapper mapper;
	
	@PostConstruct
	@SuppressWarnings("deprecation")
	// AWS Docs recommended this class
	private void mapper() {
		this.mapper = new DynamoDBMapper(amazonDynamoDB, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
	}

	public void putItem(String p_key, String s_key, List<String> labels) {
		TaggedImage img = new TaggedImage(s_key, p_key, labels);
		mapper.save(img);
	}

	public TaggedImage getItem(String p_key, String s_key) {
		return mapper.load(TaggedImage.class, p_key, s_key);
	}

	public String deleteItem(String p_key, String s_key) {
		// Mapper would require two requests, loading the object and then deleting.
		DeleteItemRequest req = new DeleteItemRequest();
		HashMap<String, AttributeValue> keys = new HashMap<>();
		keys.put(primaryKey, new AttributeValue(p_key));
		keys.put(sortKey, new AttributeValue(s_key));
		req.setKey(keys);
		req.setTableName(tableName);
		return amazonDynamoDB.deleteItem(req).toString();
	}
}
