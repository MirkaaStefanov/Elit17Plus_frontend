//package com.example.Elit17Plus_frontend.controllers;
//
//import com.example.Elit17Plus_frontend.clients.AppointmentClient;
//import com.example.Elit17Plus_frontend.clients.ShoppingCartClient;
//import com.example.Elit17Plus_frontend.dtos.AppointmentDTO;
//import com.example.Elit17Plus_frontend.dtos.ServiceDTO;
//import com.example.Elit17Plus_frontend.dtos.WorkerDTO;
//import com.example.Elit17Plus_frontend.dtos.auth.PendingRequest;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//import java.util.UUID;
//
//@Controller
//@RequiredArgsConstructor
//@Slf4j
//@RequestMapping
//public class ContinueActionController {
//
//
//    @GetMapping("/continue-action")
//    public ModelAndView continueAction(HttpServletRequest request) {
//        String token = (String) request.getSession().getAttribute("sessionToken");
//        String role = (String) request.getSession().getAttribute("sessionRole");
//        PendingRequest pendingRequest = (PendingRequest) request.getSession().getAttribute("pendingRequest");
//
//        if (pendingRequest == null || token == null) {
//            return new ModelAndView("redirect:/");
//        }
//
//        request.getSession().removeAttribute("pendingRequest");
//
//        try {
//            if (pendingRequest.getRequestURI().startsWith("/appointments/book")) {
//
//                if(!role.equals("USER")){
//                    return new ModelAndView("redirect:/");
//                }
//                // Reconstruct the AppointmentDTO from the stored parameters
//                Map<String, String[]> params = pendingRequest.getParameters();
//                AppointmentDTO appointmentDTO = new AppointmentDTO();
//                appointmentDTO.setWorker(new WorkerDTO());
//                appointmentDTO.setService(new ServiceDTO());
//
//                appointmentDTO.getWorker().setId(UUID.fromString(params.get("worker.id")[0]));
//                appointmentDTO.getService().setId(UUID.fromString(params.get("service.id")[0]));
//                appointmentDTO.setStartTime(LocalDateTime.parse(params.get("startTime")[0]));
//
//
//                appointmentClient.bookAppointment(appointmentDTO, token);
//                return new ModelAndView("redirect:/appointments/my-appointments");
//            } else if (pendingRequest.getRequestURI().startsWith("/shopping-cart/add")) {
//
//                if(!role.equals("USER")){
//                    return new ModelAndView("redirect:/");
//                }
//                // Get the product ID from the URI
//                String[] pathSegments = pendingRequest.getRequestURI().split("/");
//                Long productId = Long.valueOf(pathSegments[pathSegments.length - 1]);
//                shoppingCartClient.addToCart(productId, 1, token);
//                return new ModelAndView("redirect:/shopping-cart");
//            } else if (pendingRequest.getRequestURI().startsWith("/shopping-cart/update")) {
//
//                if(!role.equals("USER")){
//                    return new ModelAndView("redirect:/");
//                }
//                // Get the parameters for updating quantity
//                Map<String, String[]> params = pendingRequest.getParameters();
//                Long cartItemId = Long.valueOf(params.get("cartItemId")[0]);
//                int quantity = Integer.valueOf(params.get("quantity")[0]);
//                shoppingCartClient.updateQuantity(cartItemId, quantity, token);
//                return new ModelAndView("redirect:/shopping-cart");
//            }
//        } catch (Exception e) {
//            log.error("Error re-executing pending request: {}", e.getMessage());
//            // In case of error, redirect to a safe page with an error message
//            ModelAndView mv = new ModelAndView("redirect:/error"); // Or a dedicated error page
//            mv.addObject("error", "Възникна грешка при завършване на вашата заявка.");
//            return mv;
//        }
//
//        // If no matching action is found, redirect to home
//        return new ModelAndView("redirect:/");
//    }
//}