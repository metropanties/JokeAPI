package me.metropanties.jokeapi.service.services.impl;

import me.metropanties.jokeapi.cache.ExpiringSet;
import me.metropanties.jokeapi.service.services.TokenService;
import me.metropanties.jokeapi.util.APIKeyGenerator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    private final ExpiringSet<String> tokenCache = new ExpiringSet<>();

    private String createToken() {
        String token = null;
        boolean exists = false;
        while (!exists) {
            token = APIKeyGenerator.generate();
            if (!tokenCache.contains(token)) {
                tokenCache.add(token);
                LOGGER.info("Generated new API Key: " + token);
                exists = true;
            }
        }
        return token;
    }

    @Override
    public String generate() {
        return createToken();
    }

    @Override
    public boolean validate(@NotNull String token) {
        return tokenCache.contains(token);
    }

    @Override
    public Collection<String> values() {
        return tokenCache.values();
    }

}
