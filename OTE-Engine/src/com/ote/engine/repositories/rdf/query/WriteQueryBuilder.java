package com.ote.engine.repositories.rdf.query;

import info.aduna.iteration.Iterations;

import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import com.ote.engine.OTEEngine;
import com.ote.engine.kernel.atom.Atom;
import com.ote.engine.kernel.exceptions.OTEEngineException;
import com.ote.engine.tools.Utils;

public class WriteQueryBuilder extends SparqlQueryBuilder {

	public WriteQueryBuilder(OTEEngine engine) throws OTEEngineException {
		super(engine);
	}

	public void execute(List<Statement> statementsToDelete, List<Statement> statementsToAdd) throws OTEEngineException {
		try {
			RepositoryConnection con = getEngine().getRdfConnector().getConnection();
			try {
				// Remove existing data
				List<Statement> toUpdate = new ArrayList<Statement>(); 
				for (Statement statement : statementsToDelete) {
					toUpdate = Iterations.addAll(con.getStatements(statement.getSubject(), statement.getPredicate(), statement.getObject(), false), toUpdate);

				}
				con.remove(toUpdate);

				// Add new values
				if (statementsToAdd != null)
					for (Statement statement : statementsToAdd) {
						con.add(statement);
					}			
			} finally {
				con.close();
			}

		} catch (RepositoryException e) {
			throw new OTEEngineException(e);
		}
		
	}

	

	public List<Statement> getAtomPropertiesToAddForUpdate(ValueFactory valueFactory, URI currentUri, String name, String comment) {
		List<Statement> result = new ArrayList<Statement>();
	
		// name
		result.add( new StatementImpl(currentUri, RDFS.LABEL, valueFactory.createLiteral(name)));

		// comment
		if (comment != null)
			if (!comment.isEmpty())
				result.add( new StatementImpl(currentUri, RDFS.COMMENT, valueFactory.createLiteral(comment)));
		
		// modified date
		result.add( new StatementImpl(currentUri, valueFactory.createURI(Atom.MODIFIED_URI), valueFactory.createLiteral(Utils.getCurrentDateInString())));
		
		return result;
	}
	
	public List<Statement> getAllPropertiesToDelete(URI currentUri) {
		List<Statement> result = new ArrayList<Statement>();
		result.add( new StatementImpl(currentUri, null, null));
		
		return result;
	}
	
	public List<Statement> getAtomPropertiesToDeleteBeforeUpdate(ValueFactory valueFactory, URI currentUri) {
		List<Statement> result = new ArrayList<Statement>();
		result.add( new StatementImpl(currentUri, RDFS.LABEL, null));
		result.add( new StatementImpl(currentUri, RDFS.COMMENT, null));
		result.add( new StatementImpl(currentUri, valueFactory.createURI(Atom.MODIFIED_URI), null));
	
		return result;
	}

}
