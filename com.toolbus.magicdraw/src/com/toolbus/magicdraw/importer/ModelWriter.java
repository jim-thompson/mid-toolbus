package com.toolbus.magicdraw.importer;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jimthompson.magicdraw.util.log;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.project.ProjectDescriptor;
import com.nomagic.magicdraw.core.project.ProjectDescriptorsFactory;
import com.nomagic.magicdraw.core.project.ProjectsManager;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.PresentationElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.uml.DiagramTypeConstants;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;
import com.nomagic.magicdraw.uml.symbols.shapes.ShapeElement;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Association;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Comment;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.components.mdbasiccomponents.Component;
import com.nomagic.uml2.ext.magicdraw.mdusecases.Actor;
import com.nomagic.uml2.ext.magicdraw.mdusecases.Extend;
import com.nomagic.uml2.ext.magicdraw.mdusecases.ExtensionPoint;
import com.nomagic.uml2.ext.magicdraw.mdusecases.Include;
import com.nomagic.uml2.ext.magicdraw.mdusecases.UseCase;
import com.nomagic.uml2.impl.ElementsFactory;
import com.toolbus.magicdraw.importer.UMLConst.UMLType;

public class ModelWriter
{
	private ModelDesc _model_desc;

	// Grab REFERENCE to the map in the model description, for easier coding.
	// Map of BUSE element IDs to BUS element structures
	Map<String, BUSElementStruct> _bus_id_to_bus_element_map;
	
	// Map of BUS element IDs to MagicDraw model elements
	Map<String, Element> _bus_id_to_md_element_map = new LinkedHashMap<>();
	
	// Map of BUS element IDs to diagram presentation elements
	Map<String, PresentationElement> _bus_id_to_pres_element_map = new LinkedHashMap<>();
		
	ProjectsManager 		_proj_manager;
	private Project			_project;
	private Package			_model;
	private ElementsFactory _factory;
	

	ModelWriter(ModelDesc model_description)
	{
		// Save the model description.
		_model_desc = model_description;
		
		// Grab references to the maps in the model description.
		_bus_id_to_bus_element_map = _model_desc._elements_dict;
	}

	public void createMagicDrawModel()
	{
		createModel();
		createCreatorComment();
	}		
	
	public boolean writeModel(String md_file)
	{
		ProjectDescriptor proj_descriptor = ProjectDescriptorsFactory.
				createLocalProjectDescriptor(_project, new File(md_file));
		boolean did_save = _proj_manager.saveProject(proj_descriptor, true);
		
//		dumpElementStructs();

		return did_save;
	}
	
	private void createModel()
	{
		_proj_manager = Application.getInstance().getProjectsManager();
		_project = _proj_manager.createProject();
		_model = _project.getPrimaryModel();
		_factory = _project.getElementsFactory();
		
		boolean found_root_model = findRootModel();
		
		if (!found_root_model)
		{
			log.error("*** ERROR: Unable to find root model. Cannot continue.");
			return;
		}
		
		createElements();
		establishOwnerships();
		establishRelationships();
		
		createDiagrams();
	}
	
	boolean findRootModel()
	{
		int looked_at_elements = 0;
		
		for (String id : _bus_id_to_bus_element_map.keySet())
		{
			BUSElementStruct bus_element = _bus_id_to_bus_element_map.get(id);
			
			// We can expect the root-level model to be the first we encounter.
			if (bus_element._type == UMLConst.UMLType.MODEL)
			{
				bus_element._new_element_id = _model.getID();
				bus_element._added_to_model = true;
				
				log.info("*** Found the root model after " + looked_at_elements + " elements seen");
				
				// Having found the root-level model, there's no sense in looking at
				// the remaining elements. Terminate the loop.
				return true;
			}
			
			looked_at_elements += 1;
		}
		
		return false;
	}
	
