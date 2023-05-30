package ru.otus.webserver.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.webserver.services.TemplateProcessor;
import java.io.IOException;
import java.util.*;
import static ru.otus.webserver.servlet.ServletUtils.generateHtmlForTableAllClients;

public class AllClientsServlet extends HttpServlet {
    private final TemplateProcessor templateProcessor;

    private final Gson gson;

    public AllClientsServlet(TemplateProcessor templateProcessor, Gson gson) {
        this.templateProcessor = templateProcessor;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tableHtml = generateHtmlForTableAllClients();

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(tableHtml));
    }
}
