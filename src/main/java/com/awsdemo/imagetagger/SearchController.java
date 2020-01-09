package com.awsdemo.imagetagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.awsdemo.imagetagger.search.ElasticsearchServiceImpl;

@Controller
public class SearchController {
	
	@Autowired
	ElasticsearchServiceImpl searchService;
	
	@GetMapping("/search")
	@ResponseBody
	public String search() {
		return searchService.getAll();
	}
	
	@GetMapping("/search/{term:.+}")
	@ResponseBody
	public String searchTerm(@PathVariable String term) {
		return searchService.search(term);
	}

}
