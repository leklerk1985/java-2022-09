package ru.otus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.crm.model.Client;
import ru.otus.repository.ClientRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AllClientsController {
    @Autowired
    private ClientRepository repository;

    @GetMapping("/allclients")
    public String viewAllClients(Model model) {
        var allClients = getAllClientsAsMaps();
        model.addAttribute("allclients", allClients);
        return "allclients.html";
    }

    private List<Map<String, String>> getAllClientsAsMaps() {
        var allClientsAsMaps = new ArrayList<Map<String, String>>();
        Map<String, String> currClientAsMap;
        List<Client> allClients;

        try {
            allClients = getAllClients();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return allClientsAsMaps;
        }

        for (var client: allClients) {
            currClientAsMap = new HashMap<>();
            currClientAsMap.put("id", client.getId().toString());
            currClientAsMap.put("name", client.getName());
            currClientAsMap.put("address", client.getAddress().toString());
            currClientAsMap.put("phones", client.getPhonesAsString());

            allClientsAsMaps.add(currClientAsMap);
        }

        return allClientsAsMaps;
    }

    private List<Client> getAllClients() {
        return repository.findAll();
    }
}
