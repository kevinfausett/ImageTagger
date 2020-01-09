package com.awsdemo.imagetagger.storage;

import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.awsdemo.imagetagger.data.DynamoImageDaoImpl;

/**
 * A service providing an API to the file system.
 */
public interface ImageStorageService {
	
	/**
	 * Stores a file in the file system
	 * @param file: The file to be uploaded
	 * @return String: the file system's key for the newly uploaded file
	 */
	String uploadFile(MultipartFile file);
	/**
	 * Provides a URL to access a file
	 * @param key: The key to lookup in the file system
	 * @return String: a pre-signed URL to access the file, expires in 1 hour
	 */
	String getUrl(String key);

	/**
	 * Removes a file from the file system
	 * @param key: The key to lookup and delete in the file system
	 */
	void deleteFile(String key);
	
	// Temp during development
	Stream<String> loadAll();
	
	// Temp during development
	void emptyBucket(DynamoImageDaoImpl imageDao);
}
