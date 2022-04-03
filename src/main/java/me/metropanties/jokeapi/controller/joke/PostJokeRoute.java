package me.metropanties.jokeapi.controller.joke;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import me.metropanties.jokeapi.Server;
import me.metropanties.jokeapi.entity.Joke;
import me.metropanties.jokeapi.service.services.JokeService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PostJokeRoute implements Handler {

    private final JokeService jokeService;

    public PostJokeRoute() {
        this.jokeService = Server.getInstance().getServiceManager().getService(JokeService.class);
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        Joke joke = ctx.bodyAsClass(Joke.class);
        if (joke == null) {
            ctx.status(HttpCode.BAD_REQUEST).json(Map.of(
                    "message", "Please provide a valid joke body!"
            ));
            return;
        }

        jokeService.addJoke(joke);
        ctx.status(HttpCode.CREATED).json(joke);
    }

}
