package com.codigo.resttemplate.controller;

import com.codigo.resttemplate.agreggates.response.ResponseSunat;
import com.codigo.resttemplate.service.PersonaJuridicaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/resttemplate")
public class PersonaJuridicaController {
    private final PersonaJuridicaService personaJuridicaService;

    public PersonaJuridicaController(PersonaJuridicaService personaJuridicaService) {
        this.personaJuridicaService = personaJuridicaService;
    }
    @GetMapping("/sunat/{ruc}")
    public ResponseEntity<ResponseSunat> getInfoReniec(@PathVariable String ruc){
        return new ResponseEntity<>(personaJuridicaService.getInfoSunat(ruc), HttpStatus.OK);
    }
}
