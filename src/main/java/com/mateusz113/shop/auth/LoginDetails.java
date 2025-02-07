package com.mateusz113.shop.auth;

import java.io.Serializable;

/**
 * Class representing user login details collected from console.
 *
 * @param email user email.
 * @param password user password.
 */
public record LoginDetails(String email, String password) implements Serializable {
    /**
     * Builder class for {@code LoginDetails}.
     */
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

        /**
         * Builder build method.
         *
         * @return {@code LoginDetails} instance with builder fields.
         * @throws IllegalStateException if not all fields were initialized before calling the build.
         */
        public LoginDetails build() {
            if (email == null || password == null) {
                throw new IllegalStateException("Wszystkie zmienne muszą być zainicjowane przed budowaniem!");
            }
            return new LoginDetails(email, password);
        }
    }

    /**
     * Builder creator method.
     *
     * @return builder for the {@code LoginDetails}.
     */
    public static LoginDetailsBuilder builder() {
        return new LoginDetailsBuilder();
    }
}
