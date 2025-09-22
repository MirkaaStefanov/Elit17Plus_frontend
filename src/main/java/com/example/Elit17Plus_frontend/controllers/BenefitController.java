package com.example.Elit17Plus_frontend.controllers;

import com.example.Elit17Plus_frontend.clients.BenefitClient;
import com.example.Elit17Plus_frontend.dtos.BenefitDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/benefits")
public class BenefitController {

    private final BenefitClient benefitClient;

    @GetMapping
    public String getAll(HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        List<BenefitDTO> benefits = benefitClient.getAll(token);
        model.addAttribute("benefits", benefits);
        return "Benefit/all";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("benefit", new BenefitDTO());
        return "Benefit/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute BenefitDTO dto, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        try {
            benefitClient.createBenefit(dto, token);
            redirectAttributes.addFlashAttribute("successMessage", "Benefit created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create benefit!");
        }
        return "redirect:/benefits";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable UUID id, HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        BenefitDTO dto = benefitClient.getBenefitById(id, token);
        model.addAttribute("benefit", dto);
        return "Benefit/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable UUID id, @ModelAttribute BenefitDTO dto, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        benefitClient.updateBenefit(id, dto, token);
        return "redirect:/benefits";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        try {
            benefitClient.deleteBenefit(id, token);
            redirectAttributes.addFlashAttribute("successMessage", "Benefit deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete benefit!");
        }
        return "redirect:/benefits";
    }
}
