package com.ote.engine;

import java.io.File;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ote.engine.kernel.base.Base;
import com.ote.engine.kernel.base.BasesCRUD;
import com.ote.engine.kernel.exceptions.OTEEngineException;
import com.ote.engine.repositories.OTERepositories;
import com.ote.engine.repositories.rdf.RdfConnector;

public class OTEEngine {
	final Logger logger = LoggerFactory.getLogger(OTEEngine.class);
	private OTERepositories repositories;
	private Base currentBase;
	
	public OTEEngine(File fileDirectory, String rdfServerUrl, String repositoryID) throws OTEEngineException {
		//init repositories
		this.repositories = new OTERepositories(this, fileDirectory, rdfServerUrl, repositoryID);
		
		//load current base
		loadCurrentBase();
		
		getLogger().info("OTEEngine ready");
	}
	
	

	//printing
	@Override
	public String toString() {
		return "an OTEEngine[Current base ="+this.getCurrentBase()+"]";
	}
	
	
	//Accessors
	public OTERepositories getRepositories() {
		return repositories;
	}
	
	public Base getCurrentBase() {
		return currentBase;
	}
	
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Log operations sending to the database
	 * @param user User requesting the operation
	 * @param action Name of the operation
	 * @param parameters Parameters of the operation
	 */
	public void logOperation(String user, String action, String parameters) {
		StringBuilder trace = new StringBuilder();
		trace.append("OPERATION[");
		if (user != null) trace.append(user+":");
		if (action != null) trace.append(action);
		if (parameters != null) trace.append("("+parameters+")");
		trace.append("]");
		
		getLogger().trace(trace.toString());
	}
	
		
	// BASE
	/**
	 * Load current base from repository
	 * @throws OTEEngineException
	 */
	public Base loadCurrentBase() throws OTEEngineException {
		try {
			return this.currentBase = BasesCRUD.loadBase(this);
		} catch (QueryEvaluationException e) {
			throw new OTEEngineException(e);
		} catch (RepositoryException e) {
			throw new OTEEngineException(e);
		} catch (MalformedQueryException e) {
			throw new OTEEngineException(e);
		}
	}
	
	public Base createBase(String user, String name, String namespace, String comment) throws OTEEngineException{
		return BasesCRUD.createBase(this, user, name, namespace, comment);
	}

	public Base updateBase(String user, String name, String namespace, String comment) throws OTEEngineException{
		return BasesCRUD.updateBase(this, user, name, namespace, comment);
	}



	public RdfConnector getRdfConnector() {
		return this.getRepositories().getRdfConnector();
	}
}
