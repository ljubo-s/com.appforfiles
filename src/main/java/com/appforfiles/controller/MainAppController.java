package com.appforfiles.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainAppController {

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			String message = "Hi " + principal.getName() //
					+ "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);
		}
		return "user/403";
	}

	@RequestMapping("/404.html")
	public String render404(Model model) {
		// Add model attributes
		return "404";
	}

	@RequestMapping(value = "/admin/{page}")
	public String adminRequest(@PathVariable("page") String page) {

		return "admin/" + page;
	}

	@RequestMapping(value = "/user/{page}")
	public String userRequest(@PathVariable("page") String page) {

		return "user/" + page;
	}

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String errorPage(Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {

			return "redirect:/admin/dashboard";

		} else if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {

			return "redirect:/user/dashboard";

		} else {

			return "/login";
		}

	}

}
