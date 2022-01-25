package ma.geomatic.GeocodageJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ma.geomatic.GeocodageJava.service.NetworkAnalysisService;

@RestController
public class NetworkAnalysisController {

	@Autowired
	private NetworkAnalysisService naService;

	
	@PostMapping("/v1/routing")
	public ResponseEntity<String> runNetworkAnalysisScript(@RequestBody com.fasterxml.jackson.databind.JsonNode body) {
		try {
			naService.saveBodyToJsonFile(body.toPrettyString());
			naService.runNetworkAnalysisScript();
			naService.cleanupNetworkAnalysisResourcesDirectory();
			String xmlOutput = naService.convertNetworkAnalysisOutputFileToString();
			return new ResponseEntity<String>(
					xmlOutput,
					HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.contentLength(0)
					.body(null);
		}
		
	}
	
	
	
	
}
