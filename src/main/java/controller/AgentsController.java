/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import exceptions.AgentsNotFoundException;
import java.io.IOException;
import java.util.List;
import model.Agents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.AgentsService;

/**
 *
 * @author alexi
 */
@RestController
@RequestMapping(value = "/agents", produces = MediaTypes.HAL_JSON_VALUE) 
public class AgentsController {
    
    @Autowired
    AgentsService agentsService;
    
    // Started off with the agents controller to get more comfortable with rest before moving onto properties
    // Get All Agents
    @GetMapping("")
    public ResponseEntity<List<Agents>> getAll() {
        List<Agents> agents = agentsService.findAll();
        if(agents.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok(agents);
    }
    
    // Get an Agents details based on their ID
    @GetMapping("/{id}")
    public ResponseEntity<Agents> one(@PathVariable Long id) {
      Agents age = agentsService.findOne(id);
      if(null == age)
          throw new AgentsNotFoundException(id);

      return ResponseEntity.ok(age);
    }
    
    // Get image of Agent based on ID
    @GetMapping("/picture/{id}")
    public ResponseEntity<byte[]> picture(@PathVariable Long id) throws IOException {
  
        Agents age = agentsService.findOne(id);
        if(null == age)
            throw new AgentsNotFoundException(id);

        ClassPathResource path = new ClassPathResource("static/assets/images/agents/"+ age.getAgent_Id() +".jpg");
        byte[] bytes = StreamUtils.copyToByteArray(path.getInputStream());

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }
    
}
