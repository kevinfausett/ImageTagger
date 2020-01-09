package com.awsdemo.imagetagger.taggedimage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.awsdemo.imagetagger.data.DynamoImageDaoImpl;
import com.awsdemo.imagetagger.search.ElasticsearchServiceImpl;
import com.awsdemo.imagetagger.storage.ImageStorageService;
import com.awsdemo.imagetagger.vision.VisionService;
@Service
public class TaggedImageServiceImpl {
	
	@Autowired
	ImageStorageService imageStorageService;
	
	@Autowired
	VisionService visionService;
	
	@Autowired
	DynamoImageDaoImpl imageDao;
	
	@Autowired
	ElasticsearchServiceImpl searchService;
	
	/**
	 * Uploads the given file to the file system, collects labels from computer vision service, and stores that information
	 * in the DB. Asynchronously, the DB will report the changes in a stream to an AWS Lambda, 
	 * who will make the corresponding changes in the search index.
	 * @param file
	 * @return TaggedImage: an object representing the new document
	 */
	public TaggedImage postTaggedImage(MultipartFile file) {
		String key = imageStorageService.uploadFile(file);
		List<String> labels = visionService.getLabels(key);
		imageDao.putItem("kevin", key, labels);		
		return new TaggedImage(key, "kevin", labels);
	}
	
	public void deleteTaggedImage(String key, String userId) {
		imageStorageService.deleteFile(key);
		imageDao.deleteItem(userId, key);
	}
	
	public void deleteAll() {
		imageStorageService.emptyBucket(imageDao);
	}
}
