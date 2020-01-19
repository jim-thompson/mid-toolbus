package com.toolbus.riinc;
//RITBFile.java
//Version 2.06
//
//Routines for processing of TOOLBUS files

//imports of JAVA standard classes
import java.io.* ;
import java.text.*;
//-------------------------------------------------------------
// RITBFile: structure and routines for one TOOLBUS file
//----------------------------------------------------------
public class RITBFile
{
 public String fileName;
 public int numRecs;
 public int numAlines;
 public char fileType;
 public BufferedReader tbReader;
 public BufferedWriter tbWriter;
 public RITBRec nextTb;
 public RITBRec vorTb;
 public RITBRec outTb;

//-------------------------------------------------------------
//  open and initialize TB file
//----------------------------------------------------------
 public RITBFile (String name, char art)
   {
   try {
    this.fileName = name;
    this.numRecs  = 0;
    this.numAlines = 0;
    this.nextTb = null;
    this.vorTb = null;
    this.fileType = art;
    if (this.fileType == 'O')
       this.tbWriter = new BufferedWriter( new FileWriter(name));
    else
       this.tbReader = new BufferedReader( new FileReader(name));
    }
      catch ( IOException e) {
          RIGlobal.msgInfos[0] = "Error during open of Toolbus File";
          RIGlobal.msgInfos[1] = e.toString();
          RIGlobal.printMsg(2,'S');
//V1.08
          if (RIGlobal.Batch)
                 RIGlobal.abendPgm();
      }
   }
//-------------------------------------------------------------
//  open and initialize TB file WITH CHARSETNAME
//----------------------------------------------------------
 RITBFile (String name, char art, String charsetname)
 {
   try {
    this.fileName = name;
    this.numRecs  = 0;
    this.numAlines = 0;
    this.nextTb = null;
    this.vorTb = null;
    this.fileType = art;
    if (this.fileType == 'O') {
       FileOutputStream fos = new FileOutputStream(name);
       OutputStreamWriter ow;
       if (charsetname.equals(""))
          ow = new OutputStreamWriter(fos);
       else
          ow = new OutputStreamWriter(fos,charsetname);
       this.tbWriter = new BufferedWriter(ow);
    }
    else  {
       FileInputStream fis = new FileInputStream(name);
       InputStreamReader ir;
       if (charsetname.equals(""))
          ir = new InputStreamReader(fis);
       else
          ir = new InputStreamReader(fis,charsetname);
       this.tbReader = new BufferedReader(ir);
    }
   }
   catch ( UnsupportedEncodingException uee) {
          RIGlobal.msgInfos[0] = "Error during open of Toolbus File";
          RIGlobal.msgInfos[1] = "Unsupported Charsetname = " + charsetname;
          RIGlobal.msgInfos[2] = uee.toString();
          RIGlobal.printMsg(2,'S');
          if (RIGlobal.Batch)
                 RIGlobal.abendPgm();
   }
   catch ( IOException e) {
          RIGlobal.msgInfos[0] = "Error during open of Toolbus File";
          RIGlobal.msgInfos[1] = e.toString();
          RIGlobal.printMsg(2,'S');
          if (RIGlobal.Batch)
                 RIGlobal.abendPgm();
   }
 }
//-------------------------------------------------------------
// readRec: read TB record  from TB file
//          into RITBRec: nextTb
//  for variable length records: there are no continuation lines
//     method works for SCBS and DBCS codes
//  for fixed length records: continuation lines are read
//  !!! method works only for SCBS codes
//----------------------------------------------------------
 public void readRec()
 {
   String line;
   try
   {
      if (this.vorTb != null )
// already read in advance: take this record
      {
         this.nextTb = this.vorTb;
         this.vorTb = null;
      }
      else
      {
// else read next record, ignore empty and comment records
         while (true)
         {
            line = this.tbReader.readLine();
            if (line == null )
            {
               this.nextTb = null;
               return;
            }
            this.numRecs++;
// ignore empty record
            if (!(line.length() < 10))
            {
               this.nextTb = new RITBRec();
               this.nextTb.getFields(line);
// ignore comment record
               if (!this.nextTb.modus.substring(0,1).equals("*")) break;
            }
         }
      }

      if (this.nextTb.modus.equals("AR"))
      {
         this.nextTb.aname = "RELINFO";
         this.numAlines = 0;
      }
      else
      {
         this.numAlines = 1;
      }
// now look ahead
      while (true)
      {
         line = this.tbReader.readLine();
// no more records
         if (line == null ) return;
// got next record
         this.numRecs++;
         this.vorTb = new RITBRec();
         this.vorTb.getFields(line);
// ignore empty records
         if (line.length() < 10)
         {
           this.vorTb = null;
           continue;
         }
// ignore comments
         if (this.vorTb.modus.substring(0,1).equals("*"))
         {
           this.vorTb = null;
           continue;
         }
// ignore records with identical key (awert may be different)
         if (this.vorTb.relnr13.equals(this.nextTb.relnr13)
            && this.vorTb.relnr4.equals(this.nextTb.relnr4)
            && this.vorTb.modus.equals(this.nextTb.modus)
            && this.vorTb.subtypr.equals(this.nextTb.subtypr)
            && this.vorTb.entity1.equals(this.nextTb.entity1)
            && this.vorTb.entity2.equals(this.nextTb.entity2)
            && this.vorTb.aname.equals(this.nextTb.aname)
            && this.vorTb.vaname.equals(this.nextTb.vaname)
            && this.vorTb.acont.equals(this.nextTb.acont))
         {
            this.vorTb = null;
            continue;
         }
// the following record belongs to the actual AR record
// (for variable length TB records there is only one such RELINFO record)
// (awert of AR record is always empty !)
         if (this.nextTb.modus.equals("AR")
            && this.vorTb.relnr13.equals(this.nextTb.relnr13)
            && this.vorTb.relnr4.equals(this.nextTb.relnr4)
            && this.vorTb.subtypr.equals(this.nextTb.subtypr)
            && this.vorTb.entity1.equals(this.nextTb.entity1)
            && this.vorTb.entity2.equals(this.nextTb.entity2)
            && this.vorTb.aname.equals("RELINFO"))
         {
              this.nextTb.awert =
                (this.nextTb.awert + RIGlobal.spaces72).substring(0,72 * this.numAlines) +
                this.vorTb.awert ;
              this.numAlines++;
              this.vorTb = null;
              continue;
         }
// the following record is an acont record to the actual record
// !!! this is not possible for variable length TB records
         if (this.vorTb.relnr13.equals(this.nextTb.relnr13)
            && this.vorTb.relnr4.equals(this.nextTb.relnr4)
            && this.vorTb.subtypr.equals(this.nextTb.subtypr)
            && this.vorTb.entity1.equals(this.nextTb.entity1)
            && this.vorTb.entity2.equals(this.nextTb.entity2)
            && this.vorTb.aname.equals(this.nextTb.aname)
            && this.vorTb.vaname.equals(this.nextTb.vaname))
         {
              this.nextTb.awert =
                (this.nextTb.awert + RIGlobal.spaces72).substring(0,72 * this.numAlines) +
                this.vorTb.awert ;
              this.numAlines++;
              this.vorTb = null;
              continue;
         }
//       System.out.println("Awert=" + this.nextTb.awert + "#");
         break;
      }
   }
   catch ( IOException e)
   {
      System.out.println(e.toString());
      this.nextTb = null;
      this.vorTb = null;
   }
 }
//-------------------------------------------------------------
// readTRec: read record (incl. all Text and continuation lines) from TB file
//          into RITBRec: nextTb
//for variable length records: there are no continuation lines
//    method works for SCBS and DBCS codes
//for fixed length records: continuation lines are read
// !!! method works only for SCBS codes
//---------------------------------------------------------------------
 public void readTRec()
 {
   String line = null;
   String nextfullrec = "";
   int fl = 0;
   
   try
   {
      if (this.vorTb != null )
// already read in advance: take this record
      {
         this.nextTb = this.vorTb;
         this.vorTb = null;
      }
      else
      {
// else read next record, ignore empty and comment records
         while (true)
         {
            line = this.tbReader.readLine();
            if (line == null )
            {
               this.nextTb = null;
               return;
            }
            this.numRecs++;
// ignore empty record
            if (!(line.length() < 10))
            {
               this.nextTb = new RITBRec();
               this.nextTb.getFields(line);
// ignore comment record
               if (!this.nextTb.modus.substring(0,1).equals("*")) break;
            }
         }
      }
  //   System.out.println("next=" + line);
     if (this.nextTb.modus.equals("AE"))
     {
       this.nextTb.awert = "";
     }
     if (this.nextTb.modus.equals("AR"))
     {
         this.nextTb.aname = "RELINFO";
         this.nextTb.awert = "";         
     }
     if (this.nextTb.modus.equals("CA"))
     {    
//    	 first part always 72 char 	 
    	 if (this.nextTb.awert.length() < 72) this.nextTb.awert 
    	   = (this.nextTb.awert + RIGlobal.spaces72).substring(0,72);
   // remember line 000                                           
    	 if (this.nextTb.vaname.equals("000")) fl = 1;
         if (this.nextTb.aname.equals("RELINFO"))
                this.nextTb.modus = "AR";
     }
// now look ahead
      while (true)
      {
         line = this.tbReader.readLine();
// no more records
//>>>>>>>>>>>>>>>>>>>>>> 13.09.2011 Zwischenl�sung
// RITBFile von allen Innovator-Programmen austauschen !!         
         if (line == null ) {
        	 if (this.nextTb != null)
        		 this.nextTb.awert = RIGlobal.rtrim(this.nextTb.awert);
        	 return;
         }
// got next record
         this.numRecs++;
         this.vorTb = new RITBRec();
         this.vorTb.getFields(line);
    //   System.out.println("vor=" +line);
// ignore empty records
         if (line.length() < 10)
         {
           this.vorTb = null;
           continue;
         }
// ignore comments
         if (this.vorTb.modus.substring(0,1).equals("*"))
         {
           this.vorTb = null;
           continue;
         }
// ignore records with identical key (awert may be different)
         if (this.vorTb.relnr13.equals(this.nextTb.relnr13)
            && this.vorTb.relnr4.equals(this.nextTb.relnr4)
            && this.vorTb.modus.equals(this.nextTb.modus)
            && this.vorTb.subtypr.equals(this.nextTb.subtypr)
            && this.vorTb.entity1.equals(this.nextTb.entity1)
            && this.vorTb.entity2.equals(this.nextTb.entity2)
            && this.vorTb.aname.equals(this.nextTb.aname)
            && this.vorTb.vaname.equals(this.nextTb.vaname)
            && this.vorTb.acont.equals(this.nextTb.acont))
         {
            this.vorTb = null;
            continue;
         }
         nextfullrec = this.vorTb.awert; 
//    	 next part always 72 char 
         if (nextfullrec.length() < 72) nextfullrec = (nextfullrec + RIGlobal.spaces72).substring(0,72);
// the following record belongs to the actual AR record
// (with ACONT and same line number)
// !!! this is not possible for variable length TB records
         if (this.nextTb.modus.equals("AR")
                        && this.vorTb.relnr13.equals(this.nextTb.relnr13)
            && this.vorTb.relnr4.equals(this.nextTb.relnr4)
            && this.vorTb.subtypr.equals(this.nextTb.subtypr)
            && this.vorTb.entity1.equals(this.nextTb.entity1)
            && this.vorTb.entity2.equals(this.nextTb.entity2)
            && this.vorTb.vaname.equals(this.nextTb.vaname)
            && this.vorTb.aname.equals("RELINFO"))
         {     
        	  fl = 0 ;         	
              this.nextTb.awert = this.nextTb.awert + nextfullrec;             
              this.vorTb = null;
              continue;
         }
// the following record belongs to the actual AR record
// (with different line number)  
     if (this.nextTb.modus.equals("AR")
     && this.vorTb.relnr13.equals(this.nextTb.relnr13)
     && this.vorTb.relnr4.equals(this.nextTb.relnr4)
     && this.vorTb.subtypr.equals(this.nextTb.subtypr)
     && this.vorTb.entity1.equals(this.nextTb.entity1)
     && this.vorTb.entity2.equals(this.nextTb.entity2)
     && this.vorTb.aname.equals("RELINFO"))     
  {     
       if (this.nextTb.awert.equals(""))	 
         this.nextTb.awert = nextfullrec;
       else {
    	 if (fl ==1) {
     	    fl = 0;
     	    this.nextTb.awert = this.nextTb.awert + "\n" + nextfullrec;
     	 }
    	 else
    	    this.nextTb.awert = RIGlobal.rtrim(this.nextTb.awert) + "\n" + nextfullrec;
       } 
       this.nextTb.vaname = this.vorTb.vaname;
       this.vorTb = null;
       continue;
  }
// the following record is an acont record to the actual record
// !!! this is not possible for variable length TB records
         if (this.nextTb.modus.equals("CA")
            && this.vorTb.relnr13.equals(this.nextTb.relnr13)
            && this.vorTb.relnr4.equals(this.nextTb.relnr4)
            && this.vorTb.subtypr.equals(this.nextTb.subtypr)
            && this.vorTb.entity1.equals(this.nextTb.entity1)
            && this.vorTb.entity2.equals(this.nextTb.entity2)
            && this.vorTb.aname.equals(this.nextTb.aname)
            && this.vorTb.vaname.equals(this.nextTb.vaname))
         {   	  
        	  fl = 0;
              this.nextTb.awert = this.nextTb.awert + nextfullrec;
              this.vorTb = null;
              continue;
         }
// the following record is an additional text line for the actual record
         if (this.nextTb.modus.equals("CA")
            && this.vorTb.relnr13.equals(this.nextTb.relnr13)
            && this.vorTb.relnr4.equals(this.nextTb.relnr4)
            && this.vorTb.subtypr.equals(this.nextTb.subtypr)
            && this.vorTb.entity1.equals(this.nextTb.entity1)
            && this.vorTb.entity2.equals(this.nextTb.entity2)
            && this.vorTb.aname.equals(this.nextTb.aname))
         {     
        	 if (fl ==1) {
          	    fl = 0;
          	    this.nextTb.awert = this.nextTb.awert + "\n" + nextfullrec;
          	  }
         	  else
         	    this.nextTb.awert = RIGlobal.rtrim(this.nextTb.awert) + "\n" + nextfullrec;              
              this.nextTb.vaname = this.vorTb.vaname;
              this.vorTb = null;
              continue;
         }
         this.nextTb.awert = RIGlobal.rtrim(this.nextTb.awert);
         break;
      }
   }
   catch ( IOException e)
   {
      System.out.println(e.toString());
      this.nextTb = null;
      this.vorTb = null;
   }
 }
//-------------------------------------------------------------
// writeRec: write fixed length record and continuation lines to TB file
//           from RITBRec: outTb
//   !!!  can be used only for SBCS codes
//---------------------------------------------------------
 public void writeRec() {
   String line;
   String restVal;
   String zVal;
   DecimalFormat z2 = new DecimalFormat("00");
   try {
       if (this.outTb == null) return;
       if (this.outTb.modus.equals("AR"))
       {
// write AR record without AWERT
          restVal = this.outTb.awert;
          zVal = this.outTb.aname;
          this.outTb.awert = "";
          this.outTb.aname = "";
          this.outTb.acont = "";
          line = this.outTb.putFields();
          this.tbWriter.write(line);
          this.tbWriter.newLine();
          this.numRecs++;
          if (restVal.equals("")) {
//        	V2.04
              this.tbWriter.flush();
              return;
          }  
// if there is AWERT: write CA RELINFO records
          this.outTb.modus = "CA";
          this.outTb.aname = "RELINFO";
          this.outTb.awert = restVal;
       }
       if (this.outTb.awert.length() <= 72)
       {
          this.outTb.acont = "";
          line = this.outTb.putFields();
//        System.out.println("line=" + line + "#");
          this.tbWriter.write(line);
          this.tbWriter.newLine();
          this.numRecs++;
//        V2.04
          this.tbWriter.flush();
          return;
       }

       zVal = this.outTb.awert.substring(0,72);
       restVal = this.outTb.awert.substring(72);
       this.outTb.awert = zVal;
       this.outTb.acont = "";
       line = this.outTb.putFields();
       this.tbWriter.write(line);
       this.tbWriter.newLine();
       this.numRecs++;
       int ia = 0;
       while (restVal.length() > 0)
       {
          if (restVal.length() <= 72)
          {
             zVal = restVal;
             restVal = "";
          }
          else
          {
             zVal = restVal.substring(0,72);
             restVal = restVal.substring(72);
          }
          this.outTb.awert = zVal;
          ia++;
          this.outTb.acont = z2.format(ia);
          line = this.outTb.putFields();
          this.tbWriter.write(line);
          this.tbWriter.newLine();
          this.numRecs++;
       }
//     V2.04
       this.tbWriter.flush();
       return;
    }
   catch ( IOException e) {
        System.out.println(e.toString());
    }
  }
//-------------------------------------------------------------
// writeVRec: write varaible length record to TB file
//           (awert IN ONE RECORD, no continuation lines)
//           from RITBRec: outTb
//   !!!  can be used for SBCS and DBCS codes
//---------------------------------------------------------
 public void writeVRec() {
   String line;
   String aVal;
   try {
       if (this.outTb == null) return;
       if (this.outTb.modus.equals("AR"))
       {
// write AR record without AWERT
          aVal = this.outTb.awert;
          this.outTb.awert = "";
          this.outTb.aname = "";
          this.outTb.acont = "";
          line = this.outTb.putFields();
          this.tbWriter.write(line);
          this.tbWriter.newLine();
          this.numRecs++;
          if (aVal.equals("")) {        	  
//        	V2.05
            this.tbWriter.flush();
            return;
          }  
// if there is AWERT: write CA RELINFO record
          this.outTb.modus = "CA";
          this.outTb.aname = "RELINFO";
          this.outTb.awert = aVal;
       }
       this.outTb.acont = "";
       line = this.outTb.putFields();
//     System.out.println("line=" + line + "#");
       this.tbWriter.write(line);
       this.tbWriter.newLine();
       this.numRecs++;
//     V2.05
       this.tbWriter.flush();
       return;
    }
   catch ( IOException e) {
        System.out.println(e.toString());
    }
  }
//-------------------------------------------------------------
// close: close TB file
//----------------------------------------------------------
 public void close() {
   try {
      if (this.fileType == 'O')
         this.tbWriter.close();
      else
         this.tbReader.close();
       }
   catch ( IOException e) {
        System.out.println(e.toString());
    }
  }
}
