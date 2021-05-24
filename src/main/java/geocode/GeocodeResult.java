/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodeResult {
    List<GeocodeObject> results;
    String status;
    
    public GeocodeResult() {
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<GeocodeObject> getResults() {
        return results;
    }
    public void setResults(List<GeocodeObject> results) {
        this.results = results;
    }
}