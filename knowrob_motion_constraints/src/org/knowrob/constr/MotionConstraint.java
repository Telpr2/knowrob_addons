package org.knowrob.constr;

import java.util.ArrayList;
import java.util.Arrays;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import controlP5.ControlP5;

import processing.core.PApplet;

public class MotionConstraint {

	protected String name = "";
	protected ArrayList<String> types;

	protected boolean active = true;

	protected double constrLowerLimit;
	protected double constrUpperLimit;

	protected MotionConstraintTemplate template;
	
	public static int CONSTRAINT_BOX_WIDTH  = 170;
	public static int CONSTRAINT_BOX_HEIGHT = 80;

	

	public MotionConstraint() {

		types = new ArrayList<String>();

	}

	public MotionConstraint(String name, String[] types, boolean active, double constrLowerLimit, double constrUpperLimit, MotionConstraintTemplate template, ControlP5 controlP5) {

		this();

		this.name = name;
		this.active = active;
		this.types = new ArrayList<String>(Arrays.asList(types));
		
		this.constrLowerLimit = constrLowerLimit;
		this.constrUpperLimit = constrUpperLimit;

		this.template = template;
		

		controlP5.addTextfield(name + "_name").setWidth(140).setText(name).setCaptionLabel("").setColor(0).setColorForeground(0).setColorBackground(255);

		controlP5.addTextfield(name + "_lower").setWidth(40).setText(""+constrUpperLimit).setCaptionLabel("lower").setColor(0).setColorForeground(0).setColorBackground(255).getCaptionLabel().setColor(80);
		controlP5.addTextfield(name + "_upper").setWidth(40).setText(""+constrLowerLimit).setCaptionLabel("upper").setColor(0).setColorForeground(0).setColorBackground(255).getCaptionLabel().setColor(80);

		// create a toggle
		controlP5.addToggle(name + "_active").setSize(17,17).setValue(active).setCaptionLabel("active").setColorBackground(255).setColorForeground(130).setColorActive(130).getCaptionLabel().setColor(80);

	}


	public void draw(PApplet c, int x, int y, ControlP5 controlP5) {

		controlP5.get(name + "_name").setPosition(x+15, y+15);

		controlP5.get(name + "_lower").setPosition(x+15, y+35);
		controlP5.get(name + "_upper").setPosition(x+60, y+35);
		controlP5.get(name + "_active").setPosition(x+105, y+36);

		c.fill(255);
		c.rect(x,y,CONSTRAINT_BOX_WIDTH, CONSTRAINT_BOX_HEIGHT);

		c.fill(100);


	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public MotionConstraintTemplate getTemplate() {
		return template;
	}

	public void setTemplate(MotionConstraintTemplate tmpl) {
		this.template = tmpl;
	}

	public ArrayList<String> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public double getConstrLowerLimit() {
		return constrLowerLimit;
	}

	public void setConstrLowerLimit(double constrLowerLimit) {
		this.constrLowerLimit = constrLowerLimit;
	}

	public double getConstrUpperLimit() {
		return constrUpperLimit;
	}

	public void setConstrUpperLimit(double constrUpperLimit) {
		this.constrUpperLimit = constrUpperLimit;
	}


	public OWLClass writeToOWL(OWLOntologyManager manager, OWLDataFactory factory, DefaultPrefixManager pm, OWLOntology ontology) {

		String KNOWROB = "http://ias.cs.tum.edu/kb/knowrob.owl#";

		// Base IRI for motion constraints ontology	
		String CONSTR = "http://ias.cs.tum.edu/kb/motion-constraints.owl#";


		OWLObjectProperty constrainedBy   = factory.getOWLObjectProperty(IRI.create(KNOWROB + "constrainedBy"));

		OWLObjectProperty toolFeature     = factory.getOWLObjectProperty(IRI.create(CONSTR + "toolFeature"));
		OWLObjectProperty worldFeature    = factory.getOWLObjectProperty(IRI.create(CONSTR + "worldFeature"));

		OWLDataProperty constrLowerLimit  = factory.getOWLDataProperty(IRI.create(CONSTR + "constrLowerLimit"));
		OWLDataProperty constrUpperLimit  = factory.getOWLDataProperty(IRI.create(CONSTR + "constrUpperLimit"));
		OWLDataProperty constrWeight      = factory.getOWLDataProperty(IRI.create(CONSTR + "constrWeight"));


		// determine constraint type and create subclass
		String constr_t = CONSTR + types.get(0); // TODO: read all types here
		String constr_n = CONSTR + name;

		OWLClass constrType = factory.getOWLClass(IRI.create(constr_t));
		OWLClass constrCls = factory.getOWLClass(IRI.create(constr_n));
		manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(constrCls, constrType)));


//		// annotate subclass with feature values
//		OWLNamedIndividual tool = factory.getOWLNamedIndividual(IRI.create(KNOWROB + this.toolFeature));
//		OWLClassExpression toolFeatureRestr = factory.getOWLObjectHasValue(toolFeature, tool);
//		manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(constrCls, toolFeatureRestr))); 
//
//		OWLNamedIndividual world = factory.getOWLNamedIndividual(IRI.create(KNOWROB + this.worldFeature));
//		OWLClassExpression worldFeatureRestr = factory.getOWLObjectHasValue(worldFeature, world);
//		manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(constrCls, worldFeatureRestr))); 

//
//		OWLClass constrVal = factory.getOWLClass(IRI.create(OWLThing.getUniqueID(constr_n)));
//		manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(constrVal, constrCls)));
//
//		// set properties
//		if(active)
//		OWLClassExpression weightRestr = factory.getOWLDataHasValue(constrWeight, factory.getOWLLiteral(1.0));
//		manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(constrVal, weightRestr))); 
//
//		OWLClassExpression lowerLimitRestr = factory.getOWLDataHasValue(constrLowerLimit, factory.getOWLLiteral(val.pos_lo[i]));
//		manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(constrVal, lowerLimitRestr))); 
//		OWLClassExpression upperLimitRestr = factory.getOWLDataHasValue(constrUpperLimit, factory.getOWLLiteral(val.pos_hi[i]));
//		manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(constrVal, upperLimitRestr))); 
//
//
//		// TODO: link constraints to phases in the task
//
//		OWLClassExpression constrainedByRestr = factory.getOWLObjectSomeValuesFrom(constrainedBy, constrVal);
//		manager.applyChange(new AddAxiom(ontology, factory.getOWLSubClassOfAxiom(phases_cls.get(i), constrainedByRestr))); 
//
//	
		return constrCls;

	}
	

	public void readFromOWL(OWLOntology ont, OWLClass constrCls) {
		
	}

}
