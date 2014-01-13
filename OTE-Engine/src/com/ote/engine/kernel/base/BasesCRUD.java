package com.ote.engine.kernel.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import com.ote.engine.OTEEngine;
import com.ote.engine.kernel.CRUD.AbstractCRUD;
import com.ote.engine.kernel.atom.Atom;
import com.ote.engine.kernel.exceptions.OTEEngineException;
import com.ote.engine.repositories.rdf.query.CreateQueryBuilder;
import com.ote.engine.repositories.rdf.query.SelectQueryBuilder;
import com.ote.engine.repositories.rdf.query.WriteQueryBuilder;

public class BasesCRUD extends AbstractCRUD {
	
	public static Base loadBase(OTEEngine engine) throws OTEEngineException, QueryEvaluationException, RepositoryException, MalformedQueryException {
		//prepare the query
		String[] variables = {"namespace"};
		String[] statements = {"?"+SelectQueryBuilder.URI_VARIABLE+" <" + Base.NAMESPACE_URI + "> ?namespace."};
		SelectQueryBuilder query = new SelectQueryBuilder(engine, Base.TYPE_URI, variables, statements);
		
		//Execute the query
		List<HashMap<String, String>> result = query.execute();
		if (!result.isEmpty()){
			HashMap<String, String> current = result.get(0);
			//Atom properties
			Atom atom = query.extractAtomPropertiesFromResult(current);
			
			Base base = new Base(atom, current.get("namespace"));
			return base;
		}else{
			return null;
		}
	}

	public static Base createBase(OTEEngine engine, String user, String name, String namespace, String comment) throws OTEEngineException {
		engine.logOperation(user, "createBase", "name=" + name + "&namespace=" + namespace + "&comment=" + comment);

		if (name == null)
			throw new OTEEngineException("The name is mandatory");
		if (name.isEmpty())
			throw new OTEEngineException("The name can't be empty");

		if (namespace == null)
			throw new OTEEngineException("The namespace is mandatory");
		if (namespace.isEmpty())
			throw new OTEEngineException("the namsepace can't be empty");

		// clear all the data in the repositories
		engine.getRepositories().clearAllDataAndFiles();

		CreateQueryBuilder query =  new CreateQueryBuilder(engine);

		// Add new data
		List<Statement> statementsToAdd = new ArrayList<Statement>();
		URI currentUri = generateUri(query.getValueFactory(), Base.TYPE_URI, name);
		//Atoms properties
		statementsToAdd.addAll(query.getAtomPropertiesForCreation(query.getValueFactory(), currentUri, Base.TYPE_URI, user, name, comment));
		//Current object properties
		statementsToAdd.add(new StatementImpl(currentUri, query.getValueFactory().createURI(Base.NAMESPACE_URI), query.getValueFactory().createLiteral(namespace)));

		//execute
		query.execute(statementsToAdd);

		// TODO create the default model
		// this.createOntoterminologicalModel(login);
		
		//load the current base to maintain the coherence
		return engine.loadCurrentBase();
	}

	public static Base updateBase(OTEEngine engine, String user, String name, String namespace, String comment) throws OTEEngineException {
		engine.logOperation(user, "updateBase", "name=" + name + "&namespace=" + namespace + "&comment=" + comment);

		if (name == null)
			throw new OTEEngineException("The name is mandatory");
		if (name.isEmpty())
			throw new OTEEngineException("The name can't be empty");

		if (namespace == null)
			throw new OTEEngineException("The namespace is mandatory");
		if (namespace.isEmpty())
			throw new OTEEngineException("the namsepace can't be empty");

		if (engine.getCurrentBase() == null)
			throw new OTEEngineException("The system does not contain a base to update.");
		
		WriteQueryBuilder query =  new WriteQueryBuilder(engine);
		
		URI currentUri = query.getValueFactory().createURI(engine.getCurrentBase().getUri());

		
		// Remove existing data
		List<Statement> statementsToDelete = new ArrayList<Statement>(); 
		//Atoms properties
		statementsToDelete.addAll(query.getAtomPropertiesToDeleteBeforeUpdate(query.getValueFactory(), currentUri));
		//Current object properties
		statementsToDelete.add(new StatementImpl(currentUri, query.getValueFactory().createURI(Base.NAMESPACE_URI), null));
		
		// Add new data
		List<Statement> statementsToAdd = new ArrayList<Statement>();
		//Atoms properties
		statementsToAdd.addAll(query.getAtomPropertiesToAddForUpdate(query.getValueFactory(), currentUri, name, comment));
		//Current object properties
		statementsToAdd.add(new StatementImpl(currentUri, query.getValueFactory().createURI(Base.NAMESPACE_URI), query.getValueFactory().createLiteral(namespace)));

		//execute
		query.execute(statementsToDelete, statementsToAdd);
		
		//load the current base to maintain the coherence
		return engine.loadCurrentBase();
	
	}


	

}
