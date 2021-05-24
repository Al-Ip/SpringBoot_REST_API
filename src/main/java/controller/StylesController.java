/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import model.Styles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.StylesService;

/**
 *
 * @author alexi
 */
@RestController
@RequestMapping(value = "/styles", produces = MediaTypes.HAL_JSON_VALUE) 
public class StylesController {
    
    @Autowired
    StylesService styleService;
    
    // Get All Styles
    @GetMapping("")
    public ResponseEntity<List<Styles>> getAll() {
        List<Styles> styles = styleService.findAll();
        if(styles.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok(styles);
    }
    
    // Get an Styles details based on their ID
    @GetMapping("/{id}")
    public ResponseEntity<Styles> one(@PathVariable Integer id) {
      Styles styles = styleService.findOne(id);
      if(null == styles)
          return new ResponseEntity(HttpStatus.NOT_FOUND);

      return ResponseEntity.ok(styles);
    }
    
}
