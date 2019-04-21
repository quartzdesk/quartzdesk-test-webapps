/*
 * Copyright (c) 2013-2019 QuartzDesk.com.
 * Licensed under the MIT license (https://opensource.org/licenses/MIT).
 */

package com.quartzdesk.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j2Test
{
  private static final Logger log = LoggerFactory.getLogger( Log4j2Test.class );

  public static void main( String[] args )
  {
    log.trace( "This is a TRACE message." );
    log.debug( "This is a DEBUG message." );
    log.info( "This is an INFO message." );
    log.warn( "This is a WARN message." );
    log.error( "This is an ERROR message." );

    Throwable t = new RuntimeException( "Exception message." );
    t.fillInStackTrace();

    log.error( "This is an ERROR message with exception.", t );
  }
}
