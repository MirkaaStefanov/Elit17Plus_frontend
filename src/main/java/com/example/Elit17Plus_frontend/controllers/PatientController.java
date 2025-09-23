package com.example.Elit17Plus_frontend.controllers;

import com.example.Elit17Plus_frontend.clients.BenefitClient;
import com.example.Elit17Plus_frontend.clients.PatientClient;
import com.example.Elit17Plus_frontend.clients.VisitClient;
import com.example.Elit17Plus_frontend.dtos.BenefitDTO;
import com.example.Elit17Plus_frontend.dtos.PatientDTO;
import com.example.Elit17Plus_frontend.dtos.VisitDTO;
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
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/patients")
public class PatientController {

    private final PatientClient patientClient;
    private final BenefitClient benefitClient;
    private final VisitClient visitClient;

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
        String role = (String) request.getSession().getAttribute("sessionRole");
        if (!"ADMIN".equals(role)) return "redirect:/";

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

    @GetMapping("/{id}")
    public String getPatient(@PathVariable UUID id, HttpServletRequest request, Model model){
        String token = (String) request.getSession().getAttribute("sessionToken");
        String role = (String) request.getSession().getAttribute("sessionRole");
        if (!"ADMIN".equals(role)) return "redirect:/";

        PatientDTO dto = patientClient.getPatientById(id, token);
        List<VisitDTO> visits = visitClient.getForPatient(id, token);
        model.addAttribute("patient", dto);
        model.addAttribute("visits", visits);
        return "Patient/patient";

    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable UUID id, HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        String role = (String) request.getSession().getAttribute("sessionRole");
        if (!"ADMIN".equals(role)) return "redirect:/";

        PatientDTO dto = patientClient.getPatientById(id, token);

        // Populate benefitIds from the patient's existing benefits for the form to display correctly
        if (dto.getBenefits() != null) {
            List<UUID> benefitIds = dto.getBenefits().stream()
                    .map(BenefitDTO::getId)
                    .collect(Collectors.toList());
            dto.setBenefitIds(benefitIds);
        }

        List<BenefitDTO> benefits = benefitClient.getAll(token);
        model.addAttribute("patient", dto);
        model.addAttribute("benefits", benefits);
        return "Patient/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable UUID id, @ModelAttribute PatientDTO patientDTO, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        String role = (String) request.getSession().getAttribute("sessionRole");
        if (!"ADMIN".equals(role)) return "redirect:/";

        PatientDTO existing = patientClient.getPatientById(id, token);
        existing.setName(patientDTO.getName());
        existing.setSurname(patientDTO.getSurname());
        existing.setEgn(patientDTO.getEgn());

        // Update benefits based on the submitted list of IDs
        if (patientDTO.getBenefitIds() != null && !patientDTO.getBenefitIds().isEmpty()) {
            List<BenefitDTO> selectedBenefits = patientDTO.getBenefitIds().stream()
                    .map(benefitId -> benefitClient.getBenefitById(benefitId, token))
                    .collect(Collectors.toList());
            existing.setBenefits(selectedBenefits);
        } else {
            // If no benefits are selected, clear the list
            existing.setBenefits(new ArrayList<>());
        }

        try {
            if (patientDTO.getImageFile() != null && !patientDTO.getImageFile().isEmpty()) {
                byte[] bytes = patientDTO.getImageFile().getBytes();
                existing.setImage(Base64.getEncoder().encodeToString(bytes));
            }
        } catch (Exception ignored) {}

        patientClient.update(id, existing, token);
        redirectAttributes.addFlashAttribute("successMessage", "Patient updated successfully!");
        return "redirect:/patients/"+id;
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        String role = (String) request.getSession().getAttribute("sessionRole");
        if (!"ADMIN".equals(role)) return "redirect:/";

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
