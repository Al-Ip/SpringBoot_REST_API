/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import model.Garagetypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import repository.GarageTypesRepository;

/**
 *
 * @author alexi
 */

@Service
public class GarageTypesService {
    
    @Autowired
    private GarageTypesRepository garageRepo;
    
    @Cacheable("garageTypes_one")
    public Garagetypes findOne(Long id){
        return garageRepo.findById(id).get();
    }
    
    public List<Garagetypes> findAll(){
        return (List<Garagetypes>) garageRepo.findAll();
    }
    
    public long count(){
        return garageRepo.count();
    }
    
    @Cacheable("garageTypes_delete")
    public void deleteByID(long vaccID){
        garageRepo.deleteById(vaccID);
    }
    
    public void saveAgent(Garagetypes g){
        garageRepo.save(g);
    } 
    
}
