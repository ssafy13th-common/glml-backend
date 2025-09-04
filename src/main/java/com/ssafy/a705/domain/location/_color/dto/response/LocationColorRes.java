package com.ssafy.a705.domain.location._color.dto.response;

import com.ssafy.a705.domain.location._color.entity.LocationColor;

public record LocationColorRes(
        Integer LocationCode,
        String LocationColor
) {

    public static LocationColorRes from(LocationColor locationColor) {
        String color = locationColor.getTransparency() + locationColor.getColor();
        return new LocationColorRes(
                locationColor.getLocation().getCode(),
                color
        );
    }

}
