/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import controller.PropertiesController;
import model.Properties;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 *
 * @author alexi
 */
@Component
public class PropertiesModelAssembler implements RepresentationModelAssembler<Properties, EntityModel<Properties>> {

  @Override
  public EntityModel<Properties> toModel(Properties properties) {
    Link link = WebMvcLinkBuilder.linkTo(methodOn(PropertiesController.class).one(properties.getId())).withSelfRel(); 
    Link link1 = WebMvcLinkBuilder.linkTo(methodOn(PropertiesController.class).all()).withRel("all_properties");

    return EntityModel.of(properties, link, link1);
  }
  
  
}
