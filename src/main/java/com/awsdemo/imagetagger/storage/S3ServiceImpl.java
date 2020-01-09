package com.awsdemo.imagetagger.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.awsdemo.imagetagger.data.DynamoImageDaoImpl;

@Service
class S3ServiceImpl implements ImageStorageService {
	
	@Autowired
	AmazonS3 amazonS3Client;
			
	@Value("${app.awsServices.bucketName}")
	String bucketName;

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
	    File convFile = new File(file.getOriginalFilename());
	    FileOutputStream fos = new FileOutputStream(convFile);
	    fos.write(file.getBytes());
	    fos.close();
	    return convFile;
	}

	@Override
	public String uploadFile(MultipartFile file) {
		try {
			String key = file.getOriginalFilename() + Instant.now();
			System.out.println("Bucket: " + bucketName);
			amazonS3Client.putObject(bucketName, key, convertMultiPartToFile(file));
			return key;
		}
		catch (IOException e ){
			return "";
		}
	}

	@Override
	public String getUrl(String key) {
		Date expiration = LocalDateTime.now().plusHours(1).toDate();
		return amazonS3Client.generatePresignedUrl(bucketName, key, expiration, HttpMethod.GET).toExternalForm();
	}
	
	@Override
	public void deleteFile(String key) {
		amazonS3Client.deleteObject(bucketName, key);
	}
	
	@Override
	public Stream<String> loadAll() {
		// https://stackoverflow.com/questions/8027265/how-to-list-all-aws-s3-objects-in-a-bucket-using-java
		// Lists first 1000, see above post if we need more than that
		ObjectListing listing = amazonS3Client.listObjects(bucketName, "");
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
		Date expiration = LocalDateTime.now().plusHours(1).toDate();
		Stream<String> urls = summaries.stream().map(
				object -> amazonS3Client.generatePresignedUrl(bucketName, object.getKey(), expiration, HttpMethod.GET).toExternalForm());
		return urls;
	}

	@Override
	public void emptyBucket(DynamoImageDaoImpl imageDao) {
		ObjectListing objectListing = amazonS3Client.listObjects(bucketName);
		Boolean more = true;
        while (more) {
        	Iterator<S3ObjectSummary> objIter = objectListing.getObjectSummaries().iterator();
        	while (objIter.hasNext()) {
        		String key = objIter.next().getKey();
        		amazonS3Client.deleteObject(bucketName, key);
        		imageDao.deleteItem("kevin", key);
        	}

            if (objectListing.isTruncated()) {
                objectListing = amazonS3Client.listNextBatchOfObjects(objectListing);
            } else {
            	more = false;
            }
        }
	}

}
