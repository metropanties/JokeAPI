package me.metropanties.jokeapi.controller.joke;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import me.metropanties.jokeapi.Server;
import me.metropanties.jokeapi.entity.Joke;
import me.metropanties.jokeapi.service.services.JokeService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class GetJokeRoute implements Handler {

    private final JokeService jokeService;

    public GetJokeRoute() {
        this.jokeService = Server.getInstance().getServiceManager().getService(JokeService.class);
    }

    @Override
    public void handle(@NotNull Context ctx) {
        final String jokeID = ctx.pathParam("id");
        Optional<Joke> foundJoke = jokeService.getJoke(Long.parseLong(jokeID));

        foundJoke.ifPresentOrElse(joke -> ctx.status(HttpCode.FOUND).json(joke),
                () -> ctx.status(HttpCode.NOT_FOUND).json(
                        Map.of("message", "Joke not found, try again.")
                ));
    }

}
