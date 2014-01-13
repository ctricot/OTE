package com.ote.engine.kernel.space;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import com.google.gson.JsonObject;
import com.ote.engine.OTEEngine;
import com.ote.engine.kernel.atom.Atom;
import com.ote.engine.kernel.contants.OTEConstants;
import com.ote.engine.kernel.exceptions.OTEEngineException;


public class Space extends Atom{
	private static final long serialVersionUID = 1L;
	
	public static final String TYPE_URI = OTEConstants.KERNEL_NAMESPACE+"space";
	
	
	public Space() {
		this(null,null,null,null,null,null);
	}

	public Space(String uri, String author, String name, String comment, String created, String modified) {
		super(uri,author, name,created,modified,comment);
	}
	
	public Space(Atom atom) {
		super(atom);
	}

	@Override
	public String getTypeUri() {
		return Space.TYPE_URI;
	}

	//Json
	public JsonObject generateJson(OTEEngine engine) throws QueryEvaluationException, RepositoryException, MalformedQueryException, OTEEngineException {
		JsonObject baseOut = super.generateJson(engine);
		
		
		return baseOut;
	}

	

}