package com.gls.examples.springsecurity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
	
	@GetMapping(path="/")
	public String helloWorld() {
		return "<h1>Hello World</h1>";
	}
	
	@GetMapping(path="/user")
	public String userPage() {
		return "<h1>Hello User</h1>";
	}

	@GetMapping(path="/admin")
	public String adminPage() {
		return "<h1>Hello Admin</h1>";
	}
}
