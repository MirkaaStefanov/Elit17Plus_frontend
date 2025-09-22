package com.example.Elit17Plus_frontend.controllers;

import com.example.Elit17Plus_frontend.clients.BenefitClient;
import com.example.Elit17Plus_frontend.clients.PatientClient;
import com.example.Elit17Plus_frontend.dtos.BenefitDTO;
import com.example.Elit17Plus_frontend.dtos.PatientDTO;
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

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/patients")
public class PatientController {

    private final PatientClient patientClient;
    private final BenefitClient benefitClient;

    @GetMapping
    public String getAllPatients(HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        List<PatientDTO> patients = patientClient.getAll(token);
        model.addAttribute("patients", patients);
        return "Patient/all";
    }

    @GetMapping("/create")
    public String createForm(HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        String role = (String) request.getSession().getAttribute("sessionRole");
        if (!"ADMIN".equals(role)) return "redirect:/";

        List<BenefitDTO> benefits = benefitClient.getAll(token);
        model.addAttribute("benefits", benefits);
        model.addAttribute("patient", new PatientDTO());
        return "Patient/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute PatientDTO patientDTO, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        try {
            if (patientDTO.getImageFile() != null && !patientDTO.getImageFile().isEmpty()) {
                byte[] bytes = patientDTO.getImageFile().getBytes();
                String encoded = Base64.getEncoder().encodeToString(bytes);
                patientDTO.setImage(encoded);
            }

            // Fetch BenefitDTOs from the backend using the list of IDs
            if (patientDTO.getBenefitIds() != null && !patientDTO.getBenefitIds().isEmpty()) {
                List<BenefitDTO> selectedBenefits = new ArrayList<>();
                for (UUID benefitId : patientDTO.getBenefitIds()) {
                    selectedBenefits.add(benefitClient.getBenefitById(benefitId, token));
                }
                patientDTO.setBenefits(selectedBenefits);
            }

            patientClient.createPatient(patientDTO, token);
            redirectAttributes.addFlashAttribute("successMessage", "Patient created successfully!");
        } catch (Exception e) {
            log.error("Error creating patient: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create patient!");
        }
        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable UUID id, HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        PatientDTO dto = patientClient.getPatientById(id, token);
        model.addAttribute("patient", dto);
        return "Patient/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable UUID id, @ModelAttribute PatientDTO patientDTO, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        PatientDTO existing = patientClient.getPatientById(id, token);
        existing.setName(patientDTO.getName());
        existing.setSurname(patientDTO.getSurname());
        existing.setEgn(patientDTO.getEgn());
        try {
            if (patientDTO.getImageFile() != null && !patientDTO.getImageFile().isEmpty()) {
                byte[] bytes = patientDTO.getImageFile().getBytes();
                existing.setImage(Base64.getEncoder().encodeToString(bytes));
            }
        } catch (Exception ignored) {}
        patientClient.update(id, existing, token);
        return "redirect:/patients";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        try {
            patientClient.deletePatient(id, token);
            redirectAttributes.addFlashAttribute("successMessage", "Patient deleted successfully!");
        } catch (Exception e) {
            log.error("Error deleting patient: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete patient!");
        }
        return "redirect:/patients";
    }
}
