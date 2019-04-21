/*
 * Copyright (c) 2013-2019 QuartzDesk.com.
 * Licensed under the MIT license (https://opensource.org/licenses/MIT).
 */

package com.quartzdesk.test.quartz.v2;

import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Extension of the {@link TestJob} that is interruptible.
 */
public class InterruptibleTestJob
    extends TestJob
    implements InterruptableJob
{
  private static final Logger log = LoggerFactory.getLogger( InterruptibleTestJob.class );

  /**
   * Name of the job data map parameter containing the flag indicating
   * if this job's interrupt method should fail with an {@link UnableToInterruptJobException}.
   */
  private static final String JDM_KEY_INTERRUPT_EXCEPTION = "interruptException";

  private String interruptException;

  private AtomicBoolean interruptFlag = new AtomicBoolean( false );


  @Override
  public void interrupt()
      throws UnableToInterruptJobException
  {
    log.warn( "Received interrupt request." );
    if ( interruptException == null )
    {
      log.warn( "Interrupting job." );
      interruptFlag.set( true );
    }
    else
    {
      log.warn( "Failing job interrupt with message: {}", interruptException );
      throw new UnableToInterruptJobException( interruptException );
    }
  }


  @Override
  protected boolean shouldBeInterrupted()
  {
    return interruptFlag.get();
  }

  @Override
  protected void executeJob( JobExecutionContext context )
      throws JobExecutionException
  {
    JobDataMap jobDataMap = context.getMergedJobDataMap();

    // optionally set the exception
    Object quartzdeskInterruptException = jobDataMap.get( JDM_KEY_INTERRUPT_EXCEPTION );
    if ( quartzdeskInterruptException != null )
    {
      log.debug( "Interrupt attempts will throw {} with message: {}", UnableToInterruptJobException.class.getName(),
          quartzdeskInterruptException );

      interruptException = quartzdeskInterruptException.toString();
    }

    super.executeJob( context );
  }
}
