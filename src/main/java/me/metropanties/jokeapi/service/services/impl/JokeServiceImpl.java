package me.metropanties.jokeapi.service.services.impl;

import me.metropanties.jokeapi.Server;
import me.metropanties.jokeapi.entity.Joke;
import me.metropanties.jokeapi.service.services.JokeService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JokeServiceImpl implements JokeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JokeServiceImpl.class);

    private final HashMap<Long, Joke> jokeCache = new HashMap<>();

    @Override
    public void onStartup() {
        try {
            Connection connection = Server.getInstance().getDatabase().getConnection();
            if (connection == null) {
                LOGGER.error("Failed loading jokes from database, connection is null!");
                return;
            }

            final String sql = "SELECT * FROM jokes";
            try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
                while (rs.next()) {
                    long jokeID = rs.getLong("id");
                    if (jokeCache.containsKey(jokeID))
                        return;

                    jokeCache.put(jokeID, new Joke(rs.getString("joke")));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed loading jokes from database!", e);
        }
    }

    @Override
    public void onShutdown() {
        try {
            Connection connection = Server.getInstance().getDatabase().getConnection();
            if (connection == null) {
                LOGGER.error("Failed saving jokes to database, connection is null!");
                return;
            }

            final String sql = "INSERT INTO jokes (joke) VALUES (?)";
            for (Joke joke : jokeCache.values()) {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, joke.getJoke());
                    statement.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addJoke(@NotNull Joke joke) {
        Objects.requireNonNull(joke, "Joke cannot be null!");
        if (jokeCache.containsValue(joke))
            return;

        int id = 0;
        while (true) {
            if (!jokeCache.containsKey((long) id)) {
                jokeCache.put((long) id, joke);
                break;
            }
            id++;
        }
    }

    @Override
    public void deleteJoke(long id) {
        if (!jokeCache.containsKey(id))
            return;

        jokeCache.remove(id);
    }

    @Override
    public Optional<Joke> getJoke(long id) {
        if (!jokeCache.containsKey(id))
            return Optional.empty();

        return Optional.of(jokeCache.get(id));
    }

    @Override
    public Collection<Joke> getAll() {
        return jokeCache.values();
    }

}
