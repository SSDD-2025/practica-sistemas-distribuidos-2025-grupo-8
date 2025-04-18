package es.codeurjc.gymapp.security.jwt;

import java.time.Duration;

public enum TokenType {

    ACCESS(Duration.ofMinutes(420), "AuthToken"),
    REFRESH(Duration.ofDays(31), "RefreshToken");

    /**
     * Token lifetime in seconds
     */
    public final Duration duration;
    public final String cookieName;

    TokenType(Duration duration, String cookieName) {
        this.duration = duration;
        this.cookieName = cookieName;
    }
}