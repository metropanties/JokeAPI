package me.metropanties.jokeapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.javalin.Javalin;
import me.metropanties.jokeapi.auth.TokenAccessManager;
import me.metropanties.jokeapi.controller.TokenRout;
import me.metropanties.jokeapi.controller.joke.GetJokeRoute;
import me.metropanties.jokeapi.controller.joke.GetallJokesRout;
import me.metropanties.jokeapi.controller.joke.PostJokeRoute;
import me.metropanties.jokeapi.database.Database;
import me.metropanties.jokeapi.entity.Config;
import me.metropanties.jokeapi.service.Service;
import me.metropanties.jokeapi.service.ServiceManager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static final String API_URL = "/api/v1";

    private static Server instance;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder()
            .setNameFormat("Server Thread")
            .setUncaughtExceptionHandler((t, e) -> LOGGER.error("Error occurred on {}!", t.getName(), e))
            .build());
    private final Config config;
    private final Database database;
    private final ServiceManager serviceManager;

    public Server() {
        instance = this;
        this.config = loadConfig();
        if (config == null) {
            LOGGER.error("Failed to start, config file is null!");
            System.exit(0);
        }

        this.database = new Database(config.getHost(),
                config.getUsername(),
                config.getPassword(),
                config.getDatabase(),
                config.getDatabasePort()
        );
        database.connect();
        database.awaitReady();

        this.serviceManager = new ServiceManager();
        serviceManager.getServices().forEach(Service::onStartup);

        Javalin app = Javalin.create(config -> config.accessManager(new TokenAccessManager()))
                .start(config.getPort());

        app.get("/", ctx -> ctx.result("Hello world!"));
        app.get(API_URL + "/auth/token", new TokenRout());
        app.get(API_URL + "/joke/get/{id}", new GetJokeRoute());
        app.get(API_URL + "/joke/getall", new GetallJokesRout());

        app.post(API_URL + "/joke/create", new PostJokeRoute());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            serviceManager.getServices().forEach(Service::onShutdown);
            database.disconnect();
            instance = null;
        }));
    }

    @Nullable
    private Config loadConfig() {
        URL resource = Server.class.getClassLoader().getResource("config.json");
        if (resource == null)
            throw new IllegalStateException("Resource not found!");

        File file = null;
        try {
            file = new File(resource.toURI());
        } catch (URISyntaxException e) {
            LOGGER.error("Failed to load config file!", e);
        }
        if (file == null)
            throw new NullPointerException("Config file is null!");

        try {
            return new ObjectMapper().readValue(file, Config.class);
        } catch (IOException e) {
            LOGGER.error("Error occurred mapping config from config file!", e);
        }
        return null;
    }

    public static Server getInstance() {
        return instance;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public Config getConfig() {
        return config;
    }

    public Database getDatabase() {
        return database;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

}
