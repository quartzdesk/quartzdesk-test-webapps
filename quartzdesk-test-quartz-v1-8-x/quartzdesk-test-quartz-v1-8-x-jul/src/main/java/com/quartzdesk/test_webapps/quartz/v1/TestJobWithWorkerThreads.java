/*
 * Copyright (c) 2013-2024 QuartzDesk.com.
 * Licensed under the MIT license (https://opensource.org/licenses/MIT).
 */

package com.quartzdesk.test_webapps.quartz.v1;

import com.quartzdesk.api.agent.log.WorkerThreadLoggingInterceptorRegistry;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Extension of the {@link TestJob} that spawns the specified number of worker threads and waits for these threads to
 * finish.
 */
public class TestJobWithWorkerThreads
    extends TestJob
{
  private static final Logger log = LoggerFactory.getLogger( TestJobWithWorkerThreads.class );

  private static final int DEFAULT_WORKER_THREAD_COUNT = 3;

  /**
   * Name of the job data map parameter containing the number of spawned worker
   * threads. The maximum number of spawned threads is capped at 5.
   */
  private static final String JDM_KEY_WORKER_THREAD_COUNT = "workerThreadCount";

  private static final int MAX_WORKER_THREAD_COUNT = 5;     // do not use more than 5 worker threads


  @Override
  protected void executeJob( JobExecutionContext context )
      throws JobExecutionException
  {
    super.executeJob( context );

    JobDataMap jobDataMap = context.getMergedJobDataMap();

    Integer workerThreadCount = DEFAULT_WORKER_THREAD_COUNT;

    // determine the worker thread count
    if ( jobDataMap.containsKey( JDM_KEY_WORKER_THREAD_COUNT ) )
    {
      workerThreadCount = jobDataMap.getIntegerFromString( JDM_KEY_WORKER_THREAD_COUNT );
    }

    if ( workerThreadCount > MAX_WORKER_THREAD_COUNT )
    {
      log.warn(
          "Requested worker thread count: {} exceeds the maximum allowed worker thread count: {}. Capping worker thread count to: {}.",
          workerThreadCount, MAX_WORKER_THREAD_COUNT, MAX_WORKER_THREAD_COUNT );

      workerThreadCount = MAX_WORKER_THREAD_COUNT;
    }

    log.debug( "Using worker thread count: {}", workerThreadCount );

    // prepare the thread pool
    ExecutorService executor = Executors.newFixedThreadPool( workerThreadCount, new ThreadFactory()
    {
      private AtomicInteger counter = new AtomicInteger( 0 );


      @Override
      public Thread newThread( Runnable r )
      {
        Thread thread = new Thread( new ThreadGroup( "ExecutorWorkers" ), r );
        thread.setName( "ExecutorWorker-" + counter.incrementAndGet() );
        return thread;
      }
    } );

    CountDownLatch doneSignal = new CountDownLatch( workerThreadCount );

    // spawn worker threads and wait for them to finish
    for ( int i = 0; i < workerThreadCount; i++ )
    {
      CountToTenRunnable countToTen = new CountToTenRunnable( Thread.currentThread(), doneSignal );
      executor.submit( countToTen );  // we do not care about the result
    }

    // wait for all worker threads to finish
    try
    {
      doneSignal.await();
    }
    catch ( InterruptedException e )
    {
      log.warn( "Job thread interrupted while waiting for its worker threads to finish.", e );
    }
    finally
    {
      executor.shutdown();
    }
  }


  private static class CountToTenRunnable
      implements Callable<Integer>
  {
    private static final Random RANDOM = new Random();

    private CountDownLatch doneSignal;

    private Thread jobThread;


    private CountToTenRunnable( Thread jobThread, CountDownLatch doneSignal )
    {
      this.jobThread = jobThread;
      this.doneSignal = doneSignal;
    }


    @Override
    public Integer call()
        throws Exception
    {
      WorkerThreadLoggingInterceptorRegistry.INSTANCE.startIntercepting( jobThread );
      try
      {
        log.debug( "Started {}, worker thread: {}, job thread: {}",
            CountToTenRunnable.class.getName(), Thread.currentThread(), jobThread );

        for ( int i = 1; i <= 10; i++ )
        {
          log.debug( "Count: {}", i );

          // sleep for max 3 seconds
          Thread.sleep( Math.abs( RANDOM.nextInt( 3000 ) ) );
        }

        return 10;
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace();
        log.warn( "Worker thread interrupted.", e );
      }
      finally
      {
        doneSignal.countDown();
        WorkerThreadLoggingInterceptorRegistry.INSTANCE.stopIntercepting();
      }

      return 10;
    }
  }
}
