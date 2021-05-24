/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import model.Garagetypes;
import model.Styles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.GarageTypesService;

/**
 *
 * @author alexi
 */
@RestController
@RequestMapping(value = "/garageTypes", produces = MediaTypes.HAL_JSON_VALUE) 
public class GarageTypesController {
    
    @Autowired
    GarageTypesService garageTypesService;
    
    // Get All GarageTypes
    @GetMapping("")
    public ResponseEntity<List<Garagetypes>> getAll() {
        List<Garagetypes> garageTypes = garageTypesService.findAll();
        if(garageTypes.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok(garageTypes);
    }
    
    // Get an GarageTypes details based on their ID
    @GetMapping("/{id}")
    public ResponseEntity<Garagetypes> one(@PathVariable Long id) {
      Garagetypes garageTypes = garageTypesService.findOne(id);
      if(null == garageTypes)
          return new ResponseEntity(HttpStatus.NOT_FOUND);

      return ResponseEntity.ok(garageTypes);
    }
    
}
