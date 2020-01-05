package com.awsdemo.imagetagger.storage;

import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.policy.Resource;

/**
 * A service to CRUD images. This serves as a layer of abstraction over the service that touches the S3 APIs in order to make it
 * easier to change platforms if needed.
 *
 */
public interface ImageStorageService {
	
	void uploadFile(MultipartFile file);

	String getFile(String key);

	void deleteFile(String key);
	
	Stream<String> loadAll();
}
