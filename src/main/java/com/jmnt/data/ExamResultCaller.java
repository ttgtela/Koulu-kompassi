package com.jmnt.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class ExamResultCaller {

    private final String VIPUNEN_URL = "https://api.vipunen.fi/api/resources/ytl_arvosanat/data?filter=koe%3D%3D%27%C3%84idinkieli%2C%20suomi%27%20and%20tutkintokertaKoodi%3D%3D%272021K%27%20and%20arvosana%3D%3D%27Eximia%20cum%20laude%20approbatur%27&sort=%28%2Blukio%29&offset=0&";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ExamResultCaller() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public ExamResults[] searchExams() {
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(VIPUNEN_URL))
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), ExamResults[].class);
            } else {
                System.err.println("Failed to fetch data. HTTP Status: " + response.statusCode());
                return new ExamResults[0];
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new ExamResults[0];
        }
    }
}
