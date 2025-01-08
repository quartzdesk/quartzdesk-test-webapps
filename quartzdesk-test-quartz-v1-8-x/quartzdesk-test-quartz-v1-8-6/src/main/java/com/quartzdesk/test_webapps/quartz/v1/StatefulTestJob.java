/*
 * Copyright (c) 2013-2025 QuartzDesk.com.
 * Licensed under the MIT license (https://opensource.org/licenses/MIT).
 */

package com.quartzdesk.test_webapps.quartz.v1;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test job that implements the {@link StatefulJob} interface. Every time this job is executed, it increments
 * the value of the {@code execCount} job data map parameter in the job detail.
 */
public class StatefulTestJob
    extends TestJob
    implements StatefulJob
{
  private static final Logger log = LoggerFactory.getLogger( StatefulTestJob.class );

  /**
   * Name of the job data map parameter containing the execution counter that is incremented
   * every time this job is executed.
   */
  private static final String JDM_KEY_EXEC_COUNT = "execCount";


  @Override
  protected void executeJob( JobExecutionContext context )
      throws JobExecutionException
  {
    super.executeJob( context );

    JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

    String execCountStr = jobDataMap.getString( JDM_KEY_EXEC_COUNT );
    if ( execCountStr == null )
    {
      jobDataMap.putAsString( JDM_KEY_EXEC_COUNT, 0 );
    }
    else
    {
      try
      {
        Integer execCount = Integer.parseInt( execCountStr );
        jobDataMap.putAsString( JDM_KEY_EXEC_COUNT, ++execCount );
      }
      catch ( NumberFormatException e )
      {
        log.warn( "Invalid value of " + JDM_KEY_EXEC_COUNT + " job data map parameter: " + execCountStr +
            ". Setting its value to 0.", e );

        jobDataMap.putAsString( JDM_KEY_EXEC_COUNT, 0 );
      }
    }
  }
}
