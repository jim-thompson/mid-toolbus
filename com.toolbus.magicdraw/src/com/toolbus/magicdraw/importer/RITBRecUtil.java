package com.toolbus.magicdraw.importer;

import com.toolbus.riinc.RIGlobal;
import com.toolbus.riinc.RITBRec;

public class RITBRecUtil
{
	public static String putFields(RITBRec ritb_rec)
	{
		String result;
		String nresult;
		String alt_1 = ritb_rec.aname_s;
		ritb_rec.aname_s = "X ";
		nresult =  (
	             ((ritb_rec.aname_s + RIGlobal.spaces40).substring(0,2)) + "|" +
	             ((ritb_rec.modus + RIGlobal.spaces40).substring(0,3)) + "|" +
	             ((ritb_rec.modpri + RIGlobal.spaces40).substring(0,2)) + "|" +
	             ((ritb_rec.relnr13 + RIGlobal.spaces40).substring(0,3)) + "|" +
	             ((ritb_rec.relnr4 + RIGlobal.spaces40).substring(0,1)) + "|" +
	             ((ritb_rec.subtyp1 + RIGlobal.spaces40).substring(0,8)) + "|" +
	             ((ritb_rec.subtyp2 + RIGlobal.spaces40).substring(0,8)) + "|" +
	             ((ritb_rec.subtypr + RIGlobal.spaces40).substring(0,8)) + "|" +
	             ((ritb_rec.entity1 + RIGlobal.spaces40).substring(0,35)) + "|" +
	             ((ritb_rec.entity2 + RIGlobal.spaces40).substring(0,35)) + "|" +
	             RIGlobal.spaces40.substring(0,30) + "|" +
	             ((ritb_rec.aname + RIGlobal.spaces40).substring(0,8)) + "|" +
	             ((ritb_rec.vaname + RIGlobal.spaces40).substring(0,3)) + "|" +
	             ((ritb_rec.acont + RIGlobal.spaces40).substring(0,2)) + "|" +
	             ritb_rec.awert
	             ).trim();
		result = ((alt_1 +  "  ").substring(0,2)) + nresult.substring(2);
		ritb_rec.aname_s = alt_1;
		return result;
	}
	
	// Same as above, but omitting fields that are always blank (in the samples I've seen).
	
	public static String putFieldsCompact(RITBRec ritb_rec)
	{
		String result;
		String nresult;
		String alt_1 = ritb_rec.aname_s;
		ritb_rec.aname_s = "X ";
		nresult =  (
             ((ritb_rec.aname_s + RIGlobal.spaces40).substring(0,2)) + "|" +
             ((ritb_rec.modus + RIGlobal.spaces40).substring(0,3)) + "|" +
//             ((ritb_rec.modpri + RIGlobal.spaces40).substring(0,2)) + "|" +
             ((ritb_rec.relnr13 + RIGlobal.spaces40).substring(0,3)) + //"|" +
             ((ritb_rec.relnr4 + RIGlobal.spaces40).substring(0,1)) + "|" +
//             ((ritb_rec.subtyp1 + RIGlobal.spaces40).substring(0,8)) + "|" +
//             ((ritb_rec.subtyp2 + RIGlobal.spaces40).substring(0,8)) + "|" +
             ((ritb_rec.subtypr + RIGlobal.spaces40).substring(0,8)) + "|" +
             ((ritb_rec.entity1 + RIGlobal.spaces40).substring(0,35)) + "|" +
             ((ritb_rec.entity2 + RIGlobal.spaces40).substring(0,35)) + "|" +
//             RIGlobal.spaces40.substring(0,30) + "|" +
             ((ritb_rec.aname + RIGlobal.spaces40).substring(0,8)) + "|" +
             ((ritb_rec.vaname + RIGlobal.spaces40).substring(0,3)) + "|" +
//             ((ritb_rec.acont + RIGlobal.spaces40).substring(0,2)) + "|" +
             ritb_rec.awert
             ).trim();
		result = ((alt_1 +  "  ").substring(0,2)) + nresult.substring(2);
		ritb_rec.aname_s = alt_1;
		return result;
	}

	public static String putFieldsCompactHdr()
	{
		return "  |" +
//	             (("TY" + RIGlobal.spaces40).substring(0,2)) + "|" +
	             (("MOD" + RIGlobal.spaces40).substring(0,3)) + "|" +
//	             (("3" + RIGlobal.spaces40).substring(0,2)) + "|" +
	             (("REL" + RIGlobal.spaces40).substring(0,3)) + //"|" +
	             ((" " + RIGlobal.spaces40).substring(0,1)) + "|" +
//	             (("6" + RIGlobal.spaces40).substring(0,8)) + "|" +
//	             (("7" + RIGlobal.spaces40).substring(0,8)) + "|" +
	             (("       " + RIGlobal.spaces40).substring(0,8)) + "|" +
	             (("      " + RIGlobal.spaces40).substring(0,35)) + "|" +
	             (("      " + RIGlobal.spaces40).substring(0,35)) + "|" +
//	             RIGlobal.spaces40.substring(0,30) + "|" +
	             (("     " + RIGlobal.spaces40).substring(0,8)) + "|" +
	             (("VAN" + RIGlobal.spaces40).substring(0,3)) + "|" +
//	             (("D" + RIGlobal.spaces40).substring(0,2)) + "|" +
	             "     ";
	}

	public static String putFieldsCompactHdrCont()
	{
		return "  |" +
//	             (("P " + RIGlobal.spaces40).substring(0,2)) + "|" +
	             (("US " + RIGlobal.spaces40).substring(0,3)) + "|" +
//	             (("3" + RIGlobal.spaces40).substring(0,2)) + "|" +
	             (("NR " + RIGlobal.spaces40).substring(0,3)) + //"|" +
	             ((" " + RIGlobal.spaces40).substring(0,1)) + "|" +
//	             (("6" + RIGlobal.spaces40).substring(0,8)) + "|" +
//	             (("7" + RIGlobal.spaces40).substring(0,8)) + "|" +
	             (("SUBTYPR" + RIGlobal.spaces40).substring(0,8)) + "|" +
	             (("ENAME1" + RIGlobal.spaces40).substring(0,35)) + "|" +
	             (("ENAME2" + RIGlobal.spaces40).substring(0,35)) + "|" +
//	             RIGlobal.spaces40.substring(0,30) + "|" +
	             (("ANAME" + RIGlobal.spaces40).substring(0,8)) + "|" +
	             (("AME" + RIGlobal.spaces40).substring(0,3)) + "|" +
//	             (("D" + RIGlobal.spaces40).substring(0,2)) + "|" +
	             "AWERT";
	}
}
