package ru.poliscam.bank.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticController {

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping(value="/payments/{number}")
	public String payments(@PathVariable("number") String number, Model model) {
		model.addAttribute("number", number);
		return "payments";
	}

}
