package com.awsdemo.imagetagger;
import java.io.File;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.awsdemo.imagetagger.storage.ImageStorageService;

@Controller
public class FileStorageController {
	@Autowired
	ImageStorageService imageStorageService;
	
	@GetMapping("/img")
	public String getPage(Model model) {
//	    model.addAttribute("files", storageService.loadAll().map(
//	            path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//	                "serveFile", path.getFileName().toString()).build().toString())
//	            .collect(Collectors.toList()));
//		model.addAttribute("files", imageStorageService.loadAll().map(
//				path -> MvcUriComponentsBuilder.fromMethodName(FileStorageController.class,
//						"serveFile", path.getFilename().toString()).build().toString())
//				.collect(Collectors.toList()));
		model.addAttribute("files", imageStorageService.loadAll().collect(Collectors.toList()));
		return "uploadForm";
	}
	
	@PostMapping("/img")
	public String uploadImg(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		imageStorageService.uploadFile(file);
	    redirectAttributes.addFlashAttribute("message",
	            "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/img";
	}
	
	@DeleteMapping("/img")
	public String deleteImage(@RequestParam("key") String key) {
		imageStorageService.deleteFile(key);
		return "redirect:/img";
	}
}