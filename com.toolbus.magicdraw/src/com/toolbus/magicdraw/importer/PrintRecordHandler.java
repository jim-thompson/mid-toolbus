package com.toolbus.magicdraw.importer;

import com.toolbus.riinc.RIGlobal;
import com.toolbus.riinc.RITBRec;

public class PrintRecordHandler implements RecordHandlerIF
{
	String _prev_id = null;
	
	@Override
	public void setup()
	{
		RIGlobal.printStartMsg("<stdout>");

		System.out.println("  =============================================================================================================");

		String h = RITBRecUtil.putFieldsCompactHdr();
		System.out.println(h);
		
		String hc = RITBRecUtil.putFieldsCompactHdrCont();
		System.out.println(hc);
		
		System.out.println();
	}

	@Override
	public void handleRecord(RITBRec ritb_rec)
	{
		String s = RITBRecUtil.putFieldsCompact(ritb_rec);
		
		if ((_prev_id != null) && (_prev_id.compareTo(ritb_rec.entity1) != 0))
		{
			System.out.println();
		}
		
		System.out.println(s);
		
		_prev_id = ritb_rec.entity1;
	}

	@Override
	public void teardown()
	{
		System.out.println("  =============================================================================================================");
	}
}