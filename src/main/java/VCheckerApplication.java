import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import model.RespArchiva;
import model.RespMavenCentral;
import model.art;
import model.artifactList;
import model.doc;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author Javier Cabezas y Eugenio F. González (eugeniofidel@gmail.com)
 * 
 */
public class VCheckerApplication {	
	//Application parameters. The name of the file wich is going to be processed
    public static void main(String[] args) {   	
    	
    	String file="./"+args[0];
    	CheckVersions(file);
    	  	
    }

	private static void CheckVersions(String file) {
		boolean comprobar=false;

		//We get the artifacts and the versions from config.json
		ObjectMapper mapper = new ObjectMapper();			
		artifactList al=null;
		try {
			al = mapper.readValue(new File(file), artifactList.class);
		} catch (JsonParseException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//We construct an iterator with al to process all the artifacts contained in it
		Iterator<art> it=al.getArtifacts().iterator();
				
		while(it.hasNext()){
			art artefacto=it.next();
			//We look at the server attribute to select the function that will process each artifact
			if (artefacto.getServer().equals("search.maven.org")){
				comprobar=CheckWithMaven(artefacto);
			}else{
				comprobar=CheckWithArchiva(artefacto);
			}
			if(!comprobar){
				 System.out.println("The artifact "+artefacto.getArtifact()+", version "+artefacto.getVersion()
						 +" is notlocated in "+artefacto.getServer());
			}else{
				System.out.println("The artifact "+artefacto.getArtifact()+", version "+artefacto.getVersion()
						 +" is located in "+artefacto.getServer());
			}
		}	
		
	}
	/**
	 * @param artefacto that is the artifact object wich we'll check
	 * @return	boolean that is true if the version of the artifact is in the Archiva url repository
	 */
	private static boolean CheckWithArchiva(art artefacto) {
		
		//building the url
		String url="http://"+artefacto.getServer()+":8080/restServices/archivaServices/browseService/versionsList/org.kurento/"+artefacto.getArtifact();
		try {
			URI uri=new URI(url);
			RestTemplate resttemplate=new RestTemplate();
			RespArchiva res=resttemplate.getForObject(uri, RespArchiva.class);
			//################################################################################################
			// At this point res contains a correct RespArchiva object returned by archiva repository
			//################################################################################################
			Iterator<String> it=res.getVersions().iterator();
			while(it.hasNext()){
				String[]structure=it.next().split("-");
				if(structure[0].equals(artefacto.getVersion())){
					return true;
				}
			}
			
		} catch (URISyntaxException e) {
			e.printStackTrace();			
		}		
		
		return false;
	}

	/**
	 * @param artefacto that is the artifact object wich we'll check
	 * @return	boolean that is true if the version of the artifact is in the mavenCentral url repository
	 */
	private static boolean CheckWithMaven(art artefacto) {
		//Mimics searching by coordinate in Advanced Search.  This search 
		//uses all coordinates (“g” for groupId, “a” for artifactId, “v” for version, 
		//		“p” for packaging, “l” for classifier) and uses “AND” to require all 
		//		terms by default.  Only one term is required for the search to work.  
		//		Terms can also be connected by “OR” separated to make them 
		//		optional 
		//http://search.maven.org/solrsearch/select?q=g:”com.google.inject”%20AND%20a:”guice”%20AND%20v:”3.0”%20AND%20l:”javadoc”%20AND%20p:”jar”&rows=20&wt=json
				
		//building the url
		String path="http://search.maven.org/solrsearch/select?q=g:\"org.kurento\" AND a:\""+artefacto.getArtifact()+"\" AND v:\""+artefacto.getVersion()+"\" OR l:\"javadoc\" OR l:\"jar\"&rows=20&wt=json";
		
		try {			
			URL url=new URL(path);
			String nullFragment = null;			
			URI uri=new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(),nullFragment );
			RestTemplate resttemplate=new RestTemplate();
			RespMavenCentral res=resttemplate.getForObject(uri, RespMavenCentral.class);			
			
			//################################################################################################
			// At this point res contains a correct RespMavenCentral object returned by mavencentral repository
			//################################################################################################
			if(!(res.getResponse().getNumFound()==0)){
				//Checking if the version and the artifact are the same.
				doc dc=res.getResponse().getDocs().get(0);
				String repo=dc.getA();
				String version=dc.getV();
				if (repo.equals(artefacto.getArtifact()) && version.equals(artefacto.getVersion())){
					return true;
				}
			}else{
				return false;
			}
							
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}

