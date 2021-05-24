/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import model.Propertytypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import repository.PropertyTypesRepository;

/**
 *
 * @author alexi
 */
@Service
public class PropertyTypesService {
    
    @Autowired
    private PropertyTypesRepository propTypeRepo;
    
    @Cacheable("propTypes_one")
    public Propertytypes findOne(Integer id){
        return propTypeRepo.findById(id).get();
    }
    
    public List<Propertytypes> findAll(){
        return (List<Propertytypes>) propTypeRepo.findAll();
    }
    
    public long count(){
        return propTypeRepo.count();
    }
    
    @Cacheable("propTypes_delete")
    public void deleteByID(Integer vaccID){
        propTypeRepo.deleteById(vaccID);
    }
    
    public void saveAgent(Propertytypes pt){
        propTypeRepo.save(pt);
    } 
    
}
