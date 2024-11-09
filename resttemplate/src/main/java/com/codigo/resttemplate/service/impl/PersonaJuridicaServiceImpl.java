package com.codigo.resttemplate.service.impl;

import com.codigo.resttemplate.Util.Util;
import com.codigo.resttemplate.agreggates.constants.Constants;
import com.codigo.resttemplate.agreggates.response.ResponseSunat;
import com.codigo.resttemplate.redis.RedisService;
import com.codigo.resttemplate.service.PersonaJuridicaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
@Service
public class PersonaJuridicaServiceImpl implements PersonaJuridicaService {
    private final RedisService redisService;
    private final RestTemplate restTemplate;

    public PersonaJuridicaServiceImpl(RedisService redisService, RestTemplate restTemplate) {
        this.redisService = redisService;
        this.restTemplate = restTemplate;
    }

    @Value("${token.api}")
    private String token;

    @Override
    public ResponseSunat getInfoSunat(String ruc) {
        ResponseSunat datosSunat = new ResponseSunat();
        String redisInfo = redisService.getDataFromRedis(Constants.REDIS_KEY_API_SUNAT +ruc);
        if(Objects.nonNull(redisInfo)){
            datosSunat = Util.convertirDesdeString(redisInfo, ResponseSunat.class);
            return datosSunat;
        }else {
            //Si no existe en redis-me voy a Sunat api
            datosSunat = executeRestTemplate(ruc);
            //Convertir a String para poder guardarlo en Redis
            String dataForRedis = Util.convertirAString(datosSunat);

            redisService.saveInRedis(Constants.REDIS_KEY_API_SUNAT +ruc, dataForRedis, Constants.REDIS_ITL);
            return datosSunat;
        }
    }

    private ResponseSunat executeRestTemplate(String ruc){
        //COnfigurar una URL Completa como String
        String url = "https://api.apis.net.pe/v2/sunat/ruc/full?numero="+ruc;
        //Genero mi Client RestTemplate y ejecuto
        ResponseEntity<ResponseSunat> executeRestTemplate = restTemplate.exchange(
                url, //URL a la cual vas a ejecutar
                HttpMethod.GET, //Tipo de solicitud al que perteneces la URL
                new HttpEntity<>(createHeaders()), //cabeceras || headers
                ResponseSunat.class //response a castear
        );
        if(executeRestTemplate.getStatusCode().equals(HttpStatus.OK)){
            return executeRestTemplate.getBody();
        }else{
            return null;
        }
    }

    private HttpHeaders createHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", Constants.BEARER +token);
        return headers;
    }
}
