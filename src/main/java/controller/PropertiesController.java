/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import assembler.PropertiesModelAssembler;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.PropertiesNotFoundException;
import geocode.GeocodeResult;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import model.Agents;
import model.Properties;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.PropertiesService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import qrCodes.QRCodeUtil;
import service.AgentsService;

/**
 *
 * @author alexi
 */
@RestController
public class PropertiesController {

    @Autowired
    PropertiesService propService;

    @Autowired
    AgentsService ageService;

    PropertiesModelAssembler assembler; 
    Logger logger = LoggerFactory.getLogger(PropertiesController.class);

    public PropertiesController(PropertiesService propService, AgentsService ageService, PropertiesModelAssembler assembler) {
        this.propService = propService;
        this.ageService = ageService;
        this.assembler = assembler;
    }

    // Get individual
    @GetMapping(value = "properties/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Properties> one(@PathVariable Long id) {
        Properties properties = propService.findOne(id);
        if (null == properties) {
            throw new PropertiesNotFoundException(id);
        }

        return assembler.toModel(properties);
    }

    // Get All
    @GetMapping(value = "properties/all", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<Properties> all() {
        List<Properties> properties = propService.findAll();
        properties.stream().map(p -> {
            long id = p.getId();
            Link selfLink = linkTo(PropertiesController.class).slash("properties").slash(id).withSelfRel();
            p.add(selfLink);
            return p;
        }).filter(p -> (propService.findAll().size() > 0)).forEachOrdered(p -> {
            Link drilldownLink = linkTo(methodOn(PropertiesController.class).drilldownOnSecondLink(p.getId())).withRel("drilldown_info");
            p.add(drilldownLink);
        });

        Link selfLink = linkTo(methodOn(PropertiesController.class).all()).withSelfRel();
        CollectionModel<Properties> result = CollectionModel.of(properties, selfLink);

        return result;
    }

    // Get second custom link
    @GetMapping(value = "properties/drilldown/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Map> drilldownOnSecondLink(@PathVariable Long id) {
        try {
            Map<String, Object> model = new HashMap<>();
            Properties property = propService.findOne(id);

            if (property != null) {
                Agents agent = ageService.findOne(Long.valueOf(property.getAgent_Id()));
                model.put("agents_email", agent.getEmail());
                model.put("price", property.getPrice());
                model.put("city", property.getCity());
                model.put("street", property.getStreet());
            }
            return ResponseEntity.ok(model);
        } catch (Exception ex) {
            logger.error(ex.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Post new Property
    @PostMapping(value = "properties/new", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> newProperty(@RequestBody Properties newProperty) {
        try {
            EntityModel<Properties> entityModel = assembler.toModel(propService.saveProperty(newProperty));
            return ResponseEntity
                    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (Exception ex) {
            logger.error(ex.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Put previously existing property
    @PutMapping(value = "properties/update/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Properties>> replaceProperty(@RequestBody Properties updateProperty, @PathVariable Long id) {
        try {
            updateProperty.setId(id);
            propService.saveProperty(updateProperty);
            EntityModel<Properties> entityModel = assembler.toModel(updateProperty);
            return ResponseEntity
                    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (Exception ex) {
            logger.error(ex.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a property
    @DeleteMapping(value = "properties/delete/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {
        try {
            propService.deleteByID(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            logger.error(ex.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Map display for specified property
    @RequestMapping(path = "properties/map", method = RequestMethod.GET)
    public ResponseEntity<String> getGeocodeMap(@RequestParam Long address) throws IOException {
        try {
            Properties prop = propService.findOne(address);
            String propAddress = prop.getStreet() + " " + prop.getCity();

            OkHttpClient client = new OkHttpClient();
            String encodedAddress = URLEncoder.encode(propAddress, "UTF-8");
            Request request = new Request.Builder()
                    .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?language=en&address=" + encodedAddress)
                    .get()
                    .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", ""/*  Use your API Key here */)
                    .build();
            ResponseBody responseBody = client.newCall(request).execute().body();
            ObjectMapper objectMapper = new ObjectMapper();
            GeocodeResult result = objectMapper.readValue(responseBody.string(), GeocodeResult.class);

            String html_page
                    = "<html>\n"
                    + "<head>"
                    + "<title>Map Address</title>"
                    + "<style type=\"text/css\">"
                    + "#map {"
                    + "height: 600px;"
                    + "width: 100%;"
                    + "}"
                    + "</style>"
                    + "<script>"
                    + "function initMap() {"
                    + "var address = document.getElementById(\'address\').innerHTML;"
                    + "var lat = " + result.getResults().get(0).getGeometry().getGeocodeLocation().getLatitude() + ";"
                    + "var lng = " + result.getResults().get(0).getGeometry().getGeocodeLocation().getLongitude() + ";"
                    + "const loc = {lat, lng};"
                    + "const map = new google.maps.Map(document.getElementById(\"map\"), {"
                    + "zoom: 4,"
                    + "center: loc"
                    + "});"
                    + "const marker = new google.maps.Marker({"
                    + "title: \"Click to zoom\","
                    + "position: loc,"
                    + "map: map"
                    + "});"
                    + "const infowindow = new google.maps.InfoWindow({"
                    + "content: address"
                    + "});"
                    + "map.addListener(\"center_changed\", () => {"
                    + "window.setTimeout(() => {"
                    + "map.panTo(marker.getPosition());"
                    + "}, 5000);"
                    + "});"
                    + "marker.addListener(\"click\", () => {"
                    + "map.setZoom(17);"
                    + "map.setCenter(marker.getPosition());"
                    + "infowindow.open(marker.get(\"map\"), marker);"
                    + "});"
                    + "}"
                    + "</script>"
                    + "</head>\n "
                    + "<body>"
                    + "<h1 id=\"address\">" + result.getResults().get(0).getFormattedAddress() + "</h1>"
                    + "<hr>"
                    + "<h4>Click on marker to view address</h4>"
                    + "<div id=\"map\"></div>"
                    + "<script\n"
                    + "src=\"https://maps.googleapis.com/maps/api/js?key=&callback=initMap&libraries=&v=weekly\" async>"
                    + "</script>"
                    + "</body>\n"
                    + "</html>";

            return ResponseEntity.ok(html_page);
        } catch (IOException ex) {
            logger.error(ex.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // QR Code of realtor 
    @GetMapping(value = "properties/qr/{propID}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> qrBarcode(@PathVariable("propID") Long propID) throws Exception {
        try {
            int agentID = propService.findOne(propID).getAgent_Id();
            return okResponse(QRCodeUtil.generateQRCodeImage(ageService.findOne(Long.valueOf(agentID))));
        } catch (Exception ex) {
            logger.error(ex.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<BufferedImage> okResponse(BufferedImage image) {
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    // Get image of property based on specified header request
    @GetMapping("properties/picture/{headerType}/{id}")
    public ResponseEntity<byte[]> picture(@PathVariable String headerType, @PathVariable Long id) throws IOException {
        try {
            HttpHeaders headers = new HttpHeaders();
            List<String> list = new ArrayList<>();
            Properties prop = propService.findOne(id);

            if (headerType.equals("large")) {
                String photoID = prop.getPhoto();
                photoID = photoID.substring(0, photoID.lastIndexOf('.'));
                File[] imagesFound = new File("src/main/resources/static/assets/images/properties/large/" + photoID).listFiles();

                //creating byteArray stream, make it bufforable and passing this buffor to ZipOutputStream
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
                ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

                if (imagesFound != null) {
                    for (File file : imagesFound) {
                        String filename = file.getName().toLowerCase();
                        list.add(filename);

                        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                        try ( FileInputStream fileInputStream = new FileInputStream(file)) {
                            IOUtils.copy(fileInputStream, zipOutputStream);
                        }
                        zipOutputStream.closeEntry();
                    }
                    if (zipOutputStream != null) {
                        zipOutputStream.finish();
                        zipOutputStream.flush();
                        IOUtils.closeQuietly(zipOutputStream);
                    }
                    IOUtils.closeQuietly(bufferedOutputStream);
                    IOUtils.closeQuietly(byteArrayOutputStream);
                }
                MultiValueMap<String, String> mapHeaders = new LinkedMultiValueMap<>();
                mapHeaders.add("Content-Type", "application/octet-stream");
                mapHeaders.add("Content-Disposition", "attachment; filename=\"PropertyID_" + id + ".zip\"");
                return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), mapHeaders, HttpStatus.OK);
            } else if (headerType.equals("thumb")) {
                File imagesFound = new File("src/main/resources/static/assets/images/properties/thumbs/");
                for (File file : imagesFound.listFiles()) {
                    String filename = file.getName().toLowerCase();
                    if (filename.equals(prop.getPhoto()) && filename.endsWith(".jpg")) {
                        list.add(filename);
                    }
                }
                ClassPathResource path = new ClassPathResource("static/assets/images/properties/thumbs/" + list.get(0));
                byte[] bytes = StreamUtils.copyToByteArray(path.getInputStream());
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            }
            return null;
        } catch (IOException ex) {
            logger.error(ex.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
