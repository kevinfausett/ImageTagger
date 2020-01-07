package com.awsdemo.imagetagger;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.awsdemo.imagetagger.data.DynamoImageDaoImpl;
import com.awsdemo.imagetagger.storage.ImageStorageService;
import com.awsdemo.imagetagger.vision.RekognitionServiceImpl;

@Controller
public class FileStorageController {
	
	@Autowired
	ImageStorageService imageStorageService;
	
	@Autowired
	RekognitionServiceImpl visionService;
	
	@Autowired
	DynamoImageDaoImpl imageDao;
	
	@GetMapping("/")
	public String getPage(Model model) {
		model.addAttribute("files", imageStorageService.loadAll().collect(Collectors.toList()));
		return "uploadForm";
	}
	
	@PostMapping("/img")
	public String uploadImg(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		String key = imageStorageService.uploadFile(file);
		List<String> labels = visionService.getLabels(key);
	    redirectAttributes.addFlashAttribute("message",
	            "You successfully uploaded " + file.getOriginalFilename() + "!");
	    
	    redirectAttributes.addFlashAttribute("labels", labels);
	    imageDao.putItem(key, labels);
		return "redirect:/img";
	}
	
	@DeleteMapping("/img")
	public String deleteImage(@RequestParam("key") String key) {
		imageStorageService.deleteFile(key);
		return "redirect:/img";
	}
	
	// debugging convenience
	@DeleteMapping("/img/all")
	public String deleteAll() {
		imageStorageService.emptyBucket();
		return "redirect:/img";
		
	}

	// debugging convenience
	@GetMapping("/tags")
	@ResponseBody
	public String getTags(@RequestParam("key") String key) {
		return imageDao.getItem(key);
	}
	
}