package com.mateusz113.shop.model;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
        String userId,
        LocalDateTime placementTime,
        List<Product> products
) {
}
