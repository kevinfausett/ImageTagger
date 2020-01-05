package com.awsdemo.imagetagger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSConfig {

	@Bean
	public AmazonS3 amazonS3Client(
			@Value("${cloud.aws.region.static}") String region,
			@Value("${aws.access_key_id}") String accessKey, 
			@Value("${aws.secret_access_key}") String secretKey) {
		return AmazonS3ClientBuilder.standard().withRegion(region).build();
		
	}
}
