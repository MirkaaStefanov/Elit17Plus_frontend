package com.example.Elit17Plus_frontend.controllers;

import com.example.Elit17Plus_frontend.clients.VisitClient;
import com.example.Elit17Plus_frontend.exception.VisitException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/patients")
public class VisitController {

    private final VisitClient visitClient;

    @PostMapping("/{patientId}")
    public String setVisit(@PathVariable UUID patientId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        String role = (String) request.getSession().getAttribute("sessionRole");

        if (!"ADMIN".equals(role)) {
            return "redirect:/";
        }

        try {
            visitClient.addVisit(patientId, token);
            return "redirect:/patients/"+patientId;
        } catch (VisitException e) {
            // Catch the specific stock error and add a flash attribute
            log.error("Visit exception: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/patients/"+patientId;
        } catch (Exception e) {
            // Catch any other unexpected errors
            log.error("Failed : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Възникна грешка");
            return "redirect:/patients/"+patientId;
        }
    }

}
