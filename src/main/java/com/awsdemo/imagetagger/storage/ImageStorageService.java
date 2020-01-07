package com.awsdemo.imagetagger.storage;

import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;

/**
 * A service to CRUD images. This serves as a layer of abstraction over the service that touches the S3 APIs in order to make it
 * easier to change platforms if needed.
 *
 */
public interface ImageStorageService {
	
	String uploadFile(MultipartFile file);

	String getUrl(String key);

	void deleteFile(String key);
	
	Stream<String> loadAll();
	
	public S3Object getObject(String key);

	void emptyBucket();
}
