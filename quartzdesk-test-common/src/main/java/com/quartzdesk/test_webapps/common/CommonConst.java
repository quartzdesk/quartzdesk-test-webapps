/*
 * Copyright (c) 2013-2025 QuartzDesk.com.
 * Licensed under the MIT license (https://opensource.org/licenses/MIT).
 */

package com.quartzdesk.test_webapps.common;

import java.io.File;
import java.util.TimeZone;

/**
 * Common constants.
 */
public interface CommonConst
{
  /**
   * Comma.
   */
  String COMMA = ",";
  /**
   * Double-quot.
   */
  String DQUOT = "\"";
  /**
   * Empty byte array.
   */
  byte[] EMPTY_BYTE_ARRAY = new byte[0];
  /**
   * Empty string.
   */
  String EMPTY_STRING = "";
  /**
   * UTF-8 encoding.
   */
  String ENCODING_UTF8 = "UTF-8";
  /**
   * Platform dependent file separator ('/' in Unix, '\' in DOS).
   */
  String FILE_SEPARATOR = File.separator;
  /**
   * Number of hours in a day.
   */
  int HOURS_IN_DAY = 24;
  /**
   * Boolean false expressed as an integer value.
   */
  int INT_FALSE = 0;
  /**
   * Boolean true expressed as an integer value.
   */
  int INT_TRUE = 1;
  /**
   * Number of milliseconds in a second.
   */
  long MILLS_IN_SECOND = 1000L;
  /**
   * Number of minutes in an hour.
   */
  int MINUTES_IN_HOUR = 60;
  /**
   * Platform dependent new line separator.
   */
  String NL = System.getProperty( "line.separator" );
  /**
   * Not-available shortcut as a String.
   */
  String NOT_AVAIL = "n/a";
  /**
   * Platform dependent path separator (':' in Unix, ';' in DOS).
   */
  String PATH_SEPARATOR = File.pathSeparator;
  /**
   * Single quot.
   */
  String QUOT = "'";
  /**
   * Number of seconds in a minute.
   */
  int SECONDS_IN_MINUTE = 60;
  /**
   * Number of milliseconds in a minute.
   */
  long MILLS_IN_MINUTE = SECONDS_IN_MINUTE * MILLS_IN_SECOND;
  /**
   * Number of milliseconds in an hour.
   */
  long MILLS_IN_HOUR = MINUTES_IN_HOUR * MILLS_IN_MINUTE;
  /**
   * Number of milliseconds in a day.
   */
  long MILLS_IN_DAY = HOURS_IN_DAY * MILLS_IN_HOUR;
  /**
   * Number of seconds in a day.
   */
  long SECONDS_IN_HOUR = MINUTES_IN_HOUR * SECONDS_IN_MINUTE;
  /**
   * Number of seconds in a day.
   */
  long SECONDS_IN_DAY = HOURS_IN_DAY * MINUTES_IN_HOUR * SECONDS_IN_MINUTE;
  /**
   * Single space.
   */
  String SINGLE_SPACE = " ";
  /**
   * GMT time-zone.
   */
  TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone( "GMT" );
  /**
   * UTC time-zone.
   */
  TimeZone TIME_ZONE_UTC = TimeZone.getTimeZone( "UTC" );
  /**
   * XML preambule.
   */
  String XML_PREAMBULE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
}
