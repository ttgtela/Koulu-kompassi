package com.jmnt.data;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NominatimApiCaller {
    private final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    public Place[] searchLocation(String location) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_URL)
                .queryParam("q", location)
                .queryParam("format", "json")
                .toUriString();

        return restTemplate.getForObject(url, Place[].class);
    }
}
