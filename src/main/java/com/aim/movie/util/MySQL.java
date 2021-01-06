package com.aim.movie.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum MySQL {
    URL("jdbc:mysql://localhost:3306/movie?"), USER("root"), PASS("B3xl3315");

    public final String value;

    private MySQL(String value) {
        this.value = value;
    }

}