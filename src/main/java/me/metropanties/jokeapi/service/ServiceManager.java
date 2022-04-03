package me.metropanties.jokeapi.service;

import me.metropanties.jokeapi.service.services.JokeService;
import me.metropanties.jokeapi.service.services.TokenService;
import me.metropanties.jokeapi.service.services.impl.JokeServiceImpl;
import me.metropanties.jokeapi.service.services.impl.TokenServiceImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceManager {

    private final ConcurrentHashMap<Class<? extends Service>, Object> services = new ConcurrentHashMap<>();

    public ServiceManager() {
        services.put(TokenService.class, new TokenServiceImpl());
        services.put(JokeService.class, new JokeServiceImpl());
    }

    public <T> T getService(@NotNull Class<T> tClass) {
        Object service = services.get(tClass);
        return tClass.cast(service);
    }

    public Collection<Service> getServices() {
        return Collections.emptyList();
    }

}