	void createElements()
	{
//		StandardProfile standard_profile = StandardProfile.getInstance(_project);
//		Stereotype subsystem = standard_profile.getSubsystem();
		
		_factory = _project.getElementsFactory();
		
		Element added_element = null;
		for (String id : _bus_id_to_bus_element_map.keySet())
		{
			added_element = null;
			BUSElementStruct bus_element = _bus_id_to_bus_element_map.get(id);
			
			switch (bus_element._type)
			{
			case ACTOR:
				Actor actor = _factory.createActorInstance();
				added_element = actor;
				break;
				
			case ASSOCIATION:
				Association association = _factory.createAssociationInstance();
				added_element = association;
				break;
				
			case COMPONENT:
				Component component = _factory.createComponentInstance();
//				StereotypesHelper.addStereotype(component, subsystem);
				added_element = component;
				break;
				
			case EXTEND:
				Extend extend = _factory.createExtendInstance();
				extend.setName(bus_element._name);
				added_element = extend;
				break;
				
			case EXTENSION_POINT:
				ExtensionPoint extension_point = _factory.createExtensionPointInstance();
				added_element = extension_point;
				break;
				
			case INCLUDE:
				Include include = _factory.createIncludeInstance();
				added_element = include;
				break;
				
			case MODEL:
//				Model model = _factory.createModelInstance();
				added_element = _model;
				bus_element._id = _model.getID();
				break;
				
			case PACKAGE:
				Package package_ = _factory.createPackageInstance();
				added_element = package_;
				break;
				
			case USECASE:
				UseCase usecase = _factory.createUseCaseInstance();
				added_element = usecase;
				break;
				
			default:
			}
			
			if (added_element != null)
			{
				_bus_id_to_md_element_map.put(bus_element._id, added_element);
				
				if ((added_element instanceof NamedElement) ||
					(added_element instanceof Extend))
				{
					((NamedElement) added_element).setName(bus_element._name);
				}
			}
		}
	}
	
	static PresentationElementsManager pres_elem_manager = PresentationElementsManager.getInstance();
	static ModelElementsManager model_elem_manager = ModelElementsManager.getInstance();
	static SessionManager session_manager = SessionManager.getInstance();
	
	void createDiagrams()
	{
		for (String id : _bus_id_to_bus_element_map.keySet())
		{
			BUSElementStruct bus_element = _bus_id_to_bus_element_map.get(id);
			
			switch (bus_element._type)
			{
			case MD_DIAGRAM:
				createDiagram(bus_element);
				break;
				
			default:
				break;
			}
		}
	}
	
	void createDiagram(BUSElementStruct bus_element)
	{
		session_manager.createSession(_project, "Create use case.");
		
		try
		{
			String diagram_type = DiagramTypeConstants.UML_USECASE_DIAGRAM;
			Diagram uc_diagram = model_elem_manager.createDiagram(diagram_type, _model);
			DiagramPresentationElement dp_element = _project.getDiagram(uc_diagram);
			
			uc_diagram.setName(bus_element._name);
			
			for (String inner_bus_element_id : bus_element ._relationship_names)
			{
				BUSElementStruct inner_bus_element = _bus_id_to_bus_element_map.get(inner_bus_element_id);
				
				switch (inner_bus_element._type)
				{					
				case MD_ACTOR:
				case MD_USECASE:
				case MD_COMPONENT:
					createShapeElement(dp_element, inner_bus_element);
					break;
				
				default:
					break;
				}
			}

			for (String inner_bus_element_id : bus_element ._relationship_names)
			{
				BUSElementStruct inner_bus_element = _bus_id_to_bus_element_map.get(inner_bus_element_id);
				
				switch (inner_bus_element._type)
				{				
				case MD_EXTEND:
				case MD_INCLUDE:
				case MD_ASSOCIATION:
					createPathElement(dp_element, inner_bus_element);
					break;
					
				default:
					break;
				}
			}

			session_manager.closeSession(_project);
		}
		catch (Exception e)
		{
			log.error("*** ERROR: Unexpected error when creating diagram for " + bus_element._id);
			e.printStackTrace();
		}
	}
	
	void createPathElement(DiagramPresentationElement dp_element, BUSElementStruct bus_element)
			throws ReadOnlyElementException
	{
		try
		{
			String src_element_name = bus_element._relationship_names.get(0);
			String dst_element_name = bus_element._relationship_names.get(1);
			String rel_element_name = bus_element._relationship_names.get(2);

			PresentationElement src_pres_element = _bus_id_to_pres_element_map.get(src_element_name);
			PresentationElement dst_pres_element = _bus_id_to_pres_element_map.get(dst_element_name);

			Element rel_element = _bus_id_to_md_element_map.get(rel_element_name);

//			PathElement path_element =
					pres_elem_manager.createPathElement(rel_element, src_pres_element, dst_pres_element);
			
//			if (path_element instanceof ContainerLinkView)
//			{
//				ContainerLinkView assoc_view = (ContainerLinkView) path_element;
//
//				assoc_view.setShowNameBox(false);
//			}
//
//			if (path_element instanceof LinkWithStereotype)
//			{
//				LinkWithStereotype assoc_view = (LinkWithStereotype) path_element;
//
//				assoc_view.setStereotypesDisplayMode(StereotypesDisplayModeOwner.STEREOTYPE_DISPLAY_MODE_DO_NOT_DISPLAY_STEREOTYPES);
//			}
//			
//			if (path_element instanceof ExtendView)
//			{
//				ExtendView assoc_view = (ExtendView) path_element;
//
//				assoc_view.setShowExtensionPoint(false);
//			}
		}
		catch (Exception e)
		{
			log.error("*** ERROR: Unexpected error when creating diagram for " + bus_element._id);
			e.printStackTrace();
		}

	}
	
