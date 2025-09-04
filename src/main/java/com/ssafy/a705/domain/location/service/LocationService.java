package com.ssafy.a705.domain.location.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.a705.domain.location.dto.response.LocationInfoRes;
import com.ssafy.a705.domain.location.entity.Location;
import com.ssafy.a705.domain.location.exception.FileIOException;
import com.ssafy.a705.domain.location.repository.LocationRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String SGG_URL = "geo/sgg.geojson";
    private static final String[] sggProps = {"ADMIN_NAME", "SIDO_NAME", "ADMIN_CODE"};

    @Transactional
    public void saveLocations() {
        List<LocationInfoRes> sggList = getLocations(SGG_URL, sggProps);
        List<Location> locations = extractLocations(sggList);

        for (Location location : locations) {
            Optional<Location> loc = locationRepository.findByCode(location.getCode());
            loc.ifPresentOrElse(
                    value -> value.updateName(location.getName()),
                    () -> locationRepository.save(location)
            );
        }
    }

    private List<Location> extractLocations(List<LocationInfoRes> sggList) {
        List<Location> locations = new ArrayList<>();

        for (LocationInfoRes sgg : sggList) {
            validateName(sgg.sidoName());

            if (isJeju(sgg.sidoName())) {
                String code = sgg.adminCode().substring(0, 2) + "000";
                locations.add(convertLocation(sgg.sidoName(), code));
            } else if (isCity(sgg.sidoName(), sgg.adminName())) {
                locations.add(convertLocation(sgg.adminName(), sgg.adminCode()));
            } else {
                String name;
                if (Objects.equals(sgg.sidoName(), "null")) {
                    name = "강원특별자치도 " + sgg.adminName();
                } else {
                    name = sgg.sidoName() + " " + sgg.adminName();
                }
                locations.add(convertLocation(name, sgg.adminCode()));
            }
        }

        return locations;
    }

    private void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new FileIOException();
        }
    }

    private boolean isJeju(String sidoName) {
        return Objects.equals(sidoName, "제주특별자치도");
    }

    private boolean isCity(String sidoName, String adminName) {
        return Objects.equals(sidoName, adminName);
    }

    private Location convertLocation(String adminName, String adminCode) {
        Integer code = Integer.parseInt(adminCode);
        return Location.of(adminName, code);
    }

    private List<LocationInfoRes> getLocations(String url, String[] props) {
        JsonNode json = getJson(url);
        return parseJson(json, props);
    }

    private JsonNode getJson(String fileUrl) {
        try {
            ClassPathResource resource = new ClassPathResource(fileUrl);
            InputStream is = resource.getInputStream();
            JsonNode root = mapper.readTree(is);
            return root.get("features");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FileIOException();
        }
    }

    private List<LocationInfoRes> parseJson(JsonNode json, String[] args) {
        List<LocationInfoRes> locations = new ArrayList<>();

        if (json.isArray()) {
            for (JsonNode feature : json) {
                JsonNode props = feature.get("properties");
                if (props != null) {
                    String adminName = props.get(args[0]).asText();
                    String sidoName = props.get(args[1]).asText();
                    String adminCode = props.get(args[2]).asText();

                    locations.add(LocationInfoRes.of(adminName, sidoName, adminCode));
                }

            }
        }

        return locations;
    }

}
