package com.codigo.msretrofit.service.impl;

import com.codigo.msretrofit.Util.Util;
import com.codigo.msretrofit.aggregates.constants.Constants;
import com.codigo.msretrofit.aggregates.response.ResponseSunat;
import com.codigo.msretrofit.redis.RedisService;
import com.codigo.msretrofit.retrofit.ClientSunatService;
import com.codigo.msretrofit.retrofit.impl.ClientSunatServiceImpl;
import com.codigo.msretrofit.service.PersonaJuridicaService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

@Service
@Log4j2
public class PersonaJuridicaServiceImpl implements PersonaJuridicaService {
    private final RedisService redisService;

    public PersonaJuridicaServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    ClientSunatService sunatServiceRetrofit = ClientSunatServiceImpl
            .getRetrofit()
            .create(ClientSunatService.class);

    @Value("${token.api}")
    private String token;

    @Override
    public ResponseSunat getInfoSunat(String ruc) throws IOException {
        ResponseSunat datosSunat = new ResponseSunat();

        // Validar si la información ya existe en Redis
        String redisInfo = redisService.getDataFromRedis(Constants.REDIS_KEY_API_SUNAT + ruc);
        if (Objects.nonNull(redisInfo)) {
            datosSunat = Util.convertirDesdeString(redisInfo, ResponseSunat.class);
            return datosSunat;
        } else {
            // Si no existe en Redis, consulta la API de Sunat
            Call<ResponseSunat> clientRetrofit = prepareSunatRetrofit(ruc);
            log.info("getInfoSunat -> Se preparó todo el cliente Retrofit, listo para ejecutar!");

            Response<ResponseSunat> executionSunat = clientRetrofit.execute();
            log.info("getInfoSunat -> Cliente Retrofit ejecutado");

            if (executionSunat.isSuccessful() && Objects.nonNull(executionSunat.body())) {
                datosSunat = executionSunat.body();
                log.info("getInfoSunat -> Valores del Body: " + datosSunat.toString());

                // Convertir a String para poder guardarlo en Redis
                String dataForRedis = Util.convertirAString(datosSunat);
                redisService.saveInRedis(Constants.REDIS_KEY_API_SUNAT + ruc, dataForRedis, Constants.REDIS_ITL);
                return datosSunat;
            } else {
                log.error("Error en la respuesta de la API de Sunat: " + executionSunat.errorBody());
                throw new IOException("Error en la respuesta de la API de Sunat");
            }
        }
    }

    private Call<ResponseSunat> prepareSunatRetrofit(String ruc){
        String tokenComplete = Constants.BEARER+token;
        log.info("prepareSunatRetrofit -> Ejecutando Metodo de Apoyo");
        return sunatServiceRetrofit.getInfoSunat(tokenComplete, ruc);
    }
}
