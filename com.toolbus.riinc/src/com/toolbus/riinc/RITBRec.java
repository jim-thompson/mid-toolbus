package com.toolbus.riinc;
//RITBRec.java
//Version 1.04
//
//Structure and routines for TOOLBUS records
//    (including continuation lines for attribute line
//     and including RELINFO lines for AR record)

public class RITBRec
{
 public String aname_s;
 public String modus;
 public String modpri;
 public String relnr13;
 public String relnr4;
 public String subtyp1;
 public String subtyp2;
 public String subtypr;
 public String entity1;
 public String entity2;
 public String aname;
 public String vaname;
 public String acont;
 public String awert;
 public String _line = "";

//-------------------------------------------------------------
// clear: set TBrecord empty
//----------------------------------------------------------
 public void clear()
  {
   aname_s =  "";
   modus =    "";
   modpri =   "";
   relnr13 =  "";
   relnr4 =   "";
   subtyp1 =  "";
   subtyp2 =  "";
   subtypr =  "";
   entity1 =  "";
   entity2 =  "";
   aname   =  "";
   vaname   = "";
   acont    = "";
   awert    = "";
  }
//-------------------------------------------------------------
// getFields: fill TBrecord fields from String
//----------------------------------------------------------
 public void getFields(String line)
  {
   // Make a copy of the input line we're building the record from
   _line = new String(line);
	 
   this.clear();

   if (_line.length() == 0) return;

  // alle Felder werden rechts und links getrimmt

   if (_line.length() < 3) {aname_s = _line.trim(); return;}
   aname_s = _line.substring(0,2).trim();

   if (_line.length() < 6) {modus = _line.substring(2).trim(); return;}
   modus = _line.substring(2,5).trim();

   if (_line.length() < 8) {modpri = _line.substring(5).trim(); return;}
   modpri = _line.substring(5,7).trim();

   if (_line.length() < 11) {relnr13 = _line.substring(7).trim(); return;}
   relnr13 = _line.substring(7,10).trim();

   if (_line.length() < 12) {relnr4 = _line.substring(10).trim(); return;}
   relnr4 = _line.substring(10,11).trim();

   if (_line.length() < 20) {subtyp1 = _line.substring(11).trim(); return;}
   subtyp1 = _line.substring(11,19).trim();

   if (_line.length() < 28) {subtyp2 = _line.substring(19).trim(); return;}
   subtyp2 = _line.substring(19,27).trim();

   if (_line.length() < 36) {subtypr = _line.substring(27).trim(); return;}
   subtypr = _line.substring(27,35).trim();

   if (_line.length() < 71) {entity1 = _line.substring(35).trim(); return;}
   entity1 = _line.substring(35,70).trim();

   if (_line.length() < 106) {entity2 = _line.substring(70).trim(); return;}
   entity2 = _line.substring(70,105).trim();

   if (_line.length() < 136) return;

   if (_line.length() < 144) {aname = _line.substring(135).trim(); return;}
   aname = _line.substring(135,143).trim();

   if (_line.length() < 147) {vaname = _line.substring(143).trim(); return;}
   vaname = _line.substring(143,146).trim();

   if (_line.length() < 149) {acont = _line.substring(146).trim(); return;}
   acont = _line.substring(146,148).trim();
  // awert ist rechts sowieso getrimmt und darf links nicht getrimmt werden
   awert = _line.substring(148);
  }
//-------------------------------------------------------------
// putFields: build String from TBrecord fields
//----------------------------------------------------------
 public String putFields()
  {
   String result;
   String nresult;
   String alt_1 = this.aname_s;
   this.aname_s = "X ";
   nresult =  (
             ((this.aname_s + RIGlobal.spaces40).substring(0,2)) +
             ((this.modus + RIGlobal.spaces40).substring(0,3)) +
             ((this.modpri + RIGlobal.spaces40).substring(0,2)) +
             ((this.relnr13 + RIGlobal.spaces40).substring(0,3)) +
             ((this.relnr4 + RIGlobal.spaces40).substring(0,1)) +
             ((this.subtyp1 + RIGlobal.spaces40).substring(0,8)) +
             ((this.subtyp2 + RIGlobal.spaces40).substring(0,8)) +
             ((this.subtypr + RIGlobal.spaces40).substring(0,8)) +
             ((this.entity1 + RIGlobal.spaces40).substring(0,35)) +
             ((this.entity2 + RIGlobal.spaces40).substring(0,35)) +
             RIGlobal.spaces40.substring(0,30) +
             ((this.aname + RIGlobal.spaces40).substring(0,8)) +
             ((this.vaname + RIGlobal.spaces40).substring(0,3)) +
             ((this.acont + RIGlobal.spaces40).substring(0,2)) +
             this.awert
             ).trim();
   result = ((alt_1 +  "  ").substring(0,2)) + nresult.substring(2);
   this.aname_s = alt_1;
   return result;
  }
//-------------------------------------------------------------
// edit: build print String from TBrecord fields
//----------------------------------------------------------
 public String edit()
  {
   String result = "";
   if (this.entity2.equals(""))
     if (this.aname.equals(""))
       result = " "
             + ((this.modus + RIGlobal.spaces40).substring(0,4))
             + ((this.relnr13 + RIGlobal.spaces40).substring(0,3))
             + ((this.relnr4 + RIGlobal.spaces40).substring(0,2))
             + this.entity1;
     else
       result = " "
             + ((this.modus + RIGlobal.spaces40).substring(0,4))
             + ((this.relnr13 + RIGlobal.spaces40).substring(0,3))
             + ((this.relnr4 + RIGlobal.spaces40).substring(0,2))
             + this.entity1 + " "
             + ((this.aname + RIGlobal.spaces40).substring(0,9))
             + ((this.vaname + RIGlobal.spaces40).substring(0,3))
             + ((this.acont + RIGlobal.spaces40).substring(0,3))
             + this.awert;
   else
     if (this.aname.equals(""))
       result = " "
             + ((this.modus + RIGlobal.spaces40).substring(0,4))
             + ((this.relnr13 + RIGlobal.spaces40).substring(0,3))
             + ((this.relnr4 + RIGlobal.spaces40).substring(0,2))
             + this.entity1 + " "
             + ((this.subtypr + RIGlobal.spaces40).substring(0,15))
             + this.entity2;
     else
       result = " "
             + ((this.modus + RIGlobal.spaces40).substring(0,4))
             + ((this.relnr13 + RIGlobal.spaces40).substring(0,3))
             + ((this.relnr4 + RIGlobal.spaces40).substring(0,2))
             + this.entity1 + " "
             + ((this.subtypr + RIGlobal.spaces40).substring(0,15))
             + this.entity2 + " "
             + ((this.aname + RIGlobal.spaces40).substring(0,9))
             + ((this.vaname + RIGlobal.spaces40).substring(0,3))
             + ((this.acont + RIGlobal.spaces40).substring(0,3))
             + this.awert;
   return result;
  }
//-------------------------------------------------------------
// editLong: build print String (long format) from TBrecord fields
//----------------------------------------------------------
 public String editLong()
  {
   String result;
   result = " " +
            "ANAME-S=" + this.aname_s +
            " MODUS=" + this.modus +
            " MODPRI=" + this.modpri +
            " RELNR13=" + this.relnr13 +
            " RELNR4=" + this.relnr4 +
            " SUBTYP1="+ this.subtyp1 +
            " SUBTYP2="+ this.subtyp2 +
            " SUBTYPR="+ this.subtypr +
            " ENTITY1="+ this.entity1 +
            " ENTITY2="+ this.entity2 +
            " ANAME="+ this.aname +
            " VANAME="+ this.vaname +
            " ACONT="+ this.acont +
            " AWERT="+ this.awert;
   return result;
  }
}
