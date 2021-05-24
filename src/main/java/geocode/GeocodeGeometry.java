/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author alexi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodeGeometry {
    @JsonProperty("location")
    GeocodeLocation geocodeLocation;
    public GeocodeGeometry() {
    }
    public GeocodeLocation getGeocodeLocation() {
        return geocodeLocation;
    }
    public void setGeocodeLocation(GeocodeLocation geocodeLocation) {
        this.geocodeLocation = geocodeLocation;
    }
}