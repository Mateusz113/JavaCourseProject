package com.mateusz113.shop.mapper;

import com.mateusz113.shop.auth.LoginDetails;
import com.mateusz113.shop.auth.RegisterDetails;

public class RegisterDetailsMapper {
    public static LoginDetails toLoginDetails(RegisterDetails registerDetails) {
        return (registerDetails == null) ? null : new LoginDetails(registerDetails.email(), registerDetails.password());
    }
}
