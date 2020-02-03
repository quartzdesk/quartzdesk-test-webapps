/*
 * Copyright (c) 2013-2020 QuartzDesk.com.
 * Licensed under the MIT license (https://opensource.org/licenses/MIT).
 */

package com.quartzdesk.test_webapps.quartz.v2;

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
