package com.toolbus.riinc;
//RIGlobal.java
//Version 2.17
//
//Common used global variables and methods for TOOLBUS programs

// imports of JAVA standard classes
import java.io.* ;
import java.util.*;
import java.text.*;
//
public class RIGlobal
{
public static String pgmname;
public static String pgmname8;
public static String pgmvers;
public static String pgmtitle;
public static String copyright;
public static String listDate;
public static String DBType;

public static File inif;
public static BufferedReader inifile;
public static BufferedWriter inifileOut;
public static BufferedWriter protok;
public static BufferedReader vorlfile;
public static Calendar cal;
public static SimpleDateFormat engdat;
public static String msgTexts[];
public static String msgInfos[];
public static String iniLines[];
public static int numIniLines;
public static int msgAppend;
public static int maxCode ;
public static String iniName = ""; //$NON-NLS-1$
public static String iniPath;
public static String pgmDir;
//V2.13
public static String orgpgmDir;
//
public static char parCheck;
public static DecimalFormat z9;
public static DecimalFormat nnn;
public static String spaces40;
public static String spaces72;
//V2.02
public static String ErrMsg1 = ""; //$NON-NLS-1$
public static String ErrMsg2 = ""; //$NON-NLS-1$
//V2.03
public static String statName[];
public static int statValue[];
public static int statCount;
//V2.12
public static String resultName[];
public static String resultValue[];
public static int resultCount;
//V2.06
public static boolean Batch = true;
//V2.07
public static String charsetname = ""; //$NON-NLS-1$

//---------------------------------------------------------------
//printStartMsg: open program listing and print start message
//V2.09: int with return code
//----------------------------------------------------------------
public static int printStartMsg(String protokDSN) {
try {
//    OutputStream fos = new FileOutputStream(protokDSN);
	OutputStream fos = System.out;
    OutputStreamWriter ow;
    if (charsetname.equals("")) //$NON-NLS-1$
        ow = new OutputStreamWriter(fos);
    else
        ow = new OutputStreamWriter(fos,charsetname);
   protok = new BufferedWriter(ow);
   protok.write( " " + pgmname8 //$NON-NLS-1$
      + " I 001  " + RIGlobal_Messages.getString("RIGlobal.Version") //$NON-NLS-1$ //$NON-NLS-2$
      + " " + pgmvers //$NON-NLS-1$
        + " " + RIGlobal_Messages.getString("RIGlobal.started") +  listDate) ; //$NON-NLS-1$ //$NON-NLS-2$
   protok.newLine();
   protok.write( " " + pgmname8 + " I 001A " + pgmtitle); //$NON-NLS-1$ //$NON-NLS-2$
   protok.newLine();
   protok.write( " " + pgmname8 + " I 001B " + copyright); //$NON-NLS-1$ //$NON-NLS-2$
   protok.newLine();
   protok.newLine();
   protok.flush();
   return 0;
  }
  catch ( IOException e) {
// V2.02
   ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_program_start_message"); //$NON-NLS-1$
   ErrMsg2 = e.toString();
    System.out.println(pgmname8
        + " " + RIGlobal_Messages.getString("RIGlobal.Version") //$NON-NLS-1$ //$NON-NLS-2$
      + " " + pgmvers ); //$NON-NLS-1$
    System.out.println(RIGlobal_Messages.getString("RIGlobal.error_program_start_message")); //$NON-NLS-1$
    System.out.println(e.toString());
    protok = null;
    return 12;
  }
}
//---------------------------------------------------------------
//testprotok: test if file can be used as protok (write possible)
//----------------------------------------------------------------
public static boolean testprotok (String protokDSN) {
try {
 FileWriter tprot  =  new FileWriter(protokDSN) ;
 tprot.close();
 return true;
}
catch ( IOException e) {
    return false;
}
}
//---------------------------------------------------------------
//testtrace: test if file can be read
//----------------------------------------------------------------
public static boolean testtrace (String traceDSN) {
try {
 FileReader ttrace  =  new FileReader(traceDSN) ;
 ttrace.close();
 return true;
}
catch ( IOException e) {
  return false;
}
}
//-------------------------------------------------------------
//printEndMsg: print end message and close program listing
//-------------------------------------------------------------
public static void printEndMsg() {
try {
   cal = new GregorianCalendar();
   listDate = engdat.format( cal.getTime()) ;
   protok.newLine();
   if ( maxCode == 0)
     protok.write( " " + pgmname8 //$NON-NLS-1$
        + " I 999  " + RIGlobal_Messages.getString("RIGlobal.program_normally_ended") //$NON-NLS-1$ //$NON-NLS-2$
      + listDate) ;
   else
     if ( maxCode <= 4)
       protok.write( " " + pgmname8 //$NON-NLS-1$
          + " W 999  " + RIGlobal_Messages.getString("RIGlobal.program_ended_with_warnings") + //$NON-NLS-1$ //$NON-NLS-2$
      listDate) ;
     else
       if ( maxCode <= 8)
         protok.write( " " + pgmname8 //$NON-NLS-1$
            + " E 999  " + RIGlobal_Messages.getString("RIGlobal.program_ended_with_errors") + //$NON-NLS-1$ //$NON-NLS-2$
        listDate) ;
       else
         protok.write( " " + pgmname8 //$NON-NLS-1$
            + " S 999  " + RIGlobal_Messages.getString("RIGlobal.program_aborted_with_severe_error") + //$NON-NLS-1$ //$NON-NLS-2$
        listDate) ;
   protok.newLine();
   protok.close();
  }
  catch ( IOException e) {
// V2.02
   ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_program_end_message"); //$NON-NLS-1$
   ErrMsg2 = e.toString();
    System.out.println(e.toString());
  }
}
//-------------------------------------------------------------
//abendPgm: abnormal ending of program
//-------------------------------------------------------------
public static void abendPgm() {
  // abend of Java engine
  // even if Batch flag is set to false
  if (!iniName.equals(""))  writeIni(maxCode); //$NON-NLS-1$
  cal = new GregorianCalendar();
  listDate = engdat.format( cal.getTime()) ;
  printEndMsg();
  System.out.println(RIGlobal_Messages.getString("RIGlobal.program_abended")); //$NON-NLS-1$
  System.exit(12);
}
//-------------------------------------------------------------
//printMsg: print program message and set MaxCode
//-------------------------------------------------------------
public static void printMsg(int msgNo, char msgType) {
 String app;
try {
    if (msgType == 'W' && maxCode < 4) maxCode = 4;
    if (msgType == 'E' && maxCode < 8) maxCode = 8;
    if (msgType == 'S' && maxCode < 12) maxCode = 12;
    DecimalFormat z3 = new DecimalFormat("000"); //$NON-NLS-1$
    if (!msgTexts[msgNo].equals(" ")) //$NON-NLS-1$
    {
      protok.write( " " + pgmname8 + " " + msgType + " " + z3.format(msgNo) + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
             "  " +  msgTexts[msgNo]); //$NON-NLS-1$
      protok.newLine();
    }

    for ( int i = 0; i < msgInfos.length; i++)
    {
      if (msgInfos[i] == null) break;
      if (msgAppend == 0 || i > 25)
        protok.write( " " + pgmname8 + " " + msgType + " " + z3.format(msgNo) + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
              "  " +  msgInfos[i]); //$NON-NLS-1$
      else
      {
        app = String.valueOf((char)(i+65));
        protok.write( " " + pgmname8 + " " + msgType + " " + z3.format(msgNo) + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        app  + " " +  msgInfos[i]); //$NON-NLS-1$
//V2.11 begin
        if (msgTexts[msgNo].equals(" ")) //$NON-NLS-1$
        {
         if (i < 1) app = " "; //$NON-NLS-1$
         else app = String.valueOf((char)(i+64));
        }
//V2.11 end
      }
      protok.newLine();
      msgInfos[i] = null;
    }
    protok.newLine();
    protok.flush();
    msgAppend = 1;
  }
  catch ( IOException e) {
// V2.02
   ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_program_message"); //$NON-NLS-1$
   ErrMsg2 = e.toString();
    System.out.println(e.toString());
  }
}
//-------------------------------------------------------------
//readIni: open and read INI file into string Array
//-------------------------------------------------------------
public static void readIni() {
 String line;
 String upper;
 int block = 0;
 char first;
try {

  inif = new File(iniName );
  iniPath = inif.getParent();
  if (iniPath == null)
     iniPath = ""; //$NON-NLS-1$
  else
    iniPath = iniPath + File.separatorChar;
  inifile = new BufferedReader( new FileReader(inif ));
  while (  (line = inifile.readLine()) != null )
    {
       first = line.charAt(0);
       if (first == '[')
       {
           block = 0;
           upper = line.toUpperCase();
           if (upper.equals("[RETURNCODE]")) block = 1; //$NON-NLS-1$
       }
       if (block == 0)
       {
          numIniLines = numIniLines  + 1;
          iniLines[numIniLines - 1] = line;
       }
    }
  inifile.close();
  }
  catch ( IOException e) {
//abend program
// V2.02
   ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_read_program_ini_file"); //$NON-NLS-1$
   ErrMsg2 = e.toString();
    System.out.println(pgmname8
        + " " + RIGlobal_Messages.getString("RIGlobal.Version") //$NON-NLS-1$ //$NON-NLS-2$
      + " " + pgmvers ); //$NON-NLS-1$
    System.out.println(RIGlobal_Messages.getString("RIGlobal.error_read_program_ini_file")); //$NON-NLS-1$
    System.out.println(e.toString());
//  V2.08'
    if (Batch) System.exit(12);
  }
}
//-------------------------------------------------------------
//copyLst: open and copy LST file to joblog
//-------------------------------------------------------------
public static boolean copyLst(String lstfilename, BufferedWriter joblog) {
String line;
try {
BufferedReader lst = new BufferedReader( new FileReader(lstfilename ));
joblog.newLine();
while (  (line = lst.readLine()) != null ) {
//V2.10
  if (line.length() > 0) {
    if (line.charAt(0) != (char)12) {
      joblog.write(line);
      joblog.newLine();
    }
  }
  else
    joblog.newLine();
}
lst.close();
joblog.newLine();
joblog.write("---------------------------------------------------------------------------------"); //$NON-NLS-1$
joblog.newLine();
joblog.newLine();
// V2.08
joblog.flush();
}
catch ( IOException e) {
//abend program
//  V2.02
  ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_copy_list_file"); //$NON-NLS-1$
  ErrMsg2 = e.toString();
  System.out.println(e.toString());
//V2.08'
  if (Batch) System.exit(12);
  return false;
}
return true;
}
//-------------------------------------------------------------
//writeIni: open and write INI file from string Array, with or without RC
//-------------------------------------------------------------
public static void writeIni(int RC) {
try {
  DecimalFormat z2 = new DecimalFormat("00"); //$NON-NLS-1$
  inifileOut = new BufferedWriter( new FileWriter(iniName ));
  for ( int i = 0; i < numIniLines; i++)
    {
       inifileOut.write(iniLines[i]);
       inifileOut.newLine();
    }
  if (RC != -1)
  {
    inifileOut.write("[RETURNCODE]"); //$NON-NLS-1$
    inifileOut.newLine();
    inifileOut.write("RC=" + z2.format(RC)); //$NON-NLS-1$
    inifileOut.newLine();
  }
  inifileOut.close();
  }
  catch ( IOException e) {
    inifileOut = null;
    if (protok == null)
    {
//    V2.02
    ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_rewrite_program_in_file"); //$NON-NLS-1$
    ErrMsg2 = e.toString();
      System.out.println(pgmname8
          + " " + RIGlobal_Messages.getString("RIGlobal.Version") //$NON-NLS-1$ //$NON-NLS-2$
      + " " + pgmvers ); //$NON-NLS-1$
      System.out.println(RIGlobal_Messages.getString("RIGlobal.error_rewrite_program_in_file")); //$NON-NLS-1$
      System.out.println(e.toString());
    }
    else
    {
//    V2.02
    ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_rewrite_program_in_file"); //$NON-NLS-1$
    ErrMsg2 = e.toString();
      msgInfos[0] = RIGlobal_Messages.getString("RIGlobal.error_rewrite_program_in_file"); //$NON-NLS-1$
      msgInfos[1] = e.toString();
      printMsg(2,'S');
      printEndMsg();
    }
//abend program
//  V2.08'
    if (Batch) System.exit(12);
  }
}
//------------------------------------------------------------------
//convJN: convert String to char J or N
//------------------------------------------------------------------
public static char convJN(String yesNo) {
 String upper;
 upper = yesNo.toUpperCase();
 if (upper.equals("")) return 'J'; //$NON-NLS-1$
 if (upper.equals("J")) return 'J'; //$NON-NLS-1$
 if (upper.equals("Y")) return 'J'; //$NON-NLS-1$
 if (upper.equals("JA")) return 'J'; //$NON-NLS-1$
 if (upper.equals("YES")) return 'J'; //$NON-NLS-1$
 if (upper.equals("N")) return 'N'; //$NON-NLS-1$
 if (upper.equals("NO")) return 'N'; //$NON-NLS-1$
 if (upper.equals("NEIN")) return 'N'; //$NON-NLS-1$
 return ' ';
}
//------------------------------------------------------------------
//callSort:  call external (COBOL) sort program
//------------------------------------------------------------------
public static void callSort(String sortPgmName, String sortNr,
                           String sortInDD,
                           String sortInDSN,
                           String sortOutDD,
                           String sortOutDSN,
                           String sortFileDD,
                           String sortFileDSN) {
 String siniName;
//V2.17 
// String cmd = ""; //$NON-NLS-1$
 String[] cmdarray = new String[2];
try {
 siniName = iniPath + sortPgmName + ".INI"; //$NON-NLS-1$
// cmd = pgmDir + sortPgmName + " " + siniName;
//V2.17 
// cmd = "\"" + pgmDir + sortPgmName + ".EXE\" " +
       //"\"" + siniName + "\"";
 cmdarray[0] =  pgmDir + sortPgmName ;
 cmdarray[1] =  siniName;
 inifileOut = new BufferedWriter( new FileWriter(siniName ));
 inifileOut.write("[FILES]"); //$NON-NLS-1$
 inifileOut.newLine();
 inifileOut.write("SORT-NR=" + sortNr); //$NON-NLS-1$
 inifileOut.newLine();
 inifileOut.write(sortInDD + "=" + sortInDSN); //$NON-NLS-1$
 inifileOut.newLine();
 inifileOut.write(sortOutDD + "=" + sortOutDSN); //$NON-NLS-1$
 inifileOut.newLine();
 inifileOut.write(sortFileDD + "=" + sortFileDSN); //$NON-NLS-1$
 inifileOut.newLine();
 inifileOut.close();
//V2.17
// Process p = Runtime.getRuntime().exec(cmd);
 Process p = Runtime.getRuntime().exec(cmdarray);
 p.waitFor();
 if (p.exitValue() != 0)
   {
//V2.17	 
//    msgInfos[0] = cmd;
    msgInfos[0] = cmdarray[0];
    msgInfos[1] = cmdarray[1];
  printMsg(10,'S');
//  V2.08'
    if (Batch) abendPgm();
   }
}
catch ( IOException e) {
//V2.17	
//    msgInfos[0] = cmd;
    msgInfos[0] = cmdarray[0];
    msgInfos[1] = cmdarray[1];
    msgInfos[2] = e.toString();
    printMsg(10,'S');
//  V2.08'
    if (Batch) abendPgm();
}
catch ( InterruptedException ie) {
//V2.17	
//    msgInfos[0] = cmd;
    msgInfos[0] = cmdarray[0];
    msgInfos[1] = cmdarray[1];
    msgInfos[2] = ie.toString();
    printMsg(11,'S');
//  V2.08'
    if (Batch) abendPgm();
}
}

//------------------------------------------------------------------
//V2.16
//callSortW:  call external (COBOL) sort program with work file
//------------------------------------------------------------------
public static void callSortW(String sortPgmName, String sortNr,
                           String sortInDD,
                           String sortInDSN,
                           String sortOutDD,
                           String sortOutDSN,
                           String sortWrkDD,
                           String sortWrkDSN,
                           String sortFileDD,
                           String sortFileDSN) {
 String siniName;
//V2.17
 // String cmd = ""; //$NON-NLS-1$
 String[] cmdarray = new String[2];
try {
 siniName = iniPath + sortPgmName + ".INI"; //$NON-NLS-1$
//V2.17
// cmd = pgmDir + sortPgmName + " " + siniName; //$NON-NLS-1$
 cmdarray[0] =  pgmDir + sortPgmName ;
 cmdarray[1] =  siniName;
 inifileOut = new BufferedWriter( new FileWriter(siniName ));
 inifileOut.write("[FILES]"); //$NON-NLS-1$
 inifileOut.newLine();
 inifileOut.write("SORT-NR=" + sortNr); //$NON-NLS-1$
 inifileOut.newLine();
 inifileOut.write(sortInDD + "=" + sortInDSN); //$NON-NLS-1$
 inifileOut.newLine();
 inifileOut.write(sortOutDD + "=" + sortOutDSN); //$NON-NLS-1$
 inifileOut.newLine();
//V2.11 
 inifileOut.write(sortWrkDD + "=" + sortWrkDSN); //$NON-NLS-1$
 inifileOut.newLine();
 inifileOut.write(sortFileDD + "=" + sortFileDSN); //$NON-NLS-1$
 inifileOut.newLine();
 inifileOut.close();
//V2.17
// Process p = Runtime.getRuntime().exec(cmd);
 Process p = Runtime.getRuntime().exec(cmdarray);
 p.waitFor();
 if (p.exitValue() != 0)
   {
//V2.17	 
//    msgInfos[0] = cmd;
    msgInfos[0] = cmdarray[0];
    msgInfos[1] = cmdarray[1];
    printMsg(10,'S');
//  V2.08'
    if (Batch) abendPgm();
   }
}
catch ( IOException e) {
//V2.17	
//    msgInfos[0] = cmd;
    msgInfos[0] = cmdarray[0];
    msgInfos[1] = cmdarray[1];
    msgInfos[2] = e.toString();
    printMsg(10,'S');
//  V2.08'
    if (Batch) abendPgm();
}
catch ( InterruptedException ie) {
//V2.17	
//    msgInfos[0] = cmd;
    msgInfos[0] = cmdarray[0];
    msgInfos[1] = cmdarray[1];
    msgInfos[2] = ie.toString();
    printMsg(11,'S');
//  V2.08'
    if (Batch) abendPgm();
}
}

