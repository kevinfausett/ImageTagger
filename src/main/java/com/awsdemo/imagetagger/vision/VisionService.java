package com.awsdemo.imagetagger.vision;

import java.util.List;

/**
 * This service provides an API for computation based on computer vision.
 *
 */
public interface VisionService {
	/*
	 * @param key: The key (in the file system) for the VisionService to access and analyze
	 * @returns List<String>: The labels describing the contents of the image 
	 */
	public List<String> getLabels(String key);
	}
