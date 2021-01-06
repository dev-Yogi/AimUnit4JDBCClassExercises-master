package com.aim.movie.util;

public enum PostgreSQL {
    URL("jdbc:postgresql://localhost:5432/movie"), USER("postgres"), PASS("bigredarmy");

    public final String value;

    private PostgreSQL(String value) {
        this.value = value;
    }

}