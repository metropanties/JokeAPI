package me.metropanties.jokeapi.auth;

import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import me.metropanties.jokeapi.Server;
import me.metropanties.jokeapi.service.services.TokenService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class TokenAccessManager implements AccessManager {

    private static final String UNAUTHORIZED_MESSAGE = "Please provide a valid API Key!";
    private static final Set<String> UNAUTHORISED_ENDPOINTS = Set.of(
            Server.API_URL + "/auth/token"
    );

    private final TokenService tokenService;

    public TokenAccessManager() {
        this.tokenService = Server.getInstance().getServiceManager().getService(TokenService.class);
    }

    @Override
    public void manage(@NotNull Handler handler, @NotNull Context ctx, @NotNull Set<RouteRole> routeRoles) throws Exception {
        for (String ignored : UNAUTHORISED_ENDPOINTS) {
            if (ctx.path().equalsIgnoreCase(ignored)) {
                handler.handle(ctx);
                return;
            }
        }

        if (ctx.header("Authorization") == null) {
            ctx.status(401).json(Map.of("message", UNAUTHORIZED_MESSAGE));
            return;
        }

        String token = ctx.header("Authorization");
        if (token == null || !tokenService.validate(token)) {
            ctx.status(401).json(Map.of("message", UNAUTHORIZED_MESSAGE));
            return;
        }

        handler.handle(ctx);
    }

}
