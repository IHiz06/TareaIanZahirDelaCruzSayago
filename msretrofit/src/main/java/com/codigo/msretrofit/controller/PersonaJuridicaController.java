package com.codigo.msretrofit.controller;

import com.codigo.msretrofit.aggregates.response.ResponseSunat;
import com.codigo.msretrofit.service.PersonaJuridicaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1/retrofit")
public class PersonaJuridicaController {
    private final PersonaJuridicaService personaJuridicaService;

    public PersonaJuridicaController(PersonaJuridicaService personaJuridicaService) {
        this.personaJuridicaService = personaJuridicaService;
    }


    @GetMapping("/sunat/{ruc}")
    public ResponseEntity<ResponseSunat> getInfoReniec(@PathVariable String ruc) throws IOException {
        return new ResponseEntity<>(personaJuridicaService.getInfoSunat(ruc), HttpStatus.OK);
    }
}
