package com.example.Elit17Plus_frontend.controllers;

import com.example.Elit17Plus_frontend.clients.UserClient;
import com.example.Elit17Plus_frontend.dtos.auth.PublicUserDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public String allUsers(Model model, HttpServletRequest request) {

        String token = (String) request.getSession().getAttribute("sessionToken");
        String userRole = (String) request.getSession().getAttribute("sessionRole");

        if (userRole == null || !userRole.equals("ADMIN")) {
            return "redirect:/";
        }

        List<PublicUserDTO> allUsers = userClient.getAllUsers(token);
        PublicUserDTO me = userClient.getMe(token);
        allUsers.remove(me);

        model.addAttribute("allUsers", allUsers);
        return "User/all";
    }
}
