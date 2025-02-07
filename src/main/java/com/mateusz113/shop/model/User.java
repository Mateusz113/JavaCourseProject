package com.mateusz113.shop.model;

import java.io.Serializable;

/**
 * Class that holds the information about the user.
 *
 * @param id ID of the user.
 * @param firstName first name of the user.
 * @param lastName last name of the user.
 * @param email email of the user.
 */
public record User(
        String id,
        String firstName,
        String lastName,
        String email
) implements Serializable {
}