	void createShapeElement(DiagramPresentationElement dp_element, BUSElementStruct bus_element)
			throws ReadOnlyElementException
	{
			String represented_element_name;
			Element represented_element = null;
			ShapeElement shape_element = null;

			if (bus_element._relationship_names.size() >= 1)
			{
				represented_element_name = bus_element._relationship_names.get(0);
				represented_element = _bus_id_to_md_element_map.get(represented_element_name);
			}

			log.info(bus_element._type.name() + " (" + bus_element._type_name + ")");

			shape_element = pres_elem_manager.createShapeElement(represented_element, dp_element);

			Rectangle uc_r = new Rectangle(bus_element._xkoord, bus_element._ykoord,
					bus_element._width, bus_element._height);

//			shape_element.setBounds(uc_r);
			pres_elem_manager.reshapeShapeElement(shape_element, uc_r);
			
			_bus_id_to_pres_element_map.put(bus_element._id, shape_element);
	}

	void establishOwnerships()
	{
		for (String owned_id : _bus_id_to_bus_element_map.keySet())
		{
			BUSElementStruct b_owned_element = _bus_id_to_bus_element_map.get(owned_id);
			
			if ((b_owned_element._owner == BUSElementStruct.NONE) ||
				(b_owned_element._owner == null))
			{
				continue;
			}
			
			Element e_owned_element = _bus_id_to_md_element_map.get(owned_id);
			
			String owner_id = b_owned_element._owner._id;
			
			if (b_owned_element._type == UMLType.INCLUDE)
			{
				log.error("Element <" + owned_id + "> owner: <" + owner_id + ">");
			}
			
			Element e_owner = _bus_id_to_md_element_map.get(owner_id);

			if (e_owner == null)
			{
				// If we can't find the element's actual owner, set the owner to be the top-
				// level model. This is what MagicDraw does in certain circumstances, so wo
				// can do the same.
				e_owned_element.setOwner(_model);
			}
			else
			{
				e_owned_element.setOwner(e_owner);
			}
		}			
	}

	void establishRelationships()
	{
		for (String owned_id : _bus_id_to_bus_element_map.keySet())
		{
			BUSElementStruct b_element = _bus_id_to_bus_element_map.get(owned_id);
		
			switch (b_element._type)
			{
			case INCLUDE:
			case EXTEND:
				connectDirectedRelationship(b_element);
				break;
				
			case ASSOCIATION:
				connectAssociation(b_element);
				break;
				
			default:
				// None of the remaining element types have relationships that need to be 
				// connected.
				break;
			}
		}		
	}
	
	void connectAssociation(BUSElementStruct b_element)
	{
		String b_source_element_name = b_element._relationship_names.get(0);
		String b_target_element_name = b_element._relationship_names.get(1);
		
		Element directed_relationship = _bus_id_to_md_element_map.get(b_element._id);
		Element source_element = _bus_id_to_md_element_map.get(b_source_element_name);
		Element target_element = _bus_id_to_md_element_map.get(b_target_element_name);
		
		ModelHelper.setClientElement(directed_relationship, source_element);
		ModelHelper.setSupplierElement(directed_relationship, target_element);

		Association association = (Association) directed_relationship;
		Property p0 = association.getMemberEnd().get(0);
		Property p1 = association.getMemberEnd().get(1);
		
		ModelHelper.setNavigable(p0, true);
		ModelHelper.setNavigable(p1, true);
	}
	
	void connectDirectedRelationship(BUSElementStruct b_element)
	{
		String b_target_element_name = b_element._relationship_names.get(0);
		String b_source_element_name = b_element._relationship_names.get(1);
		
		Element directed_relationship = _bus_id_to_md_element_map.get(b_element._id);
		Element source_element = _bus_id_to_md_element_map.get(b_source_element_name);
		Element target_element = _bus_id_to_md_element_map.get(b_target_element_name);
		
		// NOTE: for some elements deriving from DirectedRelationship (e.g. Include), this
		// establishes ownership (regardless of the previous setOwner call):
		ModelHelper.setClientElement(directed_relationship, source_element);
		ModelHelper.setSupplierElement(directed_relationship, target_element);
	}
	
	@SuppressWarnings("unused")
	private void dumpElementStructs()
	{
		for (String id : _bus_id_to_bus_element_map.keySet())
		{
			BUSElementStruct element = _bus_id_to_bus_element_map.get(id);
			
			log.info();
			element.dump(_bus_id_to_bus_element_map);
		}
	}
	
	private void createCreatorComment()
	{
		Comment c = _factory.createCommentInstance();
		c.setBody("Hello, world");
		c.setOwningElement(_model);
	}
}
