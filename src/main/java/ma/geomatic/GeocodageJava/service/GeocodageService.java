package ma.geomatic.GeocodageJava.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;

import ma.geomatic.GeocodageJava.config.GeocodageOutputResourcesProperties;
import ma.geomatic.GeocodageJava.config.GeocodageTempResourcesProperties;
import ma.geomatic.GeocodageJava.helper.PointCsv;

@Service
public class GeocodageService {
	
	private final Path tempLocation;
	private final Path outputLocation;
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	public GeocodageService(GeocodageTempResourcesProperties tempResourcesProperties, GeocodageOutputResourcesProperties outputResourcesProperties) {
		this.tempLocation = Paths.get(tempResourcesProperties.getLocation());
		this.outputLocation = Paths.get(outputResourcesProperties.getLocation());
	}
	
	@PostConstruct
	public void init() {
		try {
			Files.createDirectories(tempLocation);
			Files.createDirectories(outputLocation);
		} catch(IOException ioe) {
			throw new RuntimeException("Could not initialize storage location");
		}
	}
	
	public void runGeocodageScript(Integer radius) throws IOException, InterruptedException {
		String cmd = "cmd.exe /c echo 'activating env...'"
				+ " & activate Baia-env-clone"
				+ " & cmd.exe /c conda env list"
				
				+ " & cmd.exe /c echo 'current dir...'"
				+ " & dir"
				
				+ " & cmd.exe /c echo 'running Geocodage script...'"
				+ " & python \"I:\\D_Informatique\\Projets 2021\\Projet Baia\\Geocoding_17-11-2021\\pythonProject1\\main.py\" geocodage\\resources\\temp\\test.csv " + radius.toString() + " geocodage\\resources\\temp";
		
		Process process = Runtime.getRuntime().exec(cmd);
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		System.err.println("\nStarting process...");
		String line;
		while((line = reader.readLine()) != null) {
			System.err.println(line);
		}
		System.err.println("Finished successfully... destroying process\n");
		process.destroy();
	}
	
	public void convertListToCsv(List<PointCsv> pointCsvList) throws Exception {
		CSVWriter writer = new CSVWriter(new FileWriter(tempLocation.toString() + "/test.csv"));
		
		String[] titles = new String[] {"Dh", "lat", "lon"};
		writer.writeNext(titles);
		
	    for (PointCsv pointCsv : pointCsvList) {
	    	String dh = pointCsv.getDh();
	    	String lat = pointCsv.getLat();
	    	String lon = pointCsv.getLon();
	    	String[] array = new String[] {dh, lat, lon};
	        writer.writeNext(array);
	    }	  
	    
	    writer.close();
	}
	
	public void cleanupGeocodageResourcesDirectory() throws IOException {
		System.err.println("\nCleaning up...");
    	FileUtils.copyFile(new File("geocodage/resources/temp/Geocoding.xml"), new File(outputLocation.toString() + "/output.xml"));
    	utilService.deleteFolderContents(new File("geocodage/resources/temp"));
		File info = new File("geocodage/resources/temp/info");
	    info.delete();
	}
	
	public String convertGeocodageOutputFileToString() throws IOException {
		
		return FileUtils.readFileToString(new File("geocodage/resources/output/output.xml"), "UTF-8");
		
	}
	
	
	
	
	
	
}
