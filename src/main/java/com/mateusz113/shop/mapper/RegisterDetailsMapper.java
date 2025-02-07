package com.mateusz113.shop.mapper;

import com.mateusz113.shop.auth.LoginDetails;
import com.mateusz113.shop.auth.RegisterDetails;

public class RegisterDetailsMapper {
    /**
     * Maps {@code RegisterDetails} to {@code LoginDetails}.
     *
     * @param registerDetails {@code RegisterDetails} to be mapped.
     * @return {@code LoginDetails} object from provided info.
     */
    public static LoginDetails toLoginDetails(RegisterDetails registerDetails) {
        return (registerDetails == null) ? null : new LoginDetails(registerDetails.email(), registerDetails.password());
    }
}
