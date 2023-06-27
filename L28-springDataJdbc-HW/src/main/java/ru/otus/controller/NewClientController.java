package ru.otus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.repository.ClientRepository;

import java.util.List;

@Controller
public class NewClientController {
    @Autowired
    private ClientRepository repository;

    @GetMapping("/newclient")
    public String createClient() {
        return "newclient.html";
    }

    @PostMapping("/saveclient")
    public String clientSave(String name, String address, String phone, Model model) {
        String messageSuccess = "Клиент успешно создан (name: " + name + ", address: " + address + ", phone: " + phone + ")!";
        String messageFail = "Не удалось создать клиента (name: " + name + ", address: " + address + ", phone: " + phone + ")!";
        String messageBlank = "Не заполнено одно или несколько полей!";
        String message;

        if (!name.isBlank() && !address.isBlank() && !phone.isBlank()) {
            var newClient = buildClient(name, address, phone);

            message = messageSuccess;
            try {
                saveClient(newClient);
            } catch (Exception e) {
                message = messageFail;
            }
        } else {
            message = messageBlank;
        }

        model.addAttribute("messageNewClient", message);
        return "newclient.html";
    }

    private void saveClient(Client client) {
        repository.save(client);
    }

    private Client buildClient(String name, String address, String phone) {
        var newClient = new Client(name);
        var addressObj = new Address(address);
        var phoneObj = new Phone(phone, 0);
        var phonesObj = List.of(phoneObj);
        newClient.setAddress(addressObj);
        newClient.setPhones(phonesObj);

        return newClient;
    }
}
