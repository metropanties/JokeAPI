package me.metropanties.jokeapi.entity;

import java.io.Serializable;

public class Joke implements Serializable {

    private String joke;
    private String creator;

    public Joke() { }

    public Joke(String joke) {
        this.joke = joke;
    }

    public Joke(String joke, String creator) {
        this.joke = joke;
        this.creator = creator;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getJoke() {
        return joke;
    }

    public String getCreator() {
        return creator != null ? creator : "Anonymous";
    }

}
