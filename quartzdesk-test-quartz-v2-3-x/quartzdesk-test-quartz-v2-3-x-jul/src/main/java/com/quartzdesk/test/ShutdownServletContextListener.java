package com.quartzdesk.test;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @version $Id:$
 */
public class ShutdownServletContextListener
    implements ServletContextListener
{
  private static final Logger log = LoggerFactory.getLogger( ShutdownServletContextListener.class );


  @Override
  public void contextDestroyed( ServletContextEvent sce )
  {
    WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext( sce.getServletContext() );
    Scheduler scheduler = appCtx.getBean( Scheduler.class );
    try
    {
      log.info( "Shutting down Quartz scheduler: " + scheduler );
      scheduler.shutdown( true );
    }
    catch ( SchedulerException e )
    {
      log.error( "Error shutting down Quartz scheduler: " + scheduler, e );
    }
  }


  @Override
  public void contextInitialized( ServletContextEvent sce )
  {
    // do nothing
  }
}
