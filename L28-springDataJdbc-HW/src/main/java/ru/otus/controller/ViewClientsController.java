package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewClientsController {
    @GetMapping("/")
    public String viewClients() {
        return "clients.html";
    }
}
