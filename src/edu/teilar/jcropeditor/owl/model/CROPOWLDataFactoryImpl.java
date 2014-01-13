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

package edu.teilar.jcropeditor.owl.model;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.view.ExecutionGraph;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CROPOWLDataFactoryImpl extends OWLDataFactoryImpl {

	
	public OWLObjectPropertyAssertionAxiom getHasNodeAssertionAxiom(
			OWLNamedIndividual parentIndi, OWLNamedIndividual childIndi) {
		return getOWLObjectPropertyAssertionAxiom(getHasNode(), parentIndi, childIndi);
	}

	
	/******************** CROP OWL Individuals **********************/

	public OWLNamedIndividual getEducationalIndi(String kObjectName) {
		return getOWLNamedIndividual(":" + kObjectName
				+ OntoUtil.EducationPostfix, OntoUtil.LOMPM);
	}
	
	public OWLNamedIndividual getConceptGraphIndi(String kObjectName) {
		// System.out.println(":" + kObjectName + OntoUtil.ConceptGraphPostfix);
		return getOWLNamedIndividual(":" + kObjectName
				+ OntoUtil.ConceptGraphPostfix, OntoUtil.ConceptGraphPM);
	}

	public OWLNamedIndividual getConceptGraphIndi(KObject kObject) {
		return getConceptGraphIndi(kObject.getName());
	}

	public OWLNamedIndividual getKRCGraphIndi(String kObjectName) {
		return getOWLNamedIndividual(":" + kObjectName + OntoUtil.KRCPostfix,
				OntoUtil.KRCPM);
	}

	public OWLNamedIndividual getKRCGraphIndi(KObject kObject) {
		return getKRCGraphIndi(kObject.getName());
	}

	public OWLNamedIndividual getConceptGraphNodeIndi(String conceptLabel,
			String kObjectName) {
		return getOWLNamedIndividual(
				":" + constructNodeName(conceptLabel, kObjectName,
								OntoUtil.ConceptGraphNodePostfix),
				OntoUtil.ConceptGraphPM);
	}

	public OWLNamedIndividual getKRCNodeIndi(String conceptLabel,
			String kObjectName) {
		return getOWLNamedIndividual(
				":" + constructNodeName(conceptLabel, kObjectName,
								OntoUtil.KRCNodePostfix), OntoUtil.KRCPM);
	}

	public OWLNamedIndividual getConceptGraphEdgeIndi(String fromConceptLabel,
			String toConceptLabel, String kObjectName) {

		String cgEdgeAbbreviatedIRI = constructEdgeIndividualAbbreviatedIRI(
				fromConceptLabel, toConceptLabel, kObjectName,
				OntoUtil.ConceptGraphEdgePostfix, true);
		return getOWLNamedIndividual(cgEdgeAbbreviatedIRI, OntoUtil.GraphPM);
	}

	public OWLNamedIndividual getKRCEdgeIndi(String fromConceptLabel,
			String toConceptLabel, String kObjectName) {
		String iri = constructEdgeIndividualAbbreviatedIRI(fromConceptLabel,
				toConceptLabel, kObjectName, OntoUtil.KRCEdgePostfix, true);
		return getOWLNamedIndividual(iri, OntoUtil.GraphPM);
	}

	
	public OWLNamedIndividual getXGraphEdgeIndi(String fromXNodeLabel,
			String toXNodeLabel, String xGraphName, String kObjectName) {
		String iri = constructEdgeIndividualAbbreviatedIRI(fromXNodeLabel,
				toXNodeLabel, (xGraphName + "_" + kObjectName),
				OntoUtil.XGraphEdgePostfix, true);

		return getOWLNamedIndividual(iri, OntoUtil.GraphPM);
	}

	public OWLNamedIndividual getXGraphEdgeIndi(String xNodeId,
			String xGraphName, String kObjectName) {
		String iri = constructEdgeIndividualAbbreviatedIRI(xNodeId, 
				(xGraphName	+ "_" + kObjectName), 
				OntoUtil.XGraphEdgePostfix);

		return getOWLNamedIndividual(iri, OntoUtil.GraphPM);
	}

	
	public String extractNodeName(String fullNodeName) {
		String[] splits = fullNodeName.split("_");
		return splits[0];
	}

	public String constructNodeName(String kConceptName,
			String kObjectName, String postfixName) {
		return kConceptName + "_" + kObjectName + postfixName;
	}

	public OWLNamedIndividual getConceptIndi(String conceptLabel) {
		return getOWLNamedIndividual(":" + conceptLabel
				+ OntoUtil.ConceptPostfix, OntoUtil.KConceptPM);
	}

	/**
	 * 
	 * @param kConceptLabelA 
	 * @param kConceptLabelB
	 * @param kObjectName
	 * @param postfix
	 * @param aIsSourceOfB if true From_A_To_B_..., else From_B_To_A_... 
	 * @return
	 */
	public String constructEdgeIndividualAbbreviatedIRI(String kConceptLabelA,
			String kConceptLabelB, String kObjectName, String postfix,
			boolean aIsSourceOfB) {
		String edgeLabel;
		if (aIsSourceOfB) {
			edgeLabel = "From_" + kConceptLabelA + "_To_" + kConceptLabelB;
		} else {
			edgeLabel = "From_" + kConceptLabelB + "_To_" + kConceptLabelA;
		}

		return ":" + edgeLabel + "_" + kObjectName + postfix;
	}

	/**
	 * 
	 * @param edgeLabel e.g. From_A_To_B
	 * @param kObjectName the name of kobj
	 * @param postfix probably CROP Constant 
	 * @return e.g. From_A_To_B_kobj_Edge
	 */
	public String constructEdgeIndividualAbbreviatedIRI(String edgeLabel,
			String kObjectName, String postfix) {
		return ":" + edgeLabel + "_" + kObjectName + postfix;
	}

	public OWLNamedIndividual getContentOntologyIndi(String contentOntologyIRI) {
		return getOWLNamedIndividual(":" + contentOntologyIRI,
				OntoUtil.ConceptGraphPM);
	}

	public OWLNamedIndividual getKObjectIndi(String kObjectName) {
		return getOWLNamedIndividual(":" + kObjectName
				+ OntoUtil.KObjectPostfix, OntoUtil.KObjectPM);
	}

	public OWLNamedIndividual getDomainOntologyIndi(String domainOntology) {
		return getOWLNamedIndividual(":" + domainOntology,
				OntoUtil.ConceptGraphPM);
	}

	public OWLNamedIndividual getLOMIndi(String kObjectName) {
		return getOWLNamedIndividual(":" + kObjectName + OntoUtil.LOMPostfix,
				OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getXModelIndi(String xModelPrefix, String kobjName) {
		return getOWLNamedIndividual(":" + xModelPrefix + "_" + kobjName
				+ OntoUtil.XModelPostfix, OntoUtil.XModelPM);
	}

	public OWLNamedIndividual getXManagerIndi(String xManagerPrefix,
			KObject kobject) {
		return getOWLNamedIndividual(
				":" + xManagerPrefix + "_" + kobject.getName()
						+ OntoUtil.XManagerPostfix, OntoUtil.XModelPM);
	}

	public OWLNamedIndividual getXGraphIndi(String xGraphPrefix,
			String kobjectName) {
		return getOWLNamedIndividual(":" + xGraphPrefix + "_" + kobjectName
				+ OntoUtil.XGraphPostfix, OntoUtil.XGraphPM);
	}

	public OWLNamedIndividual getRelationIndi(String kObjectName,
			int numOfRelation) {
		return getOWLNamedIndividual(
				":" + kObjectName + OntoUtil.RelationPostfix + "_"
						+ String.valueOf(numOfRelation), OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getResourceIndi(String kObjectName) {
		return getOWLNamedIndividual(":" + kObjectName + OntoUtil.LOMPostfix
				+ OntoUtil.ResourcePostfix, OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getIdentifierIndi(String kObjName) {
		return getOWLNamedIndividual(":" + kObjName + OntoUtil.KObjectPostfix
				+ OntoUtil.IdentifierPostfix, OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getLomGeneralIndi(String kObjName) {
		return getOWLNamedIndividual(":" + kObjName + OntoUtil.GeneralPostfix,
				OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getLomLifeCycleIndi(String kObjName) {
		return getOWLNamedIndividual(
				":" + kObjName + OntoUtil.LifecyclePostfix, OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getLomIdentifierIndi(String kObjName,
			String semiPostfix) {
		return getOWLNamedIndividual(":" + kObjName + semiPostfix
				+ "_Identifier", OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getLomTechnicalIndi(String kObjName) {
		return getOWLNamedIndividual(
				":" + kObjName + OntoUtil.TechnicalPostfix, OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getLomMetaMetadataIndi(String kObjName) {
		return getOWLNamedIndividual(":" + kObjName
				+ OntoUtil.MetaMetaDataPostfix, OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getLomEducationalIndi(String kObjName, String num) {
		return getOWLNamedIndividual(":" + kObjName + OntoUtil.EducationPostfix
				+ "_" + num, OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getLomRightslIndi(String kObjName) {
		return getOWLNamedIndividual(":" + kObjName + OntoUtil.RightsPostfix,
				OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getClassificationIndi(String kObjName, int i) {
		return getOWLNamedIndividual(":" + kObjName
				+ OntoUtil.ClassificationPostfix + "_" + String.valueOf(i),
				OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getTaxonPathIndi(KObject kobj) {
		return getOWLNamedIndividual(":" + kobj.getTargetConcept()
				+ OntoUtil.TaxonPathPostfix, OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getTaxonIndi(KObject kobj) {
		return getOWLNamedIndividual(":" + kobj.getTargetConcept()
				+ OntoUtil.TaxonPostfix, OntoUtil.LOMPM);
	}

	public OWLNamedIndividual getPhysicalLocationIndi(String conceptLabel,
			String kObjectName) {
		return getOWLNamedIndividual(
				":" + constructNodeName(conceptLabel, kObjectName,
								OntoUtil.PhysicalLocationPostfix),
				OntoUtil.KObjectPM);
	}

	public OWLNamedIndividual getXNodeIndi(String name, int type,
			String xGraphName, String kObjectName) {
		if (type == ExecutionGraph.LEARNING_ACT_XNODE_TYPE) {
			return getLearningActNodeIndi(name, xGraphName, kObjectName);
		} else if (type == ExecutionGraph.CONTROL_XNODE_TYPE) {
			return getControlNodeIndi(name, xGraphName, kObjectName);
		} else if (type == ExecutionGraph.DIALOGUE_XNODE_TYPE) {
			return getDialogueNodeIndi(name, xGraphName, kObjectName);
		} else if (type == ExecutionGraph.SEQ_GROUP_XNODE_TYPE) {
			return getSeqGroupIndi(name, xGraphName, kObjectName);
		} else if (type == ExecutionGraph.PAR_GROUP_XNODE_TYPE) {
			return getParGroupIndi(name, xGraphName, kObjectName);
		}

		return null;
	}

	public OWLNamedIndividual getLearningActNodeIndi(String conceptLabel,
			String xGraphName, String kObjectName) {
		return getOWLNamedIndividual(
				":"
						+ constructNodeName(conceptLabel,
								(xGraphName + "_" + kObjectName),
								OntoUtil.LearningActNodePostfix),
				OntoUtil.XGraphPM);
	}

	public OWLNamedIndividual getDialogueNodeIndi(String dialogueId,
			String xGraphName, String kObjectName) {
		return getOWLNamedIndividual(
				":"
						+ constructNodeName(dialogueId,
								(xGraphName + "_" + kObjectName),
								OntoUtil.DialogueNodePostfix),
				OntoUtil.XGraphPM);
	}

	public OWLNamedIndividual getControlNodeIndi(String controlId,
			String xGraphName, String kObjectName) {
		return getOWLNamedIndividual(":" + constructNodeName(
				controlId, (xGraphName + "_" + kObjectName),
						OntoUtil.ControlNodePostfix), OntoUtil.XGraphPM);
	}

	//http://www.cs.teilar.gr/ontologies/Graph.owl#SeqGroup
	public OWLNamedIndividual getSeqGroupIndi(String targetConcept,
			String xGraphName, String kObjectName) {
		return getOWLNamedIndividual(":" + targetConcept + "_" + xGraphName 
				+ "_" + kObjectName + OntoUtil.SeqGroupPostfix, OntoUtil.XGraphPM);
	}
	
	//http://www.cs.teilar.gr/ontologies/Graph.owl#ParGroup
	public OWLNamedIndividual getParGroupIndi(String targetConcept,
			String xGraphName, String kObjectName) {
		return getOWLNamedIndividual(":" + targetConcept + "_" + xGraphName 
			+ "_" + kObjectName + OntoUtil.ParGroupPostfix, OntoUtil.XGraphPM);
	}
	
	public OWLNamedIndividual getBreathFirstAlgorithmIndi() {
		return getOWLNamedIndividual(":BreathFirst", OntoUtil.XModelPM);
	}

	public OWLNamedIndividual getDepthFirstAlgorithmIndi() {
		return getOWLNamedIndividual(":DepthFirst", OntoUtil.XModelPM);
	}

	public OWLNamedIndividual getRandomAlgorithmIndi() {
		return getOWLNamedIndividual(":Random", OntoUtil.XModelPM);
	}

	
	/******************** CROP OWL Class Expressions ******************/
	
	public OWLClass getEdge() {
		return getOWLClass(":Edge", OntoUtil.GraphPM);
	}

	public OWLClass getConceptGraphNode() {
		return getOWLClass(":ConceptGraphNode", OntoUtil.ConceptGraphPM);	
	}

	public OWLClass getConcept() {
		return getOWLClass(":Concept", OntoUtil.KConceptPM);
	}
	
	public OWLClass getKRCNode() {
		return getOWLClass(":KRCNode", OntoUtil.KRCPM);
	}
	
	public OWLClass getXGraphControlNode() {
		return getOWLClass(":ControlNode", OntoUtil.XGraphPM);
	}

	public OWLClass getDialogueNodeNode() {
		return getOWLClass(":DialogueNode", OntoUtil.XGraphPM);
	}
	
	
	public OWLClass getXGraph() {
		return getOWLClass(
				IRI.create("http://www.cs.teilar.gr/ontologies/XGraph.owl#XGraph"));
	}
	
	public OWLClass getParGroup() {
		return getOWLClass(IRI.create("http://www.cs.teilar.gr/ontologies/XGraph.owl#ParGroup"));
	}

	public OWLClass getSeqGroup() {
		return getOWLClass(IRI.create("http://www.cs.teilar.gr/ontologies/XGraph.owl#SeqGroup"));
	}
	
	public OWLClass getKProduct() {
		return getOWLClass(IRI
				.create("http://www.cs.teilar.gr/ontologies/KObject.owl#KProduct"));
	}
	
	public OWLClass getAssessmentResource(){
		return getOWLClass(IRI
				.create("http://www.cs.teilar.gr/ontologies/KObject.owl#AssessmentResource"));
	}
	public OWLClass getSupportResource() { 
		return getOWLClass(IRI
				.create("http://www.cs.teilar.gr/ontologies/KObject.owl#SupportResource"));
	}
	
	public OWLClass getXManager() { 
		return getOWLClass(IRI
				.create("http://www.cs.teilar.gr/ontologies/XModel.owl#ExecutionManager"));
	}
	
	public OWLClass getXModel() { 
		return getOWLClass(IRI
				.create("http://www.cs.teilar.gr/ontologies/XModel.owl#ExecutionModel"));
	}
	
	public OWLClass getConceptGraph() {
		return getOWLClass(IRI.create(
			"http://www.cs.teilar.gr/ontologies/ConceptGraph.owl#ConceptGraph"));
	}
	
	public OWLClass getKRCGraph() {
		return getOWLClass(IRI.create(
			"http://www.cs.teilar.gr/ontologies/KRC.owl#KRC"));
	}

	public OWLClass getLearningObject() {
		return getOWLClass(IRI.create(
        	"http://www.cs.teilar.gr/ontologies/LearningObject.owl#LearningObject"));
	}

	public OWLClass getKObject() {
		return getOWLClass(":KObject", OntoUtil.KObjectPM);
	}

	public OWLClass getDomainOntology() {
		return getOWLClass(":DomainOntology",
				OntoUtil.ConceptGraphPM);
	}

	public OWLClass getLOM() {
		return getOWLClass(":LOM", OntoUtil.LOMPM);
	}

	public OWLClass getEducational() {
		return getOWLClass(":Educational", OntoUtil.LOMPM);
	}
	
	public OWLClass getIdentifier() {
		return getOWLClass(":Identifier", OntoUtil.LOMPM);
	}
	
	public OWLClass getTaxon() {
		return getOWLClass(":Taxon", OntoUtil.LOMPM);
	}
	
	public OWLClass getResource() {
		return getOWLClass(":Resource", OntoUtil.LOMPM);
	}
	
	public OWLClass getTaxonPath() {
		return getOWLClass(":TaxonPath", OntoUtil.LOMPM);
	}
	public OWLClass getClassification() {
		return getOWLClass(":Classification", OntoUtil.LOMPM);
	}
	
	public OWLClass getContentOntology() {
		return getOWLClass(":ContentOntology", OntoUtil.ConceptGraphPM);
	}

	public OWLClass getKObjectByType(String typeName) {
		return getOWLClass(IRI
				.create("http://www.cs.teilar.gr/ontologies/KObject.owl#"
						+ typeName));
	}

	public OWLClass getRelation() {
		return getOWLClass(":Relation", OntoUtil.LOMPM);
	}

	public OWLClass getLOMGeneral() {
		return getOWLClass(":General", OntoUtil.LOMPM);
	}

	public OWLClass getLOMLifecycle() {
		return getOWLClass(":Lifecycle", OntoUtil.LOMPM);
	}

	public OWLClass getLOMTechnical() {
		return getOWLClass(":Technical", OntoUtil.LOMPM);
	}

	public OWLClass getLOMMetaMetadata() {
		return getOWLClass(":MetaMetadata", OntoUtil.LOMPM);
	}

	public OWLClass getLOMEducational() {
		return getOWLClass(":Educational", OntoUtil.LOMPM);
	}

	public OWLClass getLOMRights() {
		return getOWLClass(":Rights", OntoUtil.LOMPM);
	}


	public OWLClass getPhysicalLocation() {
		return getOWLClass(":PhysicalLocation", OntoUtil.KObjectPM);
	}
	
	public OWLClass getControlNode() {
		return getOWLClass(":ControlNode", OntoUtil.XGraphPM);
	}
	
	public OWLClass getLearningActNode() {
		return getOWLClass(":LearningActNode", OntoUtil.XGraphPM);
	}
	
	public OWLClass getXNode(int xNodeType) {
		if(xNodeType == ExecutionGraph.CONTROL_XNODE_TYPE) {
			return getControlNode();
		} else if(xNodeType == ExecutionGraph.DIALOGUE_XNODE_TYPE) {
			return getDialogueNode();
		} else if(xNodeType == ExecutionGraph.LEARNING_ACT_XNODE_TYPE) {
			return getLearningActNode();
		} else if(xNodeType == ExecutionGraph.PAR_GROUP_XNODE_TYPE) {
			return getParGroup();
		} else if(xNodeType == ExecutionGraph.SEQ_GROUP_XNODE_TYPE) {
			return getSeqGroup();
		} 
		
		return null;
	}
	
	public OWLClass getKResource() {
		return getOWLClass(":KResource", OntoUtil.KObjectPM);
	}

	
	public OWLClass getDialogueNode() {
		return getOWLClass(":DialogueNode", OntoUtil.XGraphPM);
	}

	
	
	/******************** CROP OWL Data Expressions ******************/
	
	public OWLObjectProperty getIsStartOf() {
		// from is source of edge 
		return getOWLObjectProperty(
			IRI.create("http://www.cs.teilar.gr/ontologies/Graph.owl#isStartOf"));
	}
	
	public OWLObjectProperty getToNode() {
		// from is source of edge 
		return getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/Graph.owl#toNode"));
	}

	public OWLObjectProperty getFromNode() {
		// from is source of edge 
		return getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/Graph.owl#fromNode"));
	}
	
	public OWLObjectProperty getIsEndOf() {
		return getOWLObjectProperty(
			IRI.create("http://www.cs.teilar.gr/ontologies/Graph.owl#isEndOf"));
	}

	public OWLObjectProperty getHasAssociated() {
		return getOWLObjectProperty(
			IRI.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasAssociated"));
	}

	public OWLObjectProperty getDefines() {
		return getOWLObjectProperty(
			IRI.create("http://www.cs.teilar.gr/ontologies/ConceptGraph.owl#defines"));
	}

	public OWLObjectProperty getHasNode() {
		return getOWLObjectProperty(
    		IRI.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasNode"));
	}

	public OWLObjectProperty getHasPrerequisite() { 
		return getOWLObjectProperty(IRI
				.create("http://www.cs.teilar.gr/ontologies/KConcept.owl#hasPrerequisite"));
	}
	
	public OWLObjectProperty getIsPrerequisiteOf() { 
		return getOWLObjectProperty(IRI
				.create("http://www.cs.teilar.gr/ontologies/KConcept.owl#isPrerequisiteOf"));
	}
	
	
	public OWLObjectProperty getTargets() {
		return getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObject.owl#targets"));
	}
	
	public OWLObjectProperty getIsTargetOf() {
		return getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObject.owl#isTargetOf"));
	}
	
	public OWLObjectProperty getDescribedBy() {
		return getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObject.owl#describedBy"));
	}
	
	public OWLObjectProperty getIsSubOntologyOf() {
		return getOWLObjectProperty(
			IRI.create("http://www.cs.teilar.gr/ontologies/ConceptGraph.owl#isSubOntologyOf"));
	}
	
	public OWLObjectProperty getIsAssociatedOf() { 
		return getOWLObjectProperty(IRI
			.create("http://www.cs.teilar.gr/ontologies/Graph.owl#isAssociatedOf"));
	}

	public OWLObjectProperty getHasElement() {
		return getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#hasElement"));
	}
	public OWLObjectProperty getHasElementComponent() {
		return getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#hasElementComponent"));
	}


	public OWLDataPropertyExpression getLOMProperty(String propertyName) {
		return getOWLDataProperty(IRI.create(
				"http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#" + propertyName));
	}

	public OWLDataPropertyExpression getPhysicalFormat() {
		return getOWLDataProperty(IRI.create(
				"http://www.cs.teilar.gr/ontologies/KObject.owl#physicalFormat"));
	}

	public OWLDataPropertyExpression getHasPhysicalLocation() {
		return getOWLDataProperty(IRI.create(
				"http://www.cs.teilar.gr/ontologies/KObject.owl#physicalLocation"));
	}
	
	public OWLDataPropertyExpression getExplanationParagraph() {
		return getOWLDataProperty(IRI.create(
				"http://www.cs.teilar.gr/ontologies/XGraph.owl#hasExplanationParagraph"));
	}
	
	public OWLDataPropertyExpression getThreshold() {
		return getOWLDataProperty(IRI.create(
				"http://www.cs.teilar.gr/ontologies/XGraph.owl#hasThreshold"));
	}

	public OWLObjectPropertyExpression getIsNodeOf() {
		return getOWLObjectProperty(IRI.create(
				"http://www.cs.teilar.gr/ontologies/Graph.owl#isNodeOf"));
	}

	
	/** Data Properties **/
	public OWLDataPropertyExpression getHasPriorityList() {
		return getOWLDataProperty(IRI.create(
			"http://www.cs.teilar.gr/ontologies/XModel.owl#hasPriorityList"));
	}
	
	public OWLDataPropertyExpression getHasVerboseLevel() {
		return getOWLDataProperty(IRI.create(
		"http://www.cs.teilar.gr/ontologies/XModel.owl#hasVerboseLevel"));
	}
	
	public OWLDataPropertyExpression getHasAlgorithm() {
		return getOWLDataProperty(IRI.create(
		"http://www.cs.teilar.gr/ontologies/XModel.owl#hasAlgorithm"));
	}
}
