package com.ote.engine.io;

import java.io.FileWriter;
import java.io.IOException;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import com.google.gson.JsonObject;
import com.ote.engine.OTEEngine;
import com.ote.engine.kernel.exceptions.OTEEngineException;

public class BaseToJson {

	
	public static void generateToFile(OTEEngine engine, String filename) throws IOException, QueryEvaluationException, RepositoryException, MalformedQueryException, OTEEngineException{
		FileWriter fw = new FileWriter(filename);
		JsonObject data = null;
		if (engine.getCurrentBase() != null){
			data = engine.getCurrentBase().generateJson(engine);
		}else {
			data = new JsonObject();
			data.addProperty("error", "base is empty");
		}
		fw.write(data.toString());

		fw.flush();
		fw.close();
	}
}
