package me.metropanties.jokeapi.controller.joke;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import me.metropanties.jokeapi.Server;
import me.metropanties.jokeapi.service.services.JokeService;
import org.jetbrains.annotations.NotNull;

public class GetallJokesRout implements Handler {

    private final JokeService jokeService;

    public GetallJokesRout() {
        this.jokeService = Server.getInstance().getServiceManager().getService(JokeService.class);
    }

    @Override
    public void handle(@NotNull Context ctx) {
        ctx.status(HttpCode.OK).json(jokeService.getAll());
    }

}
