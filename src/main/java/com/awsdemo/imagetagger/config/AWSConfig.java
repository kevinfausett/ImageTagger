package com.awsdemo.imagetagger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSConfig {

	@Bean 
	AmazonRekognition amazonRekognitionClient() {
		return AmazonRekognitionClientBuilder.standard().build();
	}
	
	@Bean
	AmazonS3 amazonS3Client(@Value("${cloud.aws.region.static}") String region) {
		return AmazonS3ClientBuilder.standard().withRegion(region).build();
	}
	
	@Bean
	AmazonDynamoDB amazonDynamoDBClient(@Value("${cloud.aws.region.static}") String region ){
		return AmazonDynamoDBClientBuilder.standard().withRegion(region).build();
	}
}
