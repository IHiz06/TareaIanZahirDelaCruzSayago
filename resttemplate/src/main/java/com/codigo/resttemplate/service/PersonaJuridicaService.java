package com.codigo.resttemplate.service;

import com.codigo.resttemplate.agreggates.response.ResponseSunat;

public interface PersonaJuridicaService {
    ResponseSunat getInfoSunat(String ruc);
}
