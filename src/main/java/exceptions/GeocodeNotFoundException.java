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
public class GeocodeNotFoundException extends RuntimeException {

  public GeocodeNotFoundException(String address) {
    super("Could not find address: " + address);
  }
  
}
