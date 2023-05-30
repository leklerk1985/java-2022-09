package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.*;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class); // изменил

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        saveTestClients(dbServiceClient);
    }

    private static void saveTestClients(DBServiceClient dbServiceClient) {
        saveFirstTestClient(dbServiceClient);
        saveSecondTestClient(dbServiceClient);
        saveThirdTestClient(dbServiceClient);
        saveFourthTestClient(dbServiceClient);
    }

    private static void saveFirstTestClient(DBServiceClient dbServiceClient) {
        var client = new Client("First client");
        var address = new Address("First street");
        var phone1 = new Phone("8 919 111 11 11");
        var phone2 = new Phone("8 908 111 11 11");
        var phones = List.of(phone1, phone2);
        client.setAddress(address);
        client.setPhones(phones);

        dbServiceClient.saveClient(client);
    }

    private static void saveSecondTestClient(DBServiceClient dbServiceClient) {
        var client = new Client("Second client");
        var address = new Address("Second street");
        var phone = new Phone("8 919 222 22 22");
        var phones = List.of(phone);
        client.setAddress(address);
        client.setPhones(phones);

        dbServiceClient.saveClient(client);
    }

    private static void saveThirdTestClient(DBServiceClient dbServiceClient) {
        var client = new Client("Third client");
        var address = new Address("Third street");
        var phone = new Phone("8 919 333 33 33");
        var phones = List.of(phone);
        client.setAddress(address);
        client.setPhones(phones);

        dbServiceClient.saveClient(client);
    }

    private static void saveFourthTestClient(DBServiceClient dbServiceClient) {
        var client = new Client("Fourth client");
        var address = new Address("Fourth street");
        var phone = new Phone("8 911 444 44 44");
        var phones = List.of(phone);
        client.setAddress(address);
        client.setPhones(phones);

        dbServiceClient.saveClient(client);
    }
}