package com.inkandreams.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class HomeController {

    @Value("${app.frontend.url:https://ink-and-dream.onrender.com}")
    private String frontendUrl;

    @GetMapping("/")
    public void redirectToFrontend(HttpServletResponse response) throws IOException {
        response.sendRedirect(frontendUrl);
    }
}
