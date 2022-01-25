package ma.geomatic.GeocodageJava.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ma.geomatic.GeocodageJava.helper.PointCsv;
import ma.geomatic.GeocodageJava.service.GeocodageService;

@RestController
public class GeocodageController {

	@Autowired
	private GeocodageService geocodageScriptService;
	
	@PostMapping("/v1/revgeocodingBatch/{radius}")
	public ResponseEntity<String> runGeocodageScript(@PathVariable Integer radius, @RequestBody ArrayList<PointCsv> pointCsvList) {
		
		if(radius < 0) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Radius cannot be negative");
		}
		
		
		try {
			geocodageScriptService.convertListToCsv(pointCsvList);
			geocodageScriptService.runGeocodageScript(radius);
			geocodageScriptService.cleanupGeocodageResourcesDirectory();
			return new ResponseEntity<String>(geocodageScriptService.convertGeocodageOutputFileToString(), HttpStatus.OK);
					
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.contentLength(0)
					.body(null);
		}
		
	}
	
	@PostMapping("/v1/revgeocodingBatch")
	public ResponseEntity<String> runGeocodageScript(@RequestBody ArrayList<PointCsv> pointCsvList) {
		
		Integer radius = 25;
		try {
			geocodageScriptService.convertListToCsv(pointCsvList);
			geocodageScriptService.runGeocodageScript(radius);
			geocodageScriptService.cleanupGeocodageResourcesDirectory();
			return new ResponseEntity<String>(geocodageScriptService.convertGeocodageOutputFileToString(), HttpStatus.OK);
					
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.contentLength(0)
					.body(null);
		}
		
	}
	
	@GetMapping("/v1/test")
	public String test() {
		return "ok";
	}
	
	
}
