/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import model.Agents;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import repository.AgentsRepository;

/**
 *
 * @author alexi
 */
@Service
public class AgentsService {
        
    @Autowired
    private AgentsRepository agentsRepo;
    
    @Cacheable("agents_one")
    public Agents findOne(Long id){
        //return agentsRepo.findById(id).get();
        
        // Try catch is used to simulate backend call and check if the request is using cached 
        // It is the only method that has this try catch, the rest will implement cached annotations as normal
        try
        {
            System.out.println("Going to sleep for 5 Secs.. to simulate backend call.");
            Thread.sleep(1000*5);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
 
        return agentsRepo.findById(id).get();
    }
    
    
    public List<Agents> findAll(){
        return (List<Agents>) agentsRepo.findAll();
    }
    

    public long count(){
        return agentsRepo.count();
    }
    
    @Cacheable("agents_delete")
    public void deleteByID(long vaccID){
        agentsRepo.deleteById(vaccID);
    }
    
    public void saveAgent(Agents a){
        agentsRepo.save(a);
    } 
    
    @Cacheable("agents_custom")
    public List<Agents> findAgentByAgentIDInProperties(Long id){
       return agentsRepo.getAgentsByPropertiesID(id);
    }
   
}



