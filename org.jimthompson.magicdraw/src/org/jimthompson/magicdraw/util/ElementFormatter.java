// Copyright © 2019 MID GmbH 
// smartfacts support for MagicDraw and Cameo
// Author: James Thompson <jim.thompson@pobox.com>

package org.jimthompson.magicdraw.util;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Association;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Classifier;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Comment;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.InstanceSpecification;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralInteger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralUnlimitedNatural;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Slot;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.StructuralFeature;

public class ElementFormatter
{
	static Pattern prefix_pattern = Pattern.compile(".*(_[0-9]+_[0-9]+)");

	int 		ef_depth = 0;
	String 		ef_indentation_text = "";
	
	public void setDepth(int depth)
	{
		if (depth != ef_depth)
		{
			// Record the depth
			ef_depth = depth;

			// Build up the string to print before the element text. This indentation
			// will depend on the depth of the element, thus allowing the printout
			// to show the structure of the model elements.
			
			ef_indentation_text = "";

			for (int i = 0; i < ef_depth; i++)
			{
				ef_indentation_text += "----";
			}		
		}
	}
	
	public void simplyShowElement(Element element)
	{
		log.info(elementDisplayText(element));
	}
	
	public void showElementNested(Element element)
	{
		String short_name = shortElementName(element);
		log.debug(ef_indentation_text + "> [" + element.getHumanType() + "] \"" + short_name +
				  "\" <" + element.getID() + ">");
	}

	public void showElement(Element element)
	{
		String additional_stuff = " ";
		String type = element.getHumanType();
		
		if (type.equals("Comment"))
		{
			Comment c = (Comment) element;
			additional_stuff += c.getBody();
			additional_stuff = additional_stuff.replace('\n', ' ');
		}
		else if (type.equals("Association") || type.equals("Association Class"))
		{
			Association a = (Association) element;
			Collection<Property> properties = a.getMemberEnd();
			additional_stuff += "{";
			for (Property p : properties)
			{
				additional_stuff += " * " + p.getName();
				additional_stuff += " : " + ElementFormatter.short_id(p);
			}
			additional_stuff += " }";
		}
		else if (type.equals("Depndency") || type.equals("Usage"))
		{
			
		}
		else if (type.equals("Package"))
		{
			Package p = (Package) element;
			additional_stuff += p.getURI();
		}
		else if (type.equals("Slot"))
		{
			Slot s = (Slot) element;
			StructuralFeature sf = s.getDefiningFeature();
			additional_stuff +=
					"[" + elementDisplayName(sf) + "]";
		}
		else if (type.equals("Property"))
		{
			Property p = (Property) element;
			additional_stuff += "[" + p.getName() + /*"/" + p.getClassType() +*/ "]";
			Association a = p.getAssociation();
			if (a != null)
			{
				additional_stuff += " [" + a.getHumanName() + " / " + ElementFormatter.short_id(a) + "]";
			}
		}
		else if (type.equals("Literal String"))
		{
			LiteralString s = (LiteralString) element;
			additional_stuff += "[\"" + s.getValue() + "\"]";
		}
		else if (type.equals("Literal Unlimited Natural"))
		{
			LiteralUnlimitedNatural lun = (LiteralUnlimitedNatural) element;
			additional_stuff += "[" + lun.getValue() + "]";
		}
		else if (type.equals("Literal Integer"))
		{
			LiteralInteger li = (LiteralInteger) element;
			additional_stuff += "[" + li.getValue() + "]";
		}
		else if (type.equals("Instance Specification"))
		{
			InstanceSpecification ispec = (InstanceSpecification) element;
			List<Classifier> classifiers = ispec.getClassifier();
			
			String sep = "";
			String c_names = "";
			for (Classifier c : classifiers)
			{
				c_names = elementDisplayName(c) + sep;
				sep = ", ";
			}
			
			additional_stuff = "[ " + c_names + " ]";
		}
		else
		{
			additional_stuff = "";
		}
				
		String id = element.getID() + "                                                    ";
		id = id.substring(0, 44);
//		log.info(id, elementDisplayText(element) + " [" + additional_stuff + "]");
		log.info(id, elementDisplayText(element));
	}

	public String elementDisplayName(Element element)
	{
		String human_name = element.getHumanName();
		String human_type = element.getHumanType();
		
		if (human_name.equals(human_type))
		{
			// This is essentially an anonymous type.
			//human_name = "-";
			human_name = short_id(element);
		}
		else
		{
			String prefix = human_type + " ";
			if (human_name.startsWith(prefix))
			{
				human_name = human_name.substring(prefix.length());
			}
			
			//human_name = "\"" + human_name + "\"" + " (" + short_id(element) + ")";
		}
		
		String classname = element.getClass().getSimpleName();
		
		return human_name + " of type " + human_type + " <<" + classname + ">>";
	}

	public String elementDisplayName(DiagramPresentationElement diagram)
	{
		String human_name = diagram.getHumanName();
		String human_type = diagram.getHumanType();
		
		return human_name + " of type " + human_type;
	}
	
	String elementDisplayText(Element element)
	{
		return ef_indentation_text + elementDisplayName(element);
	}
	
	public void showMessage(String m)
	{
		log.info(ef_indentation_text + m);
	}
	
	public static String shortElementName(Element element)
	{
		String name = element.getHumanName();
		String type = element.getHumanType();
		
		if (name.equals(type))
		{
			// This is essentially an anonymous type.
//			name = "< >"; // This is how elements with empty names are displayed in MagicDraw
			name = "-";
//			name = "(unnamed)";
		}
		else
		{
			String prefix = type + " ";
			
			if (name.startsWith(prefix))
			{
				name = name.substring(prefix.length());
			}
		}
		
		return name;
	}

	static String short_id(Element e)
	{
		return "-";
//		return "< >";
		//return w.e(e).getID();
//		if (e == null)
//		{
//			log.error("OOPS!");
//			return "OOPS";
//		}
//		
//		//String s = w.e(e).getID();
//		String s = e.getID();
//		Matcher m = prefix_pattern.matcher(s);
//		if (m.matches())
//		{
//			return m.group(1);
//		}
//		
//		return "???";
	}
}

// Local Variables:
// tab-width: 4
// fill-column: 108
// End:
