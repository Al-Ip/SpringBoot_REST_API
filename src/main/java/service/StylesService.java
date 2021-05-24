/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import model.Styles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import repository.StylesRepository;

/**
 *
 * @author alexi
 */
@Service
public class StylesService {
    
    @Autowired
    private StylesRepository styleRepo;
    
    @Cacheable("styles_one")
    public Styles findOne(Integer id){
        return styleRepo.findById(id).get();
    }
    
    public List<Styles> findAll(){
        return (List<Styles>) styleRepo.findAll();
    }
    
    public long count(){
        return styleRepo.count();
    }
    
    @Cacheable("styles_delete")
    public void deleteByID(Integer vaccID){
        styleRepo.deleteById(vaccID);
    }
    
    public void saveAgent(Styles s){
        styleRepo.save(s);
    } 
    
}
