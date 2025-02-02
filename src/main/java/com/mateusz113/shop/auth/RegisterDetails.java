package com.mateusz113.shop.auth;

public record RegisterDetails(
        String firstName,
        String lastName,
        String email,
        String password
) {
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

        public RegisterDetails build() {
            if (firstName == null || lastName == null || email == null || password == null) {
                throw new IllegalStateException("Wszystkie zmienne muszą być zainicjowane przed budowaniem!");
            }
            return new RegisterDetails(firstName, lastName, email, password);
        }
    }

    public static RegisterDetailsBuilder builder(){
        return new RegisterDetailsBuilder();
    }
}
