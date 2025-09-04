package com.ssafy.a705.domain.location._color.dto.response;

import com.ssafy.a705.domain.location._color.entity.LocationColor;
import java.util.List;

public record MapColorRes(
        List<LocationColorRes> colors
) {

    public static MapColorRes from(List<LocationColor> colors) {
        return new MapColorRes(
                colors.stream()
                        .map(LocationColorRes::from)
                        .toList()
        );
    }

}
