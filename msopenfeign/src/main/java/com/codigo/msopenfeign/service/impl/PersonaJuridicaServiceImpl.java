package com.codigo.msopenfeign.service.impl;

import com.codigo.msopenfeign.Util.Util;
import com.codigo.msopenfeign.aggregates.constants.Constants;
import com.codigo.msopenfeign.aggregates.response.ResponseSunat;
import com.codigo.msopenfeign.client.ClientSunat;
import com.codigo.msopenfeign.redis.RedisService;
import com.codigo.msopenfeign.service.PersonaJuridicaService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class PersonaJuridicaServiceImpl implements PersonaJuridicaService {
    private final RedisService redisService;
    private final ClientSunat clientSunat;

    public PersonaJuridicaServiceImpl(RedisService redisService, ClientSunat clientSunat) {
        this.redisService = redisService;
        this.clientSunat = clientSunat;
    }

    @Value("${token.api}")
    private String token;

    @Override
    public ResponseSunat getInfoSunat(String ruc) {
        ResponseSunat datosSunat = new ResponseSunat();
        //Validar info en redis

        String redisInfo = redisService.getDataFromRedis(Constants.REDIS_KEY_API_SUNAT + ruc);
        if (Objects.nonNull(redisInfo)) {
            datosSunat = Util.convertirDesdeString(redisInfo, ResponseSunat.class);
            return datosSunat;
        } else {
            //Si no existe en redis-me voy a Sunat api
            datosSunat = executionSunatJ(ruc);
            //Convertir a String para poder guardarlo en Redis
            String dataForRedis = Util.convertirAString(datosSunat);

            redisService.saveInRedis(Constants.REDIS_KEY_API_SUNAT + ruc, dataForRedis, Constants.REDIS_ITL);
            return datosSunat;
        }
    }

    private ResponseSunat executionSunatJ(String ruc) {
        String tokenO = Constants.BEARER + token;
        try {
            return clientSunat.getPersonaJuridicaReniec(ruc, tokenO);
        } catch (FeignException e) {
            System.err.println("Error en la API externa: " + e.getMessage());
            return null;
        }

    }

}