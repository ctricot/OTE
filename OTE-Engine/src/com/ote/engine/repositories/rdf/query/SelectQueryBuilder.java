package com.ote.engine.repositories.rdf.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPQueryEvaluationException;

import com.ote.engine.OTEEngine;
import com.ote.engine.kernel.atom.Atom;
import com.ote.engine.kernel.contants.OTEConstants;
import com.ote.engine.kernel.exceptions.OTEEngineException;

public class SelectQueryBuilder extends SparqlQueryBuilder {
	public static final String URI_VARIABLE = "uri";
	public static final String AUTHOR_VARIABLE = "author";
	public static final String NAME_VARIABLE = "name";
	public static final String COMMENT_VARIABLE = "comment";
	public static final String CREATED_VARIABLE = "created";
	public static final String MODIFIED_VARIABLE = "modified";
	
	private StringBuilder query;
	private List<String> variables;

	
	public SelectQueryBuilder(OTEEngine engine) throws OTEEngineException {
		super(engine);
		this.query = new StringBuilder();
		this.variables = new ArrayList<String>();

		initialize();
	}

	public SelectQueryBuilder(OTEEngine engine, String typeUri,String[] variables, String[] statements) throws OTEEngineException {
		this(engine);

		// add variables
		if (variables != null)
			for (String variable : variables) {
				this.addVariable(variable);
			}

		this.appendSelectStatement();
		this.append("WHERE {");
		// Atom properties
		this.appendAtomProperties(typeUri);
		// Other properties
		for (String statement : statements) {
			query.append(statement);
		}
		this.append("}");
	}

	
	public List<String> getVariables() {
		return variables;
	}

	/**
	 * Add a variable to the query
	 * @param variable
	 * @throws OTEEngineException if the query contains already a variable with the same name 
	 */
	public void addVariable(String variable) throws OTEEngineException {
		if (this.getVariables().contains(variable))
			throw new OTEEngineException("The query already contains the variable " + variable);
		this.getVariables().add(variable);
	}
	
	
	@Override
	public String toString() {
		return this.query.toString();
	}

	private StringBuilder getQuery() {
		return query;
	}

	
	private void initialize() throws OTEEngineException {
		// add atom variables
		addVariable(URI_VARIABLE);
		addVariable(AUTHOR_VARIABLE);
		addVariable(NAME_VARIABLE);
		addVariable(COMMENT_VARIABLE);
		addVariable(CREATED_VARIABLE);
		addVariable(MODIFIED_VARIABLE);

		// add prefixes
		this.append("PREFIX ote:<" + OTEConstants.KERNEL_NAMESPACE + ">");
		this.append("PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>");
		this.append("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>");

	}

	public void appendSelectStatement() {
		String value = "SELECT";

		for (String variable : getVariables()) {
			value += " ?" + variable;
		}

		this.append(value);

	}

	public void append(String statement) {
		// check if the statement if "well formed"
		if (statement.contains("\n")) {
			getEngine().getLogger().warn("You don't need to add \\n in your SPARQL query :\"" + statement.replaceAll("\n", "\\\\n") + "\"");
		}
		this.getQuery().append(statement + "\n");
	}

	public void appendAtomProperties(String typeUri) {
		this.append("?uri rdf:type <" + typeUri + ">.");
		this.append("?uri <" + Atom.AUTHOR_URI + "> ?author.");
		this.append("?uri rdfs:label ?name.");
		this.append("OPTIONAL {?uri rdfs:comment ?comment }");
		this.append("?uri <" + Atom.CREATED_URI + "> ?created.");
		this.append("OPTIONAL {?uri <" + Atom.MODIFIED_URI + "> ?modified }");
	}

	public Atom extractAtomPropertiesFromResult(HashMap<String, String> result) {

		return new Atom(result.get(URI_VARIABLE), result.get(AUTHOR_VARIABLE), result.get(NAME_VARIABLE), result.get(CREATED_VARIABLE), result.get(MODIFIED_VARIABLE), result.get(COMMENT_VARIABLE));
	}

	public List<HashMap<String, String>> execute() throws OTEEngineException, QueryEvaluationException, RepositoryException, MalformedQueryException {
		List<HashMap<String, String>> allResults = new ArrayList<HashMap<String,String>>();
		
		RepositoryConnection con = getEngine().getRepositories().getRdfConnector().getConnection();
		try {
			TupleQueryResult tupleQueryResult = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString()).evaluate();
			try {
				while (tupleQueryResult.hasNext()) {
					HashMap<String, String> result = new HashMap<String, String>();

					BindingSet bindingSet = tupleQueryResult.next();

					for (String variable : getVariables()) {
						String value = null;

						if (bindingSet.hasBinding(variable)) {
							value = bindingSet.getValue(variable).stringValue();
						}

						result.put(variable, value);
					}
					
					allResults.add(result);

				}
			} finally {
				tupleQueryResult.close();
			}
		} catch( HTTPQueryEvaluationException e){
			throw new OTEEngineException("Error in query :"+getQuery().toString(), e);
			
		} finally {
			con.close();
		}
		// for (String key : result.keySet())
		// {System.out.println(key+"-->"+result.get(key));}
		return allResults;
	}

}
