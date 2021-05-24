/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author alexi
 */
public class AgentsNotFoundException extends RuntimeException {

  public AgentsNotFoundException(Long id) {
    super("Could not find agent " + id);
  }
  
}

