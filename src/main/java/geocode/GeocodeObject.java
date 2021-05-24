/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author alexi
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodeObject {
    @JsonProperty("place_id")
    String placeId;
    @JsonProperty("address_components")
    List<AddressComponent> addressComponents;
    @JsonProperty("formatted_address")
    String formattedAddress;
    GeocodeGeometry geometry;
    public GeocodeObject() {
    }
    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }
    public void setAddressComponents(List<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }
    public String getPlaceId() {
        return placeId;
    }
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
    public String getFormattedAddress() {
        return formattedAddress;
    }
    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }
    public GeocodeGeometry getGeometry() {
        return geometry;
    }
    public void setGeometry(GeocodeGeometry geometry) {
        this.geometry = geometry;
    }
}