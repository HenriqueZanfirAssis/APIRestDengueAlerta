package br.edu.atitus.denguealerta.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws/status")
public class StatusController {

	@GetMapping({"", "/"})
	public String getStatus() {
		return "Servidor está no ar";
	}
	
}
