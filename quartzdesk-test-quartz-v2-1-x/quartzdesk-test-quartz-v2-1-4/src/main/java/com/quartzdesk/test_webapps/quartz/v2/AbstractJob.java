/*
 * Copyright (c) 2013-2025 QuartzDesk.com.
 * Licensed under the MIT license (https://opensource.org/licenses/MIT).
 */

package com.quartzdesk.test_webapps.quartz.v2;

import com.quartzdesk.test_webapps.common.CommonConst;
import com.quartzdesk.test_webapps.common.CommonUtils;

import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StopWatch;

import java.util.Iterator;

/**
 * Common base class for all jobs invoked by the Spring Quartz scheduler.
 * In the Spring framework, the scheduler running jobs extending this class
 * should be configured to use the {@link org.springframework.scheduling.quartz.SpringBeanJobFactory}.
 *
 * @see org.springframework.scheduling.quartz.QuartzJobBean
 */
public abstract class AbstractJob
    implements Job
{
  private static final Logger log = LoggerFactory.getLogger( AbstractJob.class );

  /**
   * Key of the scheduler context property that holds the {@link ApplicationContext}
   * instance. Please check refer to the {@code applicationContextSchedulerContextKey}
   * property of the {@link SchedulerFactoryBean}.
   */
  private static final String SCHEDULER_CONTEXT_APPLICATION_CONTEXT_KEY = "applicationContext";


  /**
   * The method invoked by the Spring scheduler. This method simply delegates the
   * execution to the {@link #executeJob(JobExecutionContext)} method.
   *
   * @param context a {@link JobExecutionContext} instance.
   * @throws JobExecutionException if an error occurs while executing the
   * job.
   */
  @Override
  public final void execute( JobExecutionContext context )
      throws JobExecutionException
  {
    String jobFullName = context.getJobDetail().getKey().toString();
    String triggerFullName = context.getTrigger().getKey().toString();

    StopWatch sw = new StopWatch();
    sw.start();

    ClassLoader origContextClassLoader = Thread.currentThread().getContextClassLoader();

    try
    {
      if ( log.isInfoEnabled() )
        log.info( "Started scheduled job: {}, fired by trigger: {}", jobFullName, triggerFullName );

      if ( log.isDebugEnabled() )
      {
        StringBuilder jobDataMapDump = new StringBuilder();

        // map that contains merged job data from the job detail data map and trigger data map
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        for ( Iterator<String> keys = jobDataMap.keySet().iterator(); keys.hasNext(); )
        {
          String key = keys.next();
          String value = CommonUtils.safeToString( jobDataMap.get( key ) );

          jobDataMapDump.append( key )
              .append( '=' )
              .append( value );

          if ( keys.hasNext() )
            jobDataMapDump.append( CommonConst.NL );
        }

        log.debug( "Job data map dump:{}{}", CommonConst.NL, jobDataMapDump.toString() );
      }

      // Set the context class loader to be the class loader of the job class.
      // This is a workaround/fix for a problem with setting the thread's context
      // class loader through Quartz properties when WebSphere work manager threads
      // are used.
      Thread.currentThread().setContextClassLoader( getClass().getClassLoader() );

      executeJob( context );

      sw.stop();

      if ( log.isInfoEnabled() )
        log.info( "Finished scheduled job: {}. Time taken: {}s.", jobFullName, sw.getTotalTimeSeconds() );
    }
    catch ( JobExecutionException e )
    {
      if ( log.isErrorEnabled() )
        log.error( "Error executing scheduled job: " + jobFullName, e );
      throw e;
    }
    finally
    {
      // restore the original thread context class loader
      Thread.currentThread().setContextClassLoader( origContextClassLoader );
    }
  }


  /**
   * Method that must be implemented by all jobs.
   *
   * @param context a {@link JobExecutionContext} instance.
   * @throws JobExecutionException if an error occurs.
   */
  protected abstract void executeJob( JobExecutionContext context )
      throws JobExecutionException;


  /**
   * Returns the {@link ApplicationContext} instance extracted from the scheduler context, or
   * null if not found.
   *
   * @param context the {@link JobExecutionContext} instance.
   * @return the {@link ApplicationContext} instance.
   */
  protected ApplicationContext getApplicationContext( JobExecutionContext context )
  {
    try
    {
      return (ApplicationContext) context.getScheduler().getContext().get( SCHEDULER_CONTEXT_APPLICATION_CONTEXT_KEY );
    }
    catch ( SchedulerException e )
    {
      if ( log.isErrorEnabled() )
        log.error( "Error obtaining the application context.", e );

      return null;
    }
  }


  /**
   * Returns true if this job should be interrupted, false otherwise. Interruptible
   * jobs (implementing the {@link InterruptableJob} Quartz interface) should override
   * this method.
   * <p>
   * This method in this base class always returns false.
   * </p>
   *
   * @return true if this job should be interrupted, false otherwise.
   */
  protected boolean shouldBeInterrupted()
  {
    return false;
  }

}
