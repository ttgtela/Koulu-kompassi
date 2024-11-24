package com.jmnt.data;

import com.jmnt.tools.UniTools;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class NominatimApiCaller {
    private final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private long lastRequestTime = 0;

    public Place[] searchLocation(String location) {
        RestTemplate restTemplate = new RestTemplate();
        Place[] places = new Place[0];
        try {
            long currentTime = System.currentTimeMillis();
            long timeSinceLastRequest = currentTime - lastRequestTime;
            if (timeSinceLastRequest < 1000) {
                // Sleep for 1 second on total because of Nominatim API usage policy (max 1 request per second).
                Thread.sleep(1000 - timeSinceLastRequest);
            }

            lastRequestTime = System.currentTimeMillis();

            String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_URL)
                    .queryParam("q", URLEncoder.encode(UniTools.normalizeString(location), StandardCharsets.UTF_8.toString()))
                    .queryParam("format", "json")
                    .toUriString();
            //System.out.println("Request URL: " + url);
            places = restTemplate.getForObject(url, Place[].class);
        } catch (Exception e) {
            throw new RuntimeException("Error making nominatim API call", e);
        }
        if (places.length == 0) {
            System.out.println("No places found for " + location);
        }

        return places;
    }
}
