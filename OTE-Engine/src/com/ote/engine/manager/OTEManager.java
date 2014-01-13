package com.ote.engine.manager;

import java.io.File;

import com.ote.engine.OTEEngine;
import com.ote.engine.io.BaseToJson;
import com.ote.engine.kernel.space.SpacesCRUD;

public class OTEManager {
	
	public static void main(String[] args) throws Exception {
		//filter the log output
        System.setProperty(org.slf4j.impl.SimpleLogger.LOG_KEY_PREFIX+"com.ote.engine.OTEEngine", "TRACE");
       //System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
        
        
		File fileDirectory = new File("/Applications/OTE/data/ote4soft");
		String rdfServerUrl = "http://localhost:8080/openrdf-sesame";
		String repositoryID = "ote4soft";
		
		@SuppressWarnings("unused")
		String user = "totoro";
		
		OTEEngine engine = new OTEEngine(fileDirectory, rdfServerUrl, repositoryID);
		
		//engine.createBase("totoro", "OTE4SOFT", "http://www.ote4Soft.org#", null);
		//engine.updateBase("totoro", "OTE4SOFT 2", "http://www.ote4Soft.com#", null);

		
		//SpacesCRUD.createSpace(engine, user, "coool", null);
		//SpacesCRUD.createSpace(engine, user, "coool again", "another great space");

		String uri = "http://www.ote.org/kernel#space_coool_1389275370125";
		SpacesCRUD.deleteSpace(engine, user, uri);
		
		BaseToJson.generateToFile(engine, "/Users/christophetricot/Downloads/out.json");
		
		System.out.println("FIN");
	}

}
