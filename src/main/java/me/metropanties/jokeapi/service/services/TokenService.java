package me.metropanties.jokeapi.service.services;

import me.metropanties.jokeapi.service.Service;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface TokenService extends Service {

    String generate();

    boolean validate(@NotNull String token);

    Collection<String> values();

}
