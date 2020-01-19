// Copyright © 2019 MID GmbH 
// smartfacts support for MagicDraw and Cameo
// Author: James Thompson <jim.thompson@pobox.com>

package org.jimthompson.magicdraw.util;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.nomagic.magicdraw.uml.BaseElement;
import com.nomagic.uml2.ext.jmi.helpers.InstanceSpecificationHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.InstanceSpecification;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Slot;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.StructuralFeature;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class ElementDumper
{
	public static void dumpInstanceSpecification(Element element)
	{
		if (element instanceof InstanceSpecification)
		{
			InstanceSpecification instance_spec = (InstanceSpecification) element;
			
			log.debug("**-- Instance Specification <" + instance_spec.getName() + ">");

			for (Slot slot : instance_spec.getSlot())
			{
				String value = InstanceSpecificationHelper.getValueBySlot(slot).toString();

				StructuralFeature feature = slot.getDefiningFeature();
				String name = feature.getName();
				
				log.debug("**---- Name: <" + name + ">, Value: <" + value + ">");
			}
		}
	}
	
	public static void dumpMultiplicityElement(Element element)
	{
//		log.debug("***** MULTIPLICITY ELEMENT *****");
//		dumpElement(element);
	}
	
	public static void dumpElement(Element element)
	{
		EClass e_class = element.eClass();
		log.debug(String.format("** EClass name <%s>", e_class.getName()));
		
		InstanceSpecification inspec = element.getAppliedStereotypeInstance();
		
		if (inspec != null)
		{
			log.debug("** Applied stereotype instance (InstanceSpecification):");
			dumpInstanceSpecification(inspec);
			
			Element foo = inspec.getStereotypedElement();
			log.debug("** stereotyped element = <" + foo.getHumanName() + ">");
			
			ValueSpecification vspec = inspec.getSpecification();

			if (vspec != null)
			{
				log.debug("** value spec <" + vspec.getHumanName() + ">");
			}
		}
		
		List<Stereotype> stereotypes = StereotypesHelper.getStereotypes(element);
//		Stereotype s;
		for (Stereotype s : stereotypes)
		{
			log.debug("** stereotype <" + s.getName() + ">");
			List<Property> tags = StereotypesHelper.getPropertiesWithDerivedOrdered(s);
			
			for (Property p : tags)
			{
				log.debug("**** property <" + p.getName() + ">");
				
				for (Object o : StereotypesHelper.getStereotypePropertyValue(element, s, p))
				{
					log.debug("****** value <" + o.getClass().getName() + "> [" + o.toString() + "]");
					
					if (o instanceof BaseElement)
					{
						BaseElement be = (BaseElement) o;
						
						log.debug("             (" + be.getHumanName() + ")");
					}
				}
			}
		}
		
		EList<EStructuralFeature> features = e_class.getEAllStructuralFeatures(); 
		for (EStructuralFeature feature : features)
		{
			// Get the name of the attribute
			String name = feature.getName();
			String type = getFeatureType(feature);
			String value = getAttributeValue(element, feature);
			
//			if ("EReference".equals(type) || "".equals(value)) continue;
			
			log.debug(String.format("* Feature: <%s>, Type: <%s>, Value: <%s>",
					                 name, type, value));
//			log.debug("Feature: <" + name + ">, Type: <" + type +">");
		}
	}
	
	private static String getAttributeValue(Element element, EStructuralFeature feature)
	{
		if (feature instanceof EAttribute)
		{
			Object value_object = element.eGet(feature);
			
			if (value_object == null)
			{
				return "";
			}
			
			return value_object.toString();
		}
		
		return "-";
	}
	
	private static String getFeatureType(EStructuralFeature feature)
	{
		if (feature instanceof EAttribute)
		{
			return "EAttribute";
		}
		else if (feature instanceof EReference)
		{
			return "EReference";
		}
		
		return "UNKNOWN";
	}
}

// Local Variables:
// tab-width: 4
// fill-column: 102
// End:
