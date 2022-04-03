package me.metropanties.jokeapi.service;

public interface Service {

    default void onStartup() { }

    default void onShutdown() { }

}
