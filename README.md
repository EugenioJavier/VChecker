# VChecker
Java program that checks if one version of one artifact has been uploaded to one repository (Maven central or Archiva)
@Param gets the data about the artifact from a json file with this structure

      {"artifacts":[
				{"artifact":"[artifactName]","version":"[artifactVersion]","server":"[ipServer]"},
				...
			]	
		}

