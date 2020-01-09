package com.awsdemo.imagetagger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.awsdemo.imagetagger.storage.ImageStorageService;
import com.awsdemo.imagetagger.taggedimage.TaggedImage;
import com.awsdemo.imagetagger.taggedimage.TaggedImageServiceImpl;

@Controller
public class FileStorageController {
	
	@Autowired
	ImageStorageService imageStorageService;
			
	@Autowired
	TaggedImageServiceImpl taggedImageService;
	
	@GetMapping("/")
	public String getPage(Model model) {
		model.addAttribute("files", imageStorageService.loadAll().collect(Collectors.toList()));
		return "uploadForm";
	}
	
	@PostMapping("/img")
	public String uploadImg(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		TaggedImage img = taggedImageService.postTaggedImage(file);
	    redirectAttributes.addFlashAttribute("message",
	            "You successfully uploaded " + file.getOriginalFilename() + "!");
	    
	    redirectAttributes.addFlashAttribute("labels", img.getLabels());
		return "redirect:/";
	}
	
	@DeleteMapping("/img")
	public String deleteImage(@RequestParam("key") String key, @RequestParam("user_id") String userId) {
		taggedImageService.deleteTaggedImage(key, userId);
		return "redirect:/";
	}
	
	// debugging convenience
	@DeleteMapping("/img/all")
	public String deleteAll() {
		taggedImageService.deleteAll();
		return "redirect:/";
		
	}

//	// debugging convenience
//	@GetMapping("/tags")
//	@ResponseBody
//	public String getTags(@RequestParam("key") String key) {
//		return imageDao.getItem("kevin", key);
//	}
	
}