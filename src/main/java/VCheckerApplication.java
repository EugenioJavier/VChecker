import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import model.RespMavenCentral;
import model.doc;

import org.springframework.web.client.RestTemplate;


public class VCheckerApplication {
	
	//parámetros de la aplicación. El nombre del fichero a procesar
    public static void main(String[] args) {
        
    	boolean resultado=false;
    	
    	
    	String fichero="./"+args[0];
    	resultado=ComprobarVersiones(fichero);
    	if(resultado){
    		System.out.println("la versión es correcta");
    	}else{
    		System.out.println("la versión no es correcta");
    	}    	
    }

	private static boolean ComprobarVersiones(String fichero) {
		boolean resultado=false;
		//construimos el FileReader
		File fich=null;
		FileReader fr=null;
		BufferedReader br=null;
		try {
			fich=new File(fichero);
			fr = new FileReader(fich);
			br=new BufferedReader(fr);
			
			//lectura del fichero
			String linea;
			
			String delimitador="#";
			//mientras haya lineas en el fichero
			while ((linea=br.readLine())!=null) {
				String[]compLineas=linea.split(delimitador);
				//nombreFichero|version|url		
				//comprobamos a que repositorio nos vamos a conectar
				if(compLineas[2].equals("search.maven.org")){
					resultado=ComprobarConMaven(compLineas);
				}else{
					resultado=ComprobarConArchiva(compLineas);
				}					
			}			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
	         // Cerramos el fichero
	         try{                    
	            if( null != fr ){ 
	               fr.close();
	               br.close();
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
		
		return resultado;
	}

	private static boolean ComprobarConArchiva(String[] compLineas) {
		// TODO Auto-generated method stub
//		Componemos la url
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("artifact", compLineas[0]);
		vars.put("url",compLineas[2]);
		String url="http://{url}:8080/restServices/browseService/searchArtifacts/{artifact}";
		try {
			URI uri=new URI(url);
			RestTemplate resttemplate=new RestTemplate();
			String respuesta=resttemplate.getForObject(uri, String.class);
			System.out.println(respuesta);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		
		/**
		  * Search artifacts with any property matching text. If repository is not provided the search runs in all
		  * repositories. If exact is true only the artifacts whose property match exactly are returned.
		  *
		  * @param text
		  * @param repositoryId
		  * @param exact
		  * @return
		  * @throws ArchivaRestServiceException
		  * @since 2.2
		  */
		
		return false;
	}

	private static boolean ComprobarConMaven(String[] compLineas) {
//		Mimics searching by coordinate in Advanced Search.  This search 
//		uses all coordinates (“g” for groupId, “a” for artifactId, “v” for version, 
//				“p” for packaging, “l” for classifier) and uses “AND” to require all 
//				terms by default.  Only one term is required for the search to work.  
//				Terms can also be connected by “OR” separated to make them 
//				optional 
//		http://search.maven.org/solrsearch/select?q=g:”com.google.inject”%20AND%20a:”guice”%20AND%20v:”3.0”%20AND%20l:”javadoc”%20AND%20p:”jar”&rows=20&wt=json
		
//		Componemos la url
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("artifact", compLineas[0]);
		vars.put("version", compLineas[1]);
		StringBuilder strBld=new StringBuilder();
		strBld.append("http://search.maven.org/solrsearch/select?q=g:");
		strBld.append((char)34);
		strBld.append("org.kurento");
		strBld.append((char)34);
		strBld.append(" AND a:");
		strBld.append((char)34);
		strBld.append(compLineas[0]);
		strBld.append((char)34);
		strBld.append(" AND v:");
		strBld.append((char)34);
		strBld.append(compLineas[1]);
		strBld.append((char)34);
		strBld.append(" OR l:");
		strBld.append((char)34);
		strBld.append("javadoc");
		strBld.append((char)34);
		strBld.append(" OR l:");
		strBld.append((char)34);
		strBld.append("jar");
		strBld.append((char)34);
		strBld.append("&rows=20&wt=json");
		
		String ruta=strBld.toString();
		
		try {			
			URL url=new URL(ruta);
			String nullFragment = null;			
			URI uri=new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(),nullFragment );
			RestTemplate resttemplate=new RestTemplate();
			RespMavenCentral res=resttemplate.getForObject(uri, RespMavenCentral.class);			
			
			//################################################################################################
			//en este punto res contiene un objeto RespMavenCentral correcto con lo devuelto por maven central
			//################################################################################################
			if(!(res.getResponse().getNumFound()==0)){
				//comprobamos que el repositorio y la versión son los mismos.
				doc dc=res.getResponse().getDocs().get(0);
				String repo=dc.getA();
				String version=dc.getV();
				if (repo.equals(compLineas[0]) && version.equals(compLineas[1])){
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

