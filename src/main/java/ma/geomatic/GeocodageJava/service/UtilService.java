package ma.geomatic.GeocodageJava.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public class UtilService {
	
	public void deleteFolderContents(File folder) throws IOException {
		File[] files = folder.listFiles();
	    if(files!=null) {
	        for(File f: files) {
	            if(f.isDirectory()) {
	            	deleteFolderContents(f);
	            } else {	        
	            	f.delete();
	            }
	        }
	    }
	}
	
	
}
