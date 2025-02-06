package com.mateusz113.shop.auth;

import java.io.Serializable;

public record LoginDetails(String email, String password) implements Serializable {
    public static class LoginDetailsBuilder {
        private String email;
        private String password;

        private LoginDetailsBuilder() {
        }

        public LoginDetailsBuilder email(String email) {
            this.email = email;
            return this;
        }

        public LoginDetailsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public LoginDetails build() {
            if (email == null || password == null) {
                throw new IllegalStateException("Wszystkie zmienne muszą być zainicjowane przed budowaniem!");
            }
            return new LoginDetails(email, password);
        }
    }

    public static LoginDetailsBuilder builder() {
        return new LoginDetailsBuilder();
    }
}
