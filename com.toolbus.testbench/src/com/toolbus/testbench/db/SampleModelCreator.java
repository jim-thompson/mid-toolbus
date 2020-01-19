// Copyright (c) MID GmbH. All Rights Reserved.
 
package com.toolbus.testbench.db;

import java.awt.Point;
import java.awt.Rectangle;
import org.jimthompson.magicdraw.util.log;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.project.ProjectsManager;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.PresentationElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.uml.DiagramTypeConstants;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.magicdraw.uml.symbols.shapes.ActorView;
import com.nomagic.magicdraw.uml.symbols.shapes.ShapeElement;
import com.nomagic.magicdraw.usecasescenarios.scenarios.AlternativeCondition;
import com.nomagic.magicdraw.usecasescenarios.scenarios.ExceptionType;
import com.nomagic.magicdraw.usecasescenarios.scenarios.FlowStep;
import com.nomagic.magicdraw.usecasescenarios.scenarios.Scenario;
import com.nomagic.magicdraw.usecasescenarios.scenarios.ScenarioManager;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Comment;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdusecases.Actor;
import com.nomagic.uml2.ext.magicdraw.mdusecases.UseCase;
import com.nomagic.uml2.impl.ElementsFactory;

public class SampleModelCreator
{
	private Project			_project;
	private Package			_model;
	private ElementsFactory _factory;
	private UseCase			_usecase;
	private Actor			_actor;
	private Package			_uc_package;
	private Diagram			_uc_diagram;
	
	public void createModel()
	{
		try
		{
			ProjectsManager proj_manager = Application.getInstance().getProjectsManager();

			log.info("Creating project...");
			_project = proj_manager.createProject();
			_model = _project.getPrimaryModel();
			_factory = _project.getElementsFactory();

			log.info("Creating creator comment...");
			createCreatorComment();

			log.info("Creating use case...");
			createUseCase();
			createUseCaseDiagram();
		}
		catch (Exception e)
		{
			log.error("*** ABEND ***");
			e.printStackTrace();
		}
	}
	
	private void createUseCase()
	{
		_uc_package = _factory.createPackageInstance();
		_uc_package.setName("Use Cases");
		_uc_package.setOwner(_model);
//		_uc_package.setOwningPackage(_model);
		
		_usecase = _factory.createUseCaseInstance();
		_usecase.setName("The Use Case");
//		_usecase.setOwningPackage(_uc_package);
		_usecase.setOwner(_uc_package);
		
		_actor = _factory.createActorInstance();
		_actor.setName("The Actor");
//		_actor.setOwningPackage(_uc_package);
		_actor.setOwner(_uc_package);
	}
	
	private void createCreatorComment()
	{
		Comment c = _factory.createCommentInstance();
		c.setBody("Hello, world");
		c.setOwningElement(_model);
	}
	
	private void createUseCaseDiagram() throws ReadOnlyElementException
	{
		SessionManager.getInstance().createSession(_project, "Create use case stuff");
		_uc_diagram = ModelElementsManager.getInstance().createDiagram(DiagramTypeConstants.UML_USECASE_DIAGRAM,
				_uc_package, true, false);
		_uc_diagram.setName("The UC Diagram");
		
		PresentationElementsManager pe_manager = PresentationElementsManager.getInstance();
		DiagramPresentationElement dpe = _project.getDiagram(_uc_diagram);
		
		ShapeElement se = pe_manager.createShapeElement(_actor, dpe);
		Rectangle r = new Rectangle(20, 20);
		ActorView av = (ActorView) se;
		
		if (av != null)
		{
			pe_manager.reshapeShapeElement(av, r);
			pe_manager.movePresentationElement(av, new Point(200, 200));
			
//			av.setBounds(r);
//			av.setLocation(200, 200);
		}
		
		SessionManager.getInstance().closeSession(_project);
	}
	
	@SuppressWarnings("unused")
	private void createActivityDiagram()
	{
		SessionManager.getInstance().createSession(_project, "Create use case stuff");
		
		// Creates a use case scenario.
		Scenario scenario = ScenarioManager.createScenario(_usecase);
		// Sets the scenario name.
		scenario.setName("Extract money from ATM.");
		 
		// Adds a basic flow step.
		FlowStep flowStep1 = scenario.addFlowStep();
		// Sets a name for the basic flow step.
		flowStep1.setName("Insert card");
		 
		FlowStep flowStep2 = scenario.addFlowStep();
		flowStep2.setName("Enter pin");
		 
		FlowStep flowStep3 = scenario.addFlowStep();
		flowStep3.setName("Good bye");
		 
		// Adds an alternative condition for the basic flow step.
		AlternativeCondition condition = scenario.addAlternativeCondition(flowStep2);
		// Sets a condition guard.
		condition.setIfCondition("Pin correct");
		 
		// Sets a name for the alternative flow step.
		FlowStep flowStep = condition.getAlternativeFlowSteps().get(0);
		flowStep.setName("Extract money");
		// Adds an exception type to the basic flow step.
		ExceptionType exceptionType = scenario.addExceptionType(flowStep2);
		 
		// Sets a name for the exception type.
		exceptionType.setName("Card expired");
		// Sets a name for the exceptional flow step.
		FlowStep exceptionalFlowStep = exceptionType.getExceptionalFlowSteps().get(0);
		exceptionalFlowStep.setName("Inform customer about expired card");
		SessionManager.getInstance().closeSession(_project);
		 
		// Opens and layouts the Activity diagram.
		ScenarioManager.displayScenario(scenario, true, true, "Open ATM Scenario");		
	}
}
