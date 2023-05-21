package ru.otus.cachehw;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.hw18.core.repository.executor.DbExecutorImpl;
import ru.otus.cachehw.hw18.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.cachehw.hw18.crm.datasource.DriverManagerDataSource;
import ru.otus.cachehw.hw18.crm.model.Client;
import ru.otus.cachehw.hw18.crm.model.Manager;
import ru.otus.cachehw.hw18.crm.service.DbServiceClientImpl;
import ru.otus.cachehw.hw18.crm.service.DbServiceManagerImpl;
import ru.otus.cachehw.hw18.mapper.*;
import ru.otus.cachehw.mycache.HwCache;
import ru.otus.cachehw.mycache.MyCache;

import javax.sql.DataSource;

public class HomeWork {

    private static final String URL = "jdbc:postgresql://localhost:5432/demoDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "sigma05";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {

        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();


        // Работа с клиентом
        EntityClassMetaData entityClassMetaDataClient = new EntityClassMetaDataImpl(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient.getConstructor(),
                Client.class, entityClassMetaDataClient.getName()); //реализация DataTemplate, универсальная


        // Код дальше должен остаться
        var cache = new MyCache<String, Client>();
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, cache);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);


        // Сделайте то же самое с классом Manager (для него надо сделать свою таблицу)
        EntityClassMetaData entityClassMetaDataManager = new EntityClassMetaDataImpl(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager.getConstructor(),
                Manager.class, entityClassMetaDataManager.getName());

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerSecondSelected = dbServiceManager.getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        log.info("managerSecondSelected:{}", managerSecondSelected);

    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}