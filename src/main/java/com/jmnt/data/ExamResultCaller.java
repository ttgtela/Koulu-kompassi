package com.jmnt.data;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExamResultCaller {
    private final String VIPUNEN_URL = "https://api.vipunen.fi/api/resources/ytl_arvosanat/data?filter=koe%3D%3D%27%C3%84idinkieli%2C%20suomi%27%20and%20tutkintokertaKoodi%3D%3D%272021K%27%20and%20arvosana%3D%3D%27Eximia%20cum%20laude%20approbatur%27&sort=%28%2Blukio%29&offset=0&";

    public ExamResults[] searchExams() {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(VIPUNEN_URL)
                .toUriString();

        return restTemplate.getForObject(url, ExamResults[].class);
    }
}
