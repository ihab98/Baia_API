package ma.geomatic.GeocodageJava.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix= "na.resources.temp")
public class NetworkAnalysisTempResourcesProperties {
	
	private String location;
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
}
