package ru.otus.webserver.servlet;

import org.hibernate.QueryException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import static ru.otus.core.repository.HibernateUtils.buildSessionFactory;
import static ru.otus.core.repository.HibernateUtils.doInSessionWithTransaction;

public final class ServletUtils {
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private ServletUtils() {
    }

    public static String generateHtmlForTableAllClients() {
        String tableHtml = "<table style=\"width: 400px\"> ";

        tableHtml += "<thead> <tr> ";
        tableHtml += "<td style=\"width: 50px\">Id</td> ";
        tableHtml += "<td style=\"width: 150px\">Имя</td> ";
        tableHtml += "<td style=\"width: 100px\">Адрес</td> ";
        tableHtml += "<td style=\"width: 100px\">Телефоны</td> ";
        tableHtml += "</tr> </thead>";

        tableHtml += "<tbody> ";

        var allClients = getAllClientsAsMaps();
        for (var client: allClients) {
            tableHtml += "<tr> ";
            tableHtml += "<td>" +  client.get("id") + "</td> ";
            tableHtml += "<td>" +  client.get("name") + "</td> ";
            tableHtml += "<td>" +  client.get("address") + "</td> ";
            tableHtml += "<td>" +  client.get("phones") + "</td> ";
            tableHtml += "</tr> ";
        }
        tableHtml += "</tbody> ";

        tableHtml += "</table>";

        return tableHtml;
    }

    private static List<Client> getAllClients() {
        var dbServiceClient = createDBServiceClient();
        return dbServiceClient.findAll();
    }

    private static List<Map<String, String>> getAllClientsAsMaps() {
        var allClientsAsMaps = new ArrayList<Map<String, String>>();
        Map<String, String> currClientAsMap;

        var allClients = getAllClients();
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

    public static void createClient(String nameClient, String streetAddress, String numberPhone) {
        var newClient = new Client(nameClient);
        var address = new Address(streetAddress);
        var phone = new Phone(numberPhone);
        var phones = List.of(phone);
        newClient.setAddress(address);
        newClient.setPhones(phones);

        var dbServiceClient = createDBServiceClient();
        dbServiceClient.saveClient(newClient);
    }

    private static DBServiceClient createDBServiceClient() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }
}
