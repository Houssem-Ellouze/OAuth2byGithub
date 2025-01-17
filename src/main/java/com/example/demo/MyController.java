package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {
    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) {
        System.out.println(oAuth2User.getAttributes());
        model.addAttribute("user", oAuth2User.getAttribute("login"));
        model.addAttribute("profileLink", oAuth2User.getAttribute("html_url"));
        model.addAttribute("repos", oAuth2User.getAttribute("repos_url"));
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (oAuth2User != null) {
            System.out.println(authentication.toString());
            model.addAttribute("user", oAuth2User.getAttribute("login"));
            model.addAttribute("profileLink", oAuth2User.getAttribute("html_url"));
            model.addAttribute("repos", oAuth2User.getAttribute("repos_url"));
            return "home";
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            SecurityContextHolder.clearContext();
        }
        request.getSession().invalidate();
        return "logout";
    }
    
    @GetMapping("/error")
    public String error(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("errorMessage", error != null ? error : "Unknown error occurred.");
        return "error";
    }
}
