package com.mateusz113.shop.model;

public record User(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
