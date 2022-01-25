package ma.geomatic.GeocodageJava.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.geomatic.GeocodageJava.config.NetworkAnalysisOutputResourcesProperties;
import ma.geomatic.GeocodageJava.config.NetworkAnalysisTempResourcesProperties;

@Service
public class NetworkAnalysisService {
	
	private final Path tempLocation;
	private final Path outputLocation;
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	public NetworkAnalysisService(NetworkAnalysisTempResourcesProperties tempResourcesProperties, NetworkAnalysisOutputResourcesProperties outputResourcesProperties) {
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
	
	public void runNetworkAnalysisScript() throws IOException, InterruptedException {
		String cmd = "cmd.exe /c echo 'activating env...'"
				+ " & activate Baia-env-clone"
				+ " & cmd.exe /c conda env list"
				
				+ " & cmd.exe /c echo 'current dir...'"
				+ " & dir"
				
				+ " & cmd.exe /c echo 'running Network Analysis script...'"
				//+ " & python \"E:\\Projet_GEOMATIC_ALAOUI_1_1_2019\\Projet_Geocodage\\NA_Baia_Tomcat\\NA_Baia\\NA_Baia.py\" "
				+ " & python \"I:\\D_Informatique\\Projets 2021\\Projet Baia\\Geocoding_17-11-2021\\NA_Baia\\NA_BAIA.py\" "
				+ "na\\resources\\temp\\jsonFile.json ";
		
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
	
	@SuppressWarnings("deprecation")
	public void saveBodyToJsonFile(String body) throws IOException {
		File f = new File(this.tempLocation.toString() + "/jsonFile.json");
		FileUtils.writeStringToFile(f, body);
		System.out.println("saved json file in " + f.getAbsolutePath().toString());
	}
	
	public void cleanupNetworkAnalysisResourcesDirectory() throws IOException {
		System.err.println("\nCleaning up...");
    	FileUtils.copyFile(new File("na/resources/temp/service_Na_Baia_python.xml"), new File(outputLocation.toString() + "/output.xml"));
    	utilService.deleteFolderContents(new File("na/resources/temp"));
	}
	
	public String convertNetworkAnalysisOutputFileToString() throws IOException {
		
		return FileUtils.readFileToString(new File("na/resources/output/output.xml"), "UTF-8");
		
	}
	
}
