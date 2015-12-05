import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import model.RespArchiva;
import model.RespMavenCentral;
import model.doc;

import org.springframework.web.client.RestTemplate;




/**
 * @author Javier Cabezas y Eugenio F. González (eugeniofidel@gmail.com)
 * 
 */
public class VCheckerApplication {	
	//parámetros de la aplicación. El nombre del file a procesar
    public static void main(String[] args) {
        
    	boolean result=false;    	
    	
    	String file="./"+args[0];
    	result=CheckVersions(file);
    	if(result){
    		System.out.println("The version is correct");
    	}else{
    		System.out.println("The version is incorrect");
    	}    	
    }

	private static boolean CheckVersions(String file) {
		boolean result=false;
		//building the FileReader
		File fich=null;
		FileReader fr=null;
		BufferedReader br=null;
		try {
			fich=new File(file);
			fr = new FileReader(fich);
			br=new BufferedReader(fr);
			
			//reading the file
			String line;
			
			String delimiter="#";
			//While we find lines in the file
			while ((line=br.readLine())!=null) {
				String[]compLines=line.split(delimiter);
				//artifact#version#url		
				//Looking for the repository where the artifact must to be
				if(compLines[2].equals("search.maven.org")){
					result=CheckWithMaven(compLines);
				}else{
					result=CheckWithArchiva(compLines);
				}					
			}			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
	         // closing the streams
	         try{                    
	            if( null != fr ){ 
	               fr.close();
	               br.close();
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
		
		return result;
	}

	
	/**
	 * @param compLines, an array of strings artifact#version#Archiva url
	 * @return	boolean that is true if the version of the artifact is in the Archiva url repository
	 */
	private static boolean CheckWithArchiva(String[] compLines) {
		
		//building the url

		String url="http://"+compLines[2]+":8080/restServices/archivaServices/browseService/versionsList/org.kurento/"+compLines[0];
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
				if(structure[0].equals(compLines[1])){
					return true;
				}
			}
			
		} catch (URISyntaxException e) {
			e.printStackTrace();			
		}		
		
		return false;
	}

	/**
	 * @param compLines, an array of strings artifact#version#mavencentral url
	 * @return	boolean that is true if the version of the artifact is in the mavenCentral url repository
	 */
	private static boolean CheckWithMaven(String[] compLines) {
//		Mimics searching by coordinate in Advanced Search.  This search 
//		uses all coordinates (“g” for groupId, “a” for artifactId, “v” for version, 
//				“p” for packaging, “l” for classifier) and uses “AND” to require all 
//				terms by default.  Only one term is required for the search to work.  
//				Terms can also be connected by “OR” separated to make them 
//				optional 
//		http://search.maven.org/solrsearch/select?q=g:”com.google.inject”%20AND%20a:”guice”%20AND%20v:”3.0”%20AND%20l:”javadoc”%20AND%20p:”jar”&rows=20&wt=json
		
//		Componemos la url
		String path="http://search.maven.org/solrsearch/select?q=g:\"org.kurento\" AND a:\""+compLines[0]+"\" AND v:\""+compLines[1]+"\" OR l:\"javadoc\" OR l:\"jar\"&rows=20&wt=json";
		
		try {			
			URL url=new URL(path);
			String nullFragment = null;			
			URI uri=new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(),nullFragment );
			RestTemplate resttemplate=new RestTemplate();
			RespMavenCentral res=resttemplate.getForObject(uri, RespMavenCentral.class);			
			
			//################################################################################################
			// At this point res contains a correct RespArchiva object returned by mavencentral repository
			//################################################################################################
			if(!(res.getResponse().getNumFound()==0)){
				//Checking the version and the artifact are the same.
				doc dc=res.getResponse().getDocs().get(0);
				String repo=dc.getA();
				String version=dc.getV();
				if (repo.equals(compLines[0]) && version.equals(compLines[1])){
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

