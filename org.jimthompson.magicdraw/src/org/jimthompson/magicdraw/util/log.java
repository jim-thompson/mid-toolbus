// Copyright © 2019 MID GmbH 
// smartfacts support for MagicDraw and Cameo
// Author: James Thompson <jim.thompson@pobox.com>

package org.jimthompson.magicdraw.util;

import org.apache.log4j.Logger;

public class log
{
	public static Logger og_logger = Logger.getRootLogger();
	public static int og_depth = 0;
	public static String og_prefix = "";

	public static boolean og_show_info = true;
	public static boolean og_show_debug = true;

	// This should always be true: we should ALWAYS show errors.
	public static final boolean og_show_errs = true;

	public static void setDepth(int depth)
	{
		og_depth = depth;
		og_prefix = "";
		for (int i = 0; i < og_depth; i++)
		{
			og_prefix += "|   ";
		}
	}

	public static void incrementDepth()
	{
		setDepth(og_depth + 1);
	}

	public static void decrementDepth()
	{
		setDepth(og_depth - 1);
	}

	public static void info(String message)
	{
		if (og_show_info)
		{
			//				og_logger.info(message);
			System.out.println(og_prefix + message);
		}
	}

	public static void info(String pre, String message)
	{
		if (og_show_info)
		{
			//				og_logger.info(message);
			System.out.println(pre + " " + og_prefix + message);
		}
	}

	public static void info()
	{
		if (og_show_info)
		{
			System.out.println();
		}
	}	

	public static void error(String message)
	{
		if (og_show_errs)
		{
			//		logger.info(message);
			System.err.println(og_prefix + message);
			System.err.flush();
		}
	}

	public static void error()
	{
		if (og_show_errs)
		{
			System.err.println();
			System.err.flush();
		}
	}	
	public static void debug(String message)
	{
		if (og_show_debug)
		{
			//		logger.info(message);
			System.out.println(og_prefix + message);
			System.out.flush();
		}
	}

	public static void debug()
	{
		if (og_show_debug)
		{
			System.out.println();
			System.out.flush();
		}
	}
}

// Local Variables:
// tab-width: 4
// fill-column: 108
// End:
