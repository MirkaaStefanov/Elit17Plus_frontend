package com.example.Elit17Plus_frontend.controllers;

import com.example.Elit17Plus_frontend.clients.OAuth2Client;
import com.example.Elit17Plus_frontend.dtos.auth.AuthenticationResponse;
import com.example.Elit17Plus_frontend.dtos.auth.PendingRequest;
import com.example.Elit17Plus_frontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OAuth2Controller {
    private final OAuth2Client oAuth2Client;
    private final SessionManager sessionManager;
    private static final String REDIRECTTXT = "redirect:/";


    @GetMapping("/login/google")
    public RedirectView redirectToGoogleAuth() {
        String authUrl = oAuth2Client.auth();
        return new RedirectView(authUrl);
    }

    @GetMapping("/process-oauth2")
    public ModelAndView handleGoogleCallback(@RequestParam("code") String code, HttpServletRequest httpServletRequest) {
        ResponseEntity<AuthenticationResponse> response = oAuth2Client.googleAuthenticate(code);
        sessionManager.setSessionToken(httpServletRequest, response.getBody().getAccessToken(), response.getBody().getUser().getRole().toString());

        System.out.println(httpServletRequest.getSession().getAttribute("pendingRequest"));

        PendingRequest pendingRequest = (PendingRequest) httpServletRequest.getSession().getAttribute("pendingRequest");
        if (pendingRequest != null) {
            return new ModelAndView("redirect:/continue-action");
        }

        String redirectUrl = (String) httpServletRequest.getSession().getAttribute("redirectAfterLogin");
        if (redirectUrl != null) {
            httpServletRequest.getSession().removeAttribute("redirectAfterLogin");
            return new ModelAndView("redirect:" + redirectUrl);
        }

        return new ModelAndView(REDIRECTTXT);
    }

}
