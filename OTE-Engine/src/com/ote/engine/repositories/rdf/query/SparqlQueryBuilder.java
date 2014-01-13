package com.ote.engine.repositories.rdf.query;

import org.openrdf.model.ValueFactory;

import com.ote.engine.OTEEngine;

public abstract class SparqlQueryBuilder {
	
	private OTEEngine engine;

	public SparqlQueryBuilder(OTEEngine engine) {
		this.engine = engine;

	}

	public OTEEngine getEngine() {
		return engine;
	}
	
	public ValueFactory getValueFactory(){
		return getEngine().getRdfConnector().getValueFactory();
	}
}
