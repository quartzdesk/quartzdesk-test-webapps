package com.quartzdesk.test.quartz.v2;

import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test job annotated with the {@link DisallowConcurrentExecution} annotation.
 */
@DisallowConcurrentExecution
public class DisallowConcurrentExecutionTestJob
    extends TestJob
{
  private static final Logger log = LoggerFactory.getLogger( DisallowConcurrentExecutionTestJob.class );
}
