/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import model.Propertytypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.PropertyTypesService;

/**
 *
 * @author alexi
 */
@RestController
@RequestMapping(value = "/propType", produces = MediaTypes.HAL_JSON_VALUE) 
public class PropertyTypesController {
    
    @Autowired
    PropertyTypesService propTypeService;
    
    // Get All PropertyTypes
    @GetMapping("")
    public ResponseEntity<List<Propertytypes>> getAll() {
        List<Propertytypes> propTypes = propTypeService.findAll();
        if(propTypes.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok(propTypes);
    }
    
    // Get a PropertyType details based on their ID
    @GetMapping("/{id}")
    public ResponseEntity<Propertytypes> one(@PathVariable Integer id) {
      Propertytypes propTypes = propTypeService.findOne(id);
      if(null == propTypes)
          return new ResponseEntity(HttpStatus.NOT_FOUND);

      return ResponseEntity.ok(propTypes);
    }
    
}