  //------------------------------------------------------------------
  //callExe: call external program
  //------------------------------------------------------------------
  public static int callExe(String PgmName, String[] inDD, String[] inDSN,
      int numDS) {

    String siniName;
    String line;
//V2.17
//    String cmd = ""; //$NON-NLS-1$
    String[] cmdarray = new String[2];
    String upper;
//V2.02
    ErrMsg1 = ""; //$NON-NLS-1$
    ErrMsg2 = ""; //$NON-NLS-1$
    String rcs = "16"; //$NON-NLS-1$
    int block = 0;
    char first;
//V2.03 begin
    statName    = new String[10];
    statValue   = new int[10];
//V2.03 end

    statCount = 0;

    resultName    = new String[10];
    resultValue   = new String[10];
//V2.03 end

    resultCount = 0;

    try {
      siniName = iniPath + PgmName + ".INI"; //$NON-NLS-1$
//    cmd = pgmDir + PgmName + " " + siniName; //$NON-NLS-1$
//V2.17      
//      cmd = "\"" + pgmDir + PgmName + ".EXE\" " +
//       "\"" + siniName + "\"";
      cmdarray[0] =  pgmDir + PgmName ;
      cmdarray[1] =  siniName;
      inifileOut = new BufferedWriter(new FileWriter(siniName));
      inifileOut.write("[FILES]"); //$NON-NLS-1$
      inifileOut.newLine();
      for (int i = 0; i < numDS; i++) {
        inifileOut.write(inDD[i] + "=" + inDSN[i]); //$NON-NLS-1$
        inifileOut.newLine();
      }
      inifileOut.close();
//V2.17
//      Process p = Runtime.getRuntime().exec(cmd);
      Process p = Runtime.getRuntime().exec(cmdarray);
      p.waitFor();
      // read inifile because of RC / Statistics
      BufferedReader inifileIn = new BufferedReader(new FileReader(
          siniName));
      while ((line = inifileIn.readLine()) != null) {
   //   System.out.println("Line=" + line);
        first = line.charAt(0);
        if (first == '[') {
          upper = line.toUpperCase();
          block = 0;
          if (upper.equals("[RETURNCODE]")) //$NON-NLS-1$
            block = 1;
//V2.03
          if (upper.equals("[STATISTICS]")) //$NON-NLS-1$
            block = 2;
//V2.12
          if (upper.equals("[RESULTS]")) //$NON-NLS-1$
            block = 3;
        } else {
          if (block == 1) {
            if (line.length() > 4) {
              rcs = line.substring(3, 5).trim();
              if (!(rcs.equals(""))) //$NON-NLS-1$
                block = 0;
            }
          }
//V2.03 begin
          if (block == 2) {
            upper = line.toUpperCase();
            int nl = upper.indexOf('=');
            if (nl > 0) {
              nl++;
              if (line.length() > nl && statCount < 10) {
                statName[++statCount] = upper.substring(0,(nl-1));
                String stv = line.substring(nl).trim();
                statValue[statCount] = Integer.parseInt(stv);
              }
            }
          }
//2.03 end
//V2.12 begin
          if (block == 3) {
            upper = line.toUpperCase();
            int nl = upper.indexOf('=');
            if (nl > 0) {
              nl++;
              if (line.length() > nl && resultCount < 10) {
                resultName[++resultCount] = upper.substring(0,(nl-1));
                resultValue[resultCount] = line.substring(nl).trim();
              }
            }
          }
//2.12 end
        }
      }
      inifileIn.close();
      return Integer.parseInt(rcs);
    } catch (IOException e) {
      // abend program
      //  V2.02
//V2.17    	
//      ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_call_programm_cmd") + cmd; //$NON-NLS-1$
      ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_call_programm_cmd") + cmdarray[0] + " " + cmdarray[1]; //$NON-NLS-1$
      ErrMsg2 = e.toString();
//      System.out.println(RIGlobal_Messages.getString("RIGlobal.error_call_programm_cmd") + cmd); //$NON-NLS-1$
      System.out.println(ErrMsg1);
      System.out.println(e.toString());
      return (16);
    }

    catch (InterruptedException ie) {
      // abend program
      //  V2.02
//V2.17    	
//      ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_call_programm_cmd") + cmd; //$NON-NLS-1$
      ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_call_programm_cmd") + cmdarray[0] + " " + cmdarray[1]; //$NON-NLS-1$
      ErrMsg2 = ie.toString();
//      System.out.println(RIGlobal_Messages.getString("RIGlobal.error_call_programm_cmd") + cmd); //$NON-NLS-1$
      System.out.println(ErrMsg1);
      System.out.println(ie.toString());
      return (16);
    }
    catch (Exception e) {
      // abend program
      //  V2.03
//V2.17    	
//      ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_call_programm_cmd") + cmd; //$NON-NLS-1$
      ErrMsg1 = RIGlobal_Messages.getString("RIGlobal.error_call_programm_cmd") + cmdarray[0] + " " + cmdarray[1]; //$NON-NLS-1$
      ErrMsg2 = e.toString();
//      System.out.println(RIGlobal_Messages.getString("RIGlobal.error_call_programm_cmd") + cmd); //$NON-NLS-1$
      System.out.println(ErrMsg1);
      System.out.println(e.toString());
      return (16);
    }

  }
//------------------------------------------------------------------
//initPgm: initialize program variables
//------------------------------------------------------------------
public static void initPgm() {
//general msg texts
//V2.16
//msgTexts    = new String[200];
msgTexts    = new String[999];
for ( int i = 0; i < msgTexts.length; i++)
 msgTexts[i] = " ";
msgTexts[0] = "not used"; //$NON-NLS-1$
msgTexts[1] = "reserved for start message"; //$NON-NLS-1$
msgTexts[2] = RIGlobal_Messages.getString("RIGlobal.file_handling_error"); //$NON-NLS-1$
msgTexts[3] = RIGlobal_Messages.getString("RIGlobal.wrong_VORLAUF_parameter"); //$NON-NLS-1$
msgTexts[4] = RIGlobal_Messages.getString("RIGlobal.wrong_VORLAUF_parameter_value"); //$NON-NLS-1$
msgTexts[5] = RIGlobal_Messages.getString("RIGlobal.unhandled_program_exception"); //$NON-NLS-1$
msgTexts[6] = RIGlobal_Messages.getString("RIGlobal.ROCHADE_parameter_missing"); //$NON-NLS-1$
msgTexts[7] = RIGlobal_Messages.getString("RIGlobal.no_access_to_ROCHADE_host"); //$NON-NLS-1$
msgTexts[8] = RIGlobal_Messages.getString("RIGlobal.arror_access_ROCHADE_host"); //$NON-NLS-1$
msgTexts[9] = RIGlobal_Messages.getString("RIGlobal.successful_access_ROCHADE_host"); //$NON-NLS-1$
msgTexts[10] = RIGlobal_Messages.getString("RIGlobal.error_from_sort"); //$NON-NLS-1$
msgTexts[11] = RIGlobal_Messages.getString("RIGlobal.exception_from_sort"); //$NON-NLS-1$
msgTexts[12] = RIGlobal_Messages.getString("RIGlobal.wrong_record_in_TABROCH"); //$NON-NLS-1$
msgTexts[13] = RIGlobal_Messages.getString("RIGlobal.errors_found_in_TABROCH"); //$NON-NLS-1$
msgTexts[14] = RIGlobal_Messages.getString("RIGlobal.wrong_record_in_NONDEL"); //$NON-NLS-1$
msgTexts[15] = RIGlobal_Messages.getString("RIGlobal.wrong_record_in_TABSEL"); //$NON-NLS-1$
msgTexts[16] = RIGlobal_Messages.getString("RIGlobal.file_error"); //$NON-NLS-1$
//Setting of fixed values
pgmname8 = (pgmname + "      ").substring(0,8); //$NON-NLS-1$
//V2.16
if (pgmname.length() > 8) {
 pgmname8 = pgmname + " ";
}
iniLines    = new String[100];
numIniLines    = 0;
msgInfos    = new String[999];
msgAppend   = 1;
maxCode = 0;
z9 = new DecimalFormat("########0"); //$NON-NLS-1$
nnn = new DecimalFormat("000"); //$NON-NLS-1$
spaces40 = "                                        "; //$NON-NLS-1$
spaces72 =  (spaces40 + spaces40).substring(0,72);
//edit Date in engl. format: on MM/DD/YYYY at HH:MM:SS
//                        e.g. on 05/08/2003 at 13:05:06
cal = new GregorianCalendar();
engdat = new SimpleDateFormat();
engdat.applyPattern(RIGlobal_Messages.getString("RIGlobal.on_at")); //$NON-NLS-1$
listDate = engdat.format( cal.getTime()) ;
}
//------------------------------------------------------------------
//rtrim: remove spaces to right end of string
//------------------------------------------------------------------

public static String rtrim(String input) {
        int l = input.length();
        int lbn = -1;
        char last;
        for (int i = l -1; i >= 0; i = i -1 ) {
                last = input.charAt(i);
                if (!(last==' ')) {
                        lbn = i;
                        break;
                }
        }
        if ( lbn == -1) return " " ; //$NON-NLS-1$
else
        return input.substring(0,lbn + 1 );

}
//V2.13
//------------------------------------------------------------------
//modifyPgmDir: remove ...:file:\ from PgmDir
//------------------------------------------------------------------

public static void modifyPgmDir() {
//      System.out.println("pgmDir(before)=" + RIGlobal.pgmDir);
        RIGlobal.orgpgmDir = RIGlobal.pgmDir;
        int pos = RIGlobal.pgmDir.lastIndexOf(":file:");
        int lpos = RIGlobal.pgmDir.length() - 1;
        if ( !(pos == -1)) {
                pos = pos + 6;
                if (!(pos >lpos )) RIGlobal.pgmDir = RIGlobal.pgmDir.substring(pos);
                if (RIGlobal.pgmDir.substring(0,1).equals("\\"))
                        RIGlobal.pgmDir = RIGlobal.pgmDir.substring(1);

        }
//      System.out.println("pgmDir(after)=" + RIGlobal.pgmDir);
return;
}

}
