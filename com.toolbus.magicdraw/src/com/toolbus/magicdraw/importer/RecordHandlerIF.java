package com.toolbus.magicdraw.importer;

import com.toolbus.riinc.RITBRec;

public interface RecordHandlerIF
{
	public void setup();
	public void handleRecord(RITBRec record);
	public void teardown();
}
