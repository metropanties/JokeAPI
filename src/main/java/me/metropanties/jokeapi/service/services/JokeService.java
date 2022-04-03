package me.metropanties.jokeapi.service.services;

import me.metropanties.jokeapi.entity.Joke;
import me.metropanties.jokeapi.service.Service;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface JokeService extends Service {

    void addJoke(@NotNull Joke joke);

    void deleteJoke(long id);

    Optional<Joke> getJoke(long id);

    Collection<Joke> getAll();

}
