/*
 * Copyright (c) 2013-2019 QuartzDesk.com.
 * Licensed under the MIT license (https://opensource.org/licenses/MIT).
 */

package com.quartzdesk.test.quartz.v2;

import com.quartzdesk.test.common.CommonConst;

import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJob
    extends AbstractJob
{
  private static final Logger log = LoggerFactory.getLogger( TestJob.class );

  /**
   * Name of the job data map parameter indicating this job should set the
   * result in the job execution context. The value of the result is the value
   * of the job data map parameter.
   */
  private static final String JDM_KEY_TEST_RESULT = "result";

  /**
   * Name of the job data map parameter indicating this job should set the
   * user-data in the job execution context. The value of the user-data is the value
   * of the job data map parameter.
   */
  private static final String JDM_KEY_TEST_USER_DATA = "userData";

  /**
   * Name of the job data map parameter indicating this should should throw
   * a JobExecutionException. The message of the exception is set to the
   * value of the job data map parameter.
   */
  private static final String JDM_KEY_TEST_EXCEPTION = "exception";

  /**
   * Name of the job data map parameter containing the number of seconds
   * this job should sleep for before finishing.
   */
  private static final String JDM_KEY_SLEEP_TIME = "sleepTime";


  /**
   * Returns true if this job should be interrupted, false otherwise. The
   * implementation of this method in this class always returns false the
   * {@link TestJob} class is not interruptible (does not implement the Quartz
   * {@link InterruptableJob}.
   * <p>
   * Interruptible job subclasses are expected to override this method.
   * </p>
   *
   * @return Returns true if this job should be interrupted, false otherwise.
   */
  protected boolean isInterrupted()
  {
    return false;
  }


  @Override
  protected void executeJob( JobExecutionContext context )
      throws JobExecutionException
  {
    log.debug( "Inside job: {}", context.getJobDetail().getKey() );

    JobDataMap jobDataMap = context.getMergedJobDataMap();

    log.trace( "This is TRACE message." );
    log.debug( "This is DEBUG message." );
    log.info( "This is INFO message." );
    log.warn( "This is WARN message." );
    log.error( "This is ERROR message." );

    log.debug( "This message contains special XML characters: <, >, &, \", '" );
    log.debug( "This message contains special JavaScript characters: \", ', \\" );

    // optionally set the result
    Object quartzdeskResult = jobDataMap.get( JDM_KEY_TEST_RESULT );
    if ( quartzdeskResult != null )
    {
      log.debug( "Setting test result: {} in the job execution context: {}", quartzdeskResult, context );
      context.setResult( quartzdeskResult );
    }

    // optionally set the user-data
    Object quartzdeskUserData = jobDataMap.get( JDM_KEY_TEST_USER_DATA );
    if ( quartzdeskUserData != null )
    {
      log.debug( "Setting test user-data: {} in the job execution context: {}", quartzdeskUserData, context );
      context.put( "USER_DATA", quartzdeskUserData );
    }

    // optionally set the sleep time
    Object sleepTime = jobDataMap.get( JDM_KEY_SLEEP_TIME );
    if ( sleepTime != null )
    {
      try
      {
        Integer sleepTimeSeconds = Integer.parseInt( sleepTime.toString() );
        log.info( "About to sleep for {} seconds.", sleepTimeSeconds );

        for ( int i = 0; i < sleepTimeSeconds; i++ )
        {
          if ( shouldBeInterrupted() )
          {
            log.warn( "Interrupting execution of this job." );
            break;
          }

          log.info( "Sleeping for 1 second." );
          Thread.sleep( CommonConst.MILLS_IN_SECOND );  // sleep for 1 second
          log.info( "Woke up from sleep ({}/{}).", i + 1, sleepTimeSeconds );
        }
      }
      catch ( NumberFormatException e )
      {
        log.error( "Invalid value of the " + JDM_KEY_SLEEP_TIME + " job data map parameter: " + sleepTime, e );
      }
      catch ( InterruptedException e )
      {
        log.warn( "Job interrupted.", e );
      }
    }

    // optionally set the exception
    Object quartzdeskException = jobDataMap.get( JDM_KEY_TEST_EXCEPTION );
    if ( quartzdeskException != null )
    {
      log.debug( "Throwing test exception with message: {}", quartzdeskException );
      throw new JobExecutionException( quartzdeskException.toString() );
    }
  }
}
