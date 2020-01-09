package com.awsdemo.imagetagger;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	@GetMapping("/searchdebug/{term:.+}")
	@ResponseBody
	public String searchTerm(@PathVariable String term) {
		System.out.println(term);
		return searchService.searchStr(term);
	}

	@GetMapping("/search/{term:.+}")
	public String searchTerm(@PathVariable String term, Model model) {
		List<String> matches;
		try {
			matches = searchService.search(term);
			model.addAttribute("matches", matches);
			model.addAttribute("message", term + ": " + matches.size() + " matches");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "search";
	}


}
