/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advice;

import exceptions.PropertiesNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PropertiesNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(PropertiesNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String propertyNotFoundHandler(PropertiesNotFoundException ex) {
    return ex.getMessage();
  }
  
}