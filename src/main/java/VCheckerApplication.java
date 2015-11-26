import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
		ruta=ruta.replace(" ", "%20");
		
		try{
			URL url=new URL(ruta);
			URLConnection urlConection = url.openConnection();
			InputStream is = urlConection.getInputStream();
			String res=getStringFromInputStream(is);
			System.out.println(res);
		}catch (MalformedURLException ex){
			ex.printStackTrace();
		}catch(IOException ei){
			ei.printStackTrace();
		}
		
		{
		//#############################################################################	
		//en este punto res contiene un json correcto con lo devuelto por maven central
		//#############################################################################
		return false;
	}
}
	
	// convert InputStream to String
		private static String getStringFromInputStream(InputStream is) {

			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;
			try {

				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return sb.toString();

		}

	}

