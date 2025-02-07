package com.mateusz113.shop.auth;

/**
 * Class representing user register details collected from console.
 *
 * @param firstName user first name.
 * @param lastName user last name.
 * @param email user email.
 * @param password user password.
 */
public record RegisterDetails(
        String firstName,
        String lastName,
        String email,
        String password
) {
    /**
     * Builder class for {@code RegisterDetails}.
     */
    public static class RegisterDetailsBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        private RegisterDetailsBuilder(){
        }

        public RegisterDetailsBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public RegisterDetailsBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public RegisterDetailsBuilder email(String email) {
            this.email = email;
            return this;
        }

        public RegisterDetailsBuilder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * Builder build method.
         *
         * @return {@code RegisterDetails} instance with builder fields.
         * @throws IllegalStateException if not all fields were initialized before calling the build.
         */
        public RegisterDetails build() {
            if (firstName == null || lastName == null || email == null || password == null) {
                throw new IllegalStateException("Wszystkie zmienne muszą być zainicjowane przed budowaniem!");
            }
            return new RegisterDetails(firstName, lastName, email, password);
        }
    }

    /**
     * Builder creator method.
     *
     * @return builder for the {@code RegisterDetails}.
     */
    public static RegisterDetailsBuilder builder(){
        return new RegisterDetailsBuilder();
    }
}
