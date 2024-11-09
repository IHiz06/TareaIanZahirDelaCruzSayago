package com.codigo.msopenfeign.service;

import com.codigo.msopenfeign.aggregates.response.ResponseSunat;

public interface PersonaJuridicaService {
    //ResponseSunat guardar(String ruc);
    ResponseSunat getInfoSunat(String ruc);
}
