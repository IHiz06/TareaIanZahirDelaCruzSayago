package com.codigo.msretrofit.service;

import com.codigo.msretrofit.aggregates.response.ResponseSunat;

import java.io.IOException;

public interface PersonaJuridicaService {
    ResponseSunat getInfoSunat(String ruc) throws IOException;
}
