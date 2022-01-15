package com.quartzdesk.test_webapps.quartz.v2;

import org.quartz.impl.calendar.HolidayCalendar;

import java.util.Date;
import java.util.List;

/**
 * A simple extension of the Quartz {@link HolidayCalendar} that allows us to initialize the set
 * of excluded dates.
 *
 * @version :$
 */
public class TestHolidayCalendar
    extends HolidayCalendar
{
  public void setExcludedDays( List<Date> excludedDates )
  {
    for ( Date excludedDate : excludedDates )
    {
      addExcludedDate( excludedDate );
    }
  }
}
