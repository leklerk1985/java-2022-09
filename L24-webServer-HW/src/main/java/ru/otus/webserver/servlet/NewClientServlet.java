package ru.otus.webserver.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.webserver.services.TemplateProcessor;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static ru.otus.webserver.servlet.ServletUtils.createClient;

public class NewClientServlet extends HttpServlet {
    private final TemplateProcessor templateProcessor;
    private final Gson gson;

    public NewClientServlet(TemplateProcessor templateProcessor, Gson gson) {
        this.templateProcessor = templateProcessor;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var clientData = getClientDataFromQueryString(request.getQueryString());
        String clientName = clientData.get("clientName");
        String clientAddress = clientData.get("clientAddress");
        String clientPhone = clientData.get("clientPhone");
        String message;

        try {
            createClient(clientName, clientAddress, clientPhone);
            message = "Клиент успешно создан (name: " + clientName + ", address: " + clientAddress + ", phone: " + clientPhone + ")!";
        } catch (Exception e) {
            message = "Не удалось создать клиента (name: " + clientName + ", address: " + clientAddress + ", phone: " + clientPhone + ")!";
        }

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(message));
    }

    private Map<String, String> getClientDataFromQueryString(String queryString) {
        String[] queries = queryString.split(",");
        String queryClientName = queries[0];
        String queryClientAddress = queries[1];
        String queryClientPhone = queries[2];

        String[] partsClientName = queryClientName.split("=");
        String clientName = partsClientName[1];
        String[] partsClientAddress = queryClientAddress.split("=");
        String clientAddress = partsClientAddress[1];
        String[] partsClientPhone = queryClientPhone.split("=");
        String clientPhone = partsClientPhone[1];

        var clientData =  new HashMap<String, String>();
        clientData.put("clientName", clientName);
        clientData.put("clientAddress", clientAddress);
        clientData.put("clientPhone", clientPhone);

        return clientData;
    }
}
