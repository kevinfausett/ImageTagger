package com.awsdemo.imagetagger.vision;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;

@Service
public class RekognitionServiceImpl implements VisionService {
	
	@Autowired
	AmazonRekognition amazonRekognitionClient;
	
	@Value("${app.awsServices.bucketName}")
	String bucketName;

	
	public List<String> getLabels(String key) {
		DetectLabelsRequest req = new DetectLabelsRequest();
		S3Object obj = new S3Object().withBucket(bucketName).withName(key);
		Image image = new Image().withS3Object(obj);
		req.withImage(image);
		return amazonRekognitionClient.detectLabels(req).getLabels().stream().map(label -> label.getName().toString()).collect(Collectors.toList());
	}
}
