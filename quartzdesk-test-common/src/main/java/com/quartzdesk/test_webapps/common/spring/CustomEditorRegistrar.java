/*
 * Copyright (c) 2013-2025 QuartzDesk.com.
 * Licensed under the MIT license (https://opensource.org/licenses/MIT).
 */

package com.quartzdesk.test_webapps.common.spring;

import com.quartzdesk.test_webapps.common.spring.propertyeditors.CustomCalendarEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CustomEditorRegistrar
    implements PropertyEditorRegistrar
{
  private static final Logger log = LoggerFactory.getLogger( CustomEditorRegistrar.class );


  @Override
  public void registerCustomEditors( PropertyEditorRegistry registry )
  {
    TimeZone timeZone = TimeZone.getDefault();

    // String -> Date
    SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
    dateFormat.setTimeZone( timeZone );

    registry.registerCustomEditor( Date.class,
        new CustomDateEditor( dateFormat, false ) );

    // String -> Calendar
    SimpleDateFormat calendarFormat =  new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS" );
    calendarFormat.setTimeZone( timeZone );

    registry.registerCustomEditor( Calendar.class,
        new CustomCalendarEditor( calendarFormat, false ) );
  }
}
