package com.mateusz113.shop.model;

import java.io.Serializable;

public record User(
        String id,
        String firstName,
        String lastName,
        String email
) implements Serializable {
}
