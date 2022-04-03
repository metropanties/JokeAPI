package me.metropanties.jokeapi.controller;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import me.metropanties.jokeapi.Server;
import me.metropanties.jokeapi.service.services.TokenService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TokenRout implements Handler {

    private final TokenService tokenService;

    public TokenRout() {
        this.tokenService = Server.getInstance().getServiceManager().getService(TokenService.class);
    }

    @Override
    public void handle(@NotNull Context ctx) {
        ctx.status(HttpCode.CREATED).json(Map.of(
                "token", tokenService.generate()
        ));
    }

}
