package com.mateusz113.shop.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class that holds order information.
 *
 * @param userId ID of the user that placed the order.
 * @param placementTime time when the order was placed.
 * @param products products that were bought.
 */
public record Order(
        String userId,
        LocalDateTime placementTime,
        List<Product> products
) implements Serializable {
}
