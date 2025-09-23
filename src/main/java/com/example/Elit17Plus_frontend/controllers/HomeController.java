package com.example.Elit17Plus_frontend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {


    @GetMapping("/")
    public String homePage(Model model, HttpServletRequest request){

        String role = (String) request.getSession().getAttribute("sessionRole");
        if ("ADMIN".equals(role)){
            return "redirect:/patients";
        }

        return "index";
    }
}
