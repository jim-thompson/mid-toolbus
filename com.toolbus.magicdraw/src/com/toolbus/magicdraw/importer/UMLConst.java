package com.toolbus.magicdraw.importer;

public class UMLConst
{
	public enum UMLType
	{
		// These represent standard UML types
		ACTOR,
		ASSOCIATION,
		COMPONENT,
		DEPENDENCY,
		EXTEND,
		EXTENSION_POINT,
		INCLUDE,
		MODEL,
		PACKAGE,
		USECASE,

		// These represent pseudo-types used by MagicDraw
		MD_ACTOR,
		MD_ASSOCIATION,
		MD_COMPONENT,
//		MD_DEPENDENCY,
		MD_EXTEND,
		MD_INCLUDE,
		MD_MODEL,
		MD_PACKAGE,
		MD_USECASE,

		MD_DIAGRAM,

		UNKNOWN,
	}

	// Standard UML types
	public static String UML_ACTOR_TNAME = "uml:Actor";
	public static String UML_ASSOCIATION_TNAME = "uml:Association";
	public static String UML_COMPONENT_TNAME = "uml:Component";
	public static String UML_DEPENDENCY_TNAME = "uml:Dependency";
	public static String UML_EXTEND_TNAME = "uml:Extend";
	public static String UML_EXTENSION_POINT_TNAME = "uml:ExtensionPoint";
	public static String UML_INCLUDE_TNAME = "uml:Include";
	public static String UML_MODEL_TNAME = "uml:Model";
	public static String UML_PACKAGE_TNAME = "uml:Package";
	public static String UML_USECASE_TNAME = "uml:UseCase";

	// MadicDraw pseudo-types
	public static String MD_ACTOR_TNAME = "Actor";
	public static String MD_ASSOCIATION_TNAME = "Association";
	public static String MD_COMPONENT_TNAME = "Component";
//	public static String MD_DEPENDENCY_TNAME = "Dependency";
	public static String MD_EXTEND_TNAME = "Extend";
	public static String MD_INCLUDE_TNAME = "Include";
	public static String MD_MODEL_TNAME = "Model";
	public static String MD_PACKAGE_TNAME = "Package";
	public static String MD_USECASE_TNAME = "UseCase";

	public static String MD_DIAGRAM_TNAME = "uml:Diagram";

	public static UMLType tnameToType(String tname)
	{
		     if (UML_ACTOR_TNAME.compareTo(tname) == 0) return UMLType.ACTOR;
		else if (UML_ASSOCIATION_TNAME.compareTo(tname) == 0) return UMLType.ASSOCIATION;
		else if (UML_COMPONENT_TNAME.compareTo(tname) == 0) return UMLType.COMPONENT;
		else if (UML_DEPENDENCY_TNAME.compareTo(tname) == 0) return UMLType.DEPENDENCY;
		else if (UML_EXTEND_TNAME.compareTo(tname) == 0) return UMLType.EXTEND;
		else if (UML_EXTENSION_POINT_TNAME.compareTo(tname) == 0) return UMLType.EXTENSION_POINT;
		else if (UML_INCLUDE_TNAME.compareTo(tname) == 0) return UMLType.INCLUDE;
		else if (UML_MODEL_TNAME.compareTo(tname) == 0) return UMLType.MODEL;
		else if (UML_PACKAGE_TNAME.compareTo(tname) == 0) return UMLType.PACKAGE;
		else if (UML_USECASE_TNAME.compareTo(tname) == 0) return UMLType.USECASE;

		else if (MD_ACTOR_TNAME.compareTo(tname) == 0) return UMLType.MD_ACTOR;
		else if (MD_ASSOCIATION_TNAME.compareTo(tname) == 0) return UMLType.MD_ASSOCIATION;
		else if (MD_COMPONENT_TNAME.compareTo(tname) == 0) return UMLType.MD_COMPONENT;
////		else if (MD_DEPENDENCY_TNAME.compareTo(tname) == 0) return UMLType.MD_DEPENDENCY;
		else if (MD_EXTEND_TNAME.compareTo(tname) == 0) return UMLType.MD_EXTEND;
		else if (MD_INCLUDE_TNAME.compareTo(tname) == 0) return UMLType.MD_INCLUDE;
//		else if (MD_MODEL_TNAME.compareTo(tname) == 0) return UMLType.MD_MODEL;
//		else if (MD_PACKAGE_TNAME.compareTo(tname) == 0) return UMLType.MD_PACKAGE;
		else if (MD_USECASE_TNAME.compareTo(tname) == 0) return UMLType.MD_USECASE;

		else if (MD_DIAGRAM_TNAME.compareTo(tname) == 0) return UMLType.MD_DIAGRAM;

		return UMLType.UNKNOWN;
	}

	public static String typeToTName(UMLType type)
	{
		switch (type)
		{
		case ACTOR: return UML_ACTOR_TNAME;
		case ASSOCIATION: return UML_ASSOCIATION_TNAME;
		case COMPONENT: return UML_COMPONENT_TNAME;
		case DEPENDENCY: return UML_DEPENDENCY_TNAME;
		case EXTEND: return UML_EXTEND_TNAME;
		case EXTENSION_POINT: return UML_EXTENSION_POINT_TNAME;
		case INCLUDE: return UML_INCLUDE_TNAME;
		case MODEL: return UML_MODEL_TNAME;
		case PACKAGE: return UML_PACKAGE_TNAME;
		case USECASE: return UML_USECASE_TNAME;

		case MD_ACTOR: return MD_ACTOR_TNAME;
		case MD_ASSOCIATION: return MD_ASSOCIATION_TNAME;
		case MD_COMPONENT: return MD_COMPONENT_TNAME;
//		case MD_DEPENDENCY: return MD_DEPENDENCY_TNAME;
		case MD_EXTEND: return MD_EXTEND_TNAME;
		case MD_INCLUDE: return MD_INCLUDE_TNAME;
		case MD_MODEL: return MD_MODEL_TNAME;
		case MD_PACKAGE: return MD_PACKAGE_TNAME;
		case MD_USECASE: return MD_USECASE_TNAME;

		case MD_DIAGRAM: return MD_DIAGRAM_TNAME;

		case UNKNOWN: return null;
		}

		return null;
	}
}
