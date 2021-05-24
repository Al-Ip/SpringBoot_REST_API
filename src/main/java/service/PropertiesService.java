/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import model.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import repository.PropertiesRepository;

/**
 *
 * @author alexi
 */
@Service
public class PropertiesService {
        
    @Autowired
    private PropertiesRepository propRepo;
    
    @Cacheable("properties_one")
    public Properties findOne(Long id){
        return propRepo.findById(id).get();
    }
    
    public List<Properties> findAll(){
        return (List<Properties>) propRepo.findAll();
    }
    
    public long count(){
        return propRepo.count();
    }
    
    @Cacheable("properties_delete")
    public void deleteByID(long vaccID){
        propRepo.deleteById(vaccID);
    }
    
    public Properties saveProperty(Properties a){
        return propRepo.save(a);
    } 
    
    public void getSecondPropertiesLink(){
        
    }
}
