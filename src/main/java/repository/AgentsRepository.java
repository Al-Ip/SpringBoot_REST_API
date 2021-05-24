/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import model.Agents;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author alexi
 */
@Repository
public interface AgentsRepository extends CrudRepository<Agents, Long> {
    
    @Query("SELECT a FROM Agents a WHERE a.agent_Id = :id")
    public List<Agents> getAgentsByPropertiesID(@Param("id") long id);
   
}
