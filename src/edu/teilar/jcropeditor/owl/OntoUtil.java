/*
 * (C) Copyright 2010-2013 Maria Tsiakmaki.
 * 
 * This file is part of jcropeditor.
 *
 * jcropeditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License (LGPL) 
 * as published by the Free Software Foundation, version 3.
 * 
 * jcropeditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with jcropeditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.teilar.jcropeditor.owl;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;


/**
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class OntoUtil {

	/** the postfix, placed after each kconcept individual */
	public static final String KObjectPostfix 			= "_KObject";
	public static final String ConceptPostfix 			= "_Concept";
	public static final String ConceptGraphPostfix 		= "_ConceptGraph";
	public static final String ConceptGraphNodePostfix 	= "_ConceptGraphNode";
	public static final String XGraphPostfix 			= "_XGraph";
	public static final String XNodePostfix				= "_XNode";
	public static final String LearningActNodePostfix	= "_LearningActNode";
	public static final String DialogueNodePostfix 		= "_DialogueNode";
	public static final String ControlNodePostfix 		= "_ControlNode";
	public static final String SeqGroupPostfix 			= "_SeqGroup";
	public static final String ParGroupPostfix 			= "_ParGroup";
	
	public static final String KRCPostfix 				= "_KRC";
	public static final String KRCNodePostfix 			= "_KRCNode";
	public static final String PhysicalLocationPostfix 	= "_PhysicalLocation";

	public static final String KRCEdgePostfix 			= "_KRCEdge";
	public static final String XGraphEdgePostfix 		= "_XGraphEdge";
	public static final String ConceptGraphEdgePostfix 	= "_ConceptGraphEdge";
	public static final String LOMPostfix 				= "_LOM";
	public static final String XModelPostfix			= "_ExecutionModel";
	public static final String XManagerPostfix			= "_ExecutionManager";

	/* lom */
	public static final String EducationPostfix			= "_Education";
	public static final String RelationPostfix			= "_Relation";
	public static final String ResourcePostfix			= "_Resource";
	public static final String ClassificationPostfix	= "_Classification";
	public static final String GeneralPostfix			= "_General";
	public static final String LifecyclePostfix			= "_Lifecycle";
	public static final String TechnicalPostfix			= "_Technical";
	public static final String MetaMetaDataPostfix		= "_MetaMetadata";
	public static final String RightsPostfix			= "_Rights";
	public static final String TaxonPostfix				= "_Taxon";
	public static final String TaxonPathPostfix			= "_TaxonPath";
	public static final String IdentifierPostfix		= "_Identifier";
	
	
	public static final IRI KConceptIri 		= IRI.create("http://www.cs.teilar.gr/ontologies/KConcept.owl");
	public static final IRI GraphIri 			= IRI.create("http://www.cs.teilar.gr/ontologies/Graph.owl");
	public static final IRI ConceptGraphIri 	= IRI.create("http://www.cs.teilar.gr/ontologies/ConceptGraph.owl");

	public static final IRI KRCIri 				= IRI.create("http://www.cs.teilar.gr/ontologies/KRC.owl");
	public static final IRI LearningObjectIri	= IRI.create("http://www.cs.teilar.gr/ontologies/LearningObject.owl");
	public static final IRI LOMIri				= IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl");
	public static final IRI KObjectIri			= IRI.create("http://www.cs.teilar.gr/ontologies/KObject.owl");
	public static final IRI XGraphIri			= IRI.create("http://www.cs.teilar.gr/ontologies/XGraph.owl");
	public static final IRI XModelIri			= IRI.create("http://www.cs.teilar.gr/ontologies/XModel.owl");
	
	public static final PrefixManager KConceptPM 		= new DefaultPrefixManager("http://www.cs.teilar.gr/ontologies/KConcept.owl#");
	public static final PrefixManager GraphPM 			= new DefaultPrefixManager("http://www.cs.teilar.gr/ontologies/Graph.owl#");
	public static final PrefixManager ConceptGraphPM 	= new DefaultPrefixManager("http://www.cs.teilar.gr/ontologies/ConceptGraph.owl#");
	public static final PrefixManager KRCPM 			= new DefaultPrefixManager("http://www.cs.teilar.gr/ontologies/KRC.owl#");
	public static final PrefixManager LearningObjectPM 	= new DefaultPrefixManager("http://www.cs.teilar.gr/ontologies/LearningObject.owl#");
	public static final PrefixManager KObjectPM 		= new DefaultPrefixManager("http://www.cs.teilar.gr/ontologies/KObject.owl#");
	public static final PrefixManager LOMPM 			= new DefaultPrefixManager("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#");
	public static final PrefixManager XGraphPM 			= new DefaultPrefixManager("http://www.cs.teilar.gr/ontologies/XGraph.owl#");
	public static final PrefixManager XModelPM 			= new DefaultPrefixManager("http://www.cs.teilar.gr/ontologies/XModel.owl#");
	
	
	
	/**
	 * Compose the full name of an individual
	 * 
	 * @see getIndividualBareName()
	 * @param xModelBareName
	 * @param kobjName
	 * @return
	 */
	public static final String buildXModelName(String xModelBareName, String kobjName) {
		return OntoUtil.buildIndividualName(xModelBareName, kobjName, XModelPostfix);
	}
	/**
	 * Deconstruct the full name of an individual and return its bare name 
	 * without the postfix and the name of the kobject
	 * 
	 * @see buildXModelName()
	 * @param indi the owl individual
	 * @param kobjName the learning object name 
	 * @param postfix the postfix associated with the type of the individual
	 * @return
	 */
	public static final String getIndividualBareName(OWLNamedIndividual indi, String kobjName, String postfix) {
		String name = indi.getIRI().getFragment();
		return name.substring(0, name.lastIndexOf("_" + kobjName + postfix));
	}
	
	public static final String getBareName(String name, String kobjName, String postfix) {
		String toCut = (kobjName != null && !kobjName.equals("")) ? "_" + kobjName : "";
		if(name.endsWith(postfix)) {
			toCut = toCut + postfix;
		}
		return name.substring(0, name.lastIndexOf(toCut));
	}
	
	
	/**
	 * Compose the full name of an individual
	 * 
	 * @see getIndividualBareName()
	 * @param xModelBareName
	 * @param kobjName
	 * @return
	 */
	public static final String buildIndividualName(String xModelBareName, String kobjName, String postfix) {
		return xModelBareName + "_" + kobjName + postfix;
	}

	/**
	 * @see constructEdgeIndividualAbbreviatedIRI() 
	 * @param kConceptLabelA
	 * @param kConceptLabelB
	 * @param kObjectName
	 * @param postfix
	 * @param aIsSourceOfB
	 * @return
	 */
	public static final String getFromLabelFromEdgeIndividualAbbreviatedIRI(String edgeLabel) {
		//edgeLabel = "From_" + kConceptLabelA + "_To_" + kConceptLabelB;
		String[] splits = edgeLabel.split("_");
		return splits[1];
	}
	
	public static final String getToLabelFromEdgeIndividualAbbreviatedIRI(String edgeLabel) {
		String[] splits = edgeLabel.split("_");
		return splits[4];
	}
	
	public static final String constructEdgeId(String kConceptLabelA, 
			String kConceptLabelB, boolean aIsSourceOfB) {
		String edgeId;
		if (aIsSourceOfB) {
			edgeId = "From_" + kConceptLabelA + "_To_" + kConceptLabelB;
		} else {
			edgeId = "From_" + kConceptLabelB + "_To_" + kConceptLabelA;
		}

		return edgeId;
	}
}
