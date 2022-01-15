/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quartzdesk.test_webapps.common.spring.propertyeditors;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Property editor for {@code java.util.Calendar} supporting a custom {@code java.text.DateFormat}.
 *
 * @see Calendar
 * @see DateFormat
 */
public class CustomCalendarEditor
    extends PropertyEditorSupport
{

  private final boolean allowEmpty;

  private final SimpleDateFormat dateFormat;


  /**
   * Create a new CustomCalendarEditor instance, using the given DateFormat
   * for parsing and rendering.
   * <p>The "allowEmpty" parameter states if an empty String should
   * be allowed for parsing, i.e. get interpreted as null value.
   * Otherwise, an IllegalArgumentException gets thrown in that case.
   *
   * @param dateFormat the DateFormat to use for parsing and rendering
   * @param allowEmpty if empty strings should be allowed
   */
  public CustomCalendarEditor( SimpleDateFormat dateFormat, boolean allowEmpty )
  {
    this.dateFormat = dateFormat;
    this.allowEmpty = allowEmpty;
  }


  /**
   * Format the Calendar as String, using the specified DateFormat.
   */
  @Override
  public String getAsText()
  {
    Calendar value = (Calendar) getValue();

    if ( value == null )
      return "";

    return dateFormat.format( value.getTime() );
  }


  /**
   * Parse the Date from the given text, using the specified DateFormat.
   */
  @Override
  public void setAsText( @Nullable String text )
      throws IllegalArgumentException
  {
    if ( allowEmpty && !StringUtils.hasText( text ) )
    {
      // Treat empty String as null value.
      setValue( null );
    }
    else
    {
      try
      {
        Date date = dateFormat.parse( text );
        Calendar calendar = Calendar.getInstance( dateFormat.getTimeZone() );
        calendar.setTime( date );

        setValue( calendar );
      }
      catch ( ParseException ex )
      {
        throw new IllegalArgumentException( "Could not parse date: " + ex.getMessage(), ex );
      }
    }
  }

}
