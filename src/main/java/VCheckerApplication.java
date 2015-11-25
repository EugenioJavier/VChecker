import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;





public class VCheckerApplication {
	
	//parámetros de la aplicación. El nombre del fichero a procesar
    public static void main(String[] args) {
        
    	boolean resultado=false;
    	
    	String fichero="./"+args[0];
    	resultado=ComprobarVersiones(fichero);
    	System.out.println(resultado);
    }

	private static boolean ComprobarVersiones(String fichero) {
		//construimos el FileReader
		File fich=null;
		FileReader fr=null;
		try {
			fich=new File(fichero);
			fr = new FileReader(fich);
			BufferedReader br=new BufferedReader(fr);
			
			//lectura del fichero
			String linea;
			boolean resultado=false;
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
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
		
		return false;
	}

	private static boolean ComprobarConArchiva(String[] compLineas) {
		// TODO Auto-generated method stub
//		Componemos la url
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("artifact", compLineas[0]);
		vars.put("url",compLineas[2]);
		String url="http://{url}:8080/restServices/browseService/searchArtifacts/{artifact}";
		RestTemplate resttemplate=new RestTemplate();
		String respuesta=resttemplate.getForObject(url, String.class,vars);
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
		System.out.println(respuesta);
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
		String url="";
//		String url="http://search.maven.org/solrsearch/select?q=g:"org.kurento”%20AND%20a:"{artifact}"%20AND%20v:"{version}%20OR%20l:”javadoc”%20OR%20p:”jar”&rows=20&wt=json";
//		String url="http://search.maven.org/solrsearch/select?q=g:”{artifact}” ANDv:”{version}”&wt=json";
//		String url="http://search.maven.org/solrsearch/select?q=g:\"com.google.inject"%20AND%20a:\"guice\"%20AND%20v:\"3.0\" %20AND%20l:\"javadoc\"%20AND%20p:\"jar\"&rows=20&wt=json";
		RestTemplate resttemplate=new RestTemplate();
		String respuesta=resttemplate.getForObject(url, String.class,vars);
		System.out.println(respuesta);
		{
		//url consulta http://search.maven.org/solrsearch/select?q=g:"org.kurento"%20AND%20a:"kurento-java"%20AND%20v:"6.1.0"%20OR%20l:"javadoc"%20OR%20p:"jar"&rows=20&wt=json

//la respuesta obtenida (ejecutada consulta en postman) buscando el artefacto kurento-java en su versión 6.1.0 es la siguiente
//	    "responseHeader": {
//	        "status": 0,
//	        "QTime": 1,
//	        "params": {
//	            "fl": "id,g,a,v,p,ec,timestamp,tags",
//	            "sort": "score desc,timestamp desc,g asc,a asc,v desc",
//	            "indent": "off",
//	            "q": "g:\"org.kurento\" AND a:\"kurento-java\" AND v:\"6.1.0\" OR l:\"javadoc\" OR p:\"jar\"",
//	            "wt": "json",
//	            "rows": "20",
//	            "version": "2.2"
//	        }
//	    },
//	    "response": {
//	        "numFound": 1,
//	        "start": 0,
//	        "docs": [
//	            {
//	                "id": "org.kurento:kurento-java:6.1.0",
//	                "g": "org.kurento",
//	                "a": "kurento-java",
//	                "v": "6.1.0",
//	                "p": "pom",
//	                "timestamp": 1441197724000,
//	                "tags": [
//	                    "todo"
//	                ],
//	                "ec": [
//	                    "-source-release.zip",
//	                    ".pom"
//	                ]
//	            }
//	        ]
//	    }
//	}
		//procesamos la respuesta para hacer la comprobación y devolvemos el booleano adecuado
		return false;
	}
}