package br.edu.unifor.semanticweb.controller;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class CrawlerController {
	
	public static void main(String[] args) {
		
		String queryStr = "PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#>"
				+ "PREFIX dbo:     <http://dbpedia.org/ontology/>" 
				+ "SELECT distinct ?software ?abstract" 
				+ " {"
				+ "  ?software a dbo:Software ;" 
				+ "           rdfs:label ?label ;"
				+ "           dbo:abstract ?abstract ;" 
				+ "           rdfs:comment ?comment ."
				+ "  FILTER langMatches(lang(?label),'EN')" 
				+ "  FILTER contains(?abstract,'people')" 
				+ "}"
				+ "LIMIT 1000";
		Query query = QueryFactory.create(queryStr);
		
		// Remote execution.
		try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)) {
			// Set the DBpedia specific timeout.
			((QueryEngineHTTP) qexec).addParam("timeout", "100000");
			
			// Execute.
			ResultSet rs = qexec.execSelect();
			
//			ResultSetFormatter.out(System.out, rs, query);
			
			System.out.println("Sotware\t\t\t\t\t\tAbstract");
			int i = 0;
			while (rs.hasNext()) {
				QuerySolution querySolution = (QuerySolution) rs.next();
				RDFNode soft = querySolution.get("?software");
				System.out.println(soft.toString());
				RDFNode abs = querySolution.get("?abstract");
				System.out.println("\t\t" + abs.toString());
				i++;
			}
			System.out.println("=========================================");
			System.out.println(i);
			System.out.println("=========================================");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

