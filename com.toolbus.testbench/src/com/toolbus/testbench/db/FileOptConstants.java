package com.toolbus.testbench.db;

import org.jimthompson.magicdraw.options.FileOptValuesProvider;

public class FileOptConstants implements FileOptValuesProvider
{
	private static String _mac_test_base = "test-models";
	private static String _win_test_base = _mac_test_base;
	private static String _lin_test_base = _mac_test_base;
	
	public static String _bases[] = { _mac_test_base, _win_test_base, _lin_test_base };

	public static int _model_to_load = 1;

	public static String[] _test_models =
		{
			/* 0 */ "EmptyUML",
			/* 1 */ "UseCaseTest",
			/* 2 */ "RIWWC01S",
		};
	
	public String[] directories() { return _bases; }
	public String[] fileNames() { return _test_models; }
	public int fileNumber() { return _model_to_load; }
}
