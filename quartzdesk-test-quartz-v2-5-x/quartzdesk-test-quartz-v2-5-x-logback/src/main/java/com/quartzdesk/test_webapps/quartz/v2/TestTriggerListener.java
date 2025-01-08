/*
 * Copyright (c) 2013-2025 QuartzDesk.com.
 * Licensed under the MIT license (https://opensource.org/licenses/MIT).
 */

package com.quartzdesk.test_webapps.quartz.v2;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTriggerListener
    extends TriggerListenerSupport
{
  private static final Logger log = LoggerFactory.getLogger( TestTriggerListener.class );

  /**
   * Name of the job data map parameter indicating this job execution should be vetoed.
   * The actual value of the parameter is not considered.
   */
  private static final String JDM_KEY_VETO = "veto";


  @Override
  public String getName()
  {
    return TestTriggerListener.class.getSimpleName();
  }


  @Override
  public boolean vetoJobExecution( Trigger trigger, JobExecutionContext context )
  {
    JobDataMap jobDataMap = context.getMergedJobDataMap();

    Object quartzdeskVeto = jobDataMap.get( JDM_KEY_VETO );
    if ( quartzdeskVeto != null )
    {
      log.warn( "Vetoing execution of job: {} because {} job data map parameter is present.",
          context.getJobDetail().getKey(), context );

      return true;
    }

    return false;
  }
}
