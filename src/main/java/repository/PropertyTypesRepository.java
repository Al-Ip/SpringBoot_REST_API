/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import model.Propertytypes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author alexi
 */
@Repository
public interface PropertyTypesRepository extends CrudRepository<Propertytypes, Integer> {
    
}
