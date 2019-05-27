# Introduction

QuartzDesk Test Web Applications (QTWA) are simple Java test web applications that use various Quartz scheduler versions and logging frameworks (jul, log4j, log4j2, logback). These applications are primarily intended for testing of our Java Quartz scheduler management and monitoring platform [QuartzDesk](http://www.quartzdesk.com).

# Requirements

### Java
JDK 1.7 or newer.

### Application Container / Server
Any modern Java servlet container or application server. QTWA are known to work on:

* Apache Tomcat
* IBM WebSphere AS
* Oracle GlassFish AS
* Oracle WebLogic AS
* RedHat JBoss AS
* WildFly AS

### Database
 
No database is required. All test web applications use a Quartz RAM job store. 


# Available Quartz Job Implementation Classes

QTWA provide the following Quartz job implementation classes that can be used to simulate various job execution conditions, e.g. a job execution failure, long running job execution etc. 

## ![](media/job-impl-class-16x16.png) com.quartzdesk.test.quartz.vX.TestJob
A non-interruptible Quartz test job implementation class.

This job supports the following job data map parameters:

`exception`: an optional parameter that makes the test job throw JobExecutionException with an optional message passed in the parameter's value. This parameter can be used to simulate job execution failures.

`veto`: an optional parameter that makes the configured trigger listener veto the test job's execution. This parameter can be used to simuate job execution vetoing.

`sleepTime`: an optional parameter that makes the test job enter a busy-wait loop causing the job to wait for the specified number of seconds. This parameter can be used to simulate long-running job executions because normally the test job's execution finishes within a couple of milliseconds.

`result`: an optional parameter that makes the test job [set the execution result](http://www.quartz-scheduler.org/api/2.2.1/org/quartz/JobExecutionContext.html#setResult(java.lang.Object)) to the specified value. The result value can then be displayed in the execution history view in the QuartzDesk GUI.

`userData`: an optional parameter that makes the test job set the QuartzDesk user-data value. The user-data value can be though of as 'result2' and it can be used by applications that already make use of the job execution result and want expose some additional job execution related data that can be displayed in the execution history view in the QuartzDesk GUI.
 

## ![](media/job-impl-class-16x16.png) com.quartzdesk.test.quartz.vX.InterruptibleTestJob
An interruptible test job implementation class that extends the `com.quartzdesk.test.quartz.vX.TestJob` class. Besides the standard `com.quartzdesk.test.quartz.vX.TestJob` job data map parameters, this 
implementation class supports the following job data map parameter: 

`interruptException`: an optional parameter that makes the test job throw an [org.quartz.UnableToInterruptJobException](http://www.quartz-scheduler.org/api/2.2.1/org/quartz/UnableToInterruptJobException.html) when the jobs receives an interrupt request.


## ![](media/job-impl-class-16x16.png) com.quartzdesk.test.quartz.vX.DisallowConcurrentExecutionTestJob
A test job implementation class that extends the `com.quartzdesk.test.quartz.vX.TestJob` class and that is annotated with the [DisallowConcurrentExecution](http://www.quartz-scheduler.org/api/2.2.1/org/quartz/DisallowConcurrentExecution.html) annotation that prevents concurrent execution of multiple instances of a job that makes use of this job implementation class.    


## ![](media/job-impl-class-16x16.png) com.quartzdesk.test.quartz.vX.PersistJobDataAfterExecutionTestJob
A test job implementation class that extends the `com.quartzdesk.test.quartz.vX.TestJob` class and that is annotated with the [DisallowConcurrentExecution](http://www.quartz-scheduler.org/api/2.2.1/org/quartz/DisallowConcurrentExecution.html) and [PersistJobDataAfterExecution](http://www.quartz-scheduler.org/api/2.2.1/org/quartz/PersistJobDataAfterExecution.html) annotations. This implementation class increments the value of the `execCount` job data map parameter in the job detail. Since this class is annotated with the [PersistJobDataAfterExecution](http://www.quartz-scheduler.org/api/2.2.1/org/quartz/PersistJobDataAfterExecution.html) annotation, the updated `execCount` value is persisted and passed to the job during its next execution.


## ![](media/job-impl-class-16x16.png) com.quartzdesk.test.quartz.vX.TestJobWithWorkerThreads  
A test job implementation class that extends the `com.quartzdesk.test.quartz.vX.TestJob` class and that spawns multiple worker threads from the main job execution thread. This job implementation class is used for testing of interception of log messages produced by the spawned worker threads. 


## ![](media/job-impl-class-16x16.png) com.quartzdesk.api.scheduler.quartz.vX.job.spring.SpringBeanInvokerJob
A test job implementation class that invokes a configured Spring bean method. This job supports the following job data map parameters:

`targetBeanName`: a name of the target Spring bean.

`targetBeanClassName`: a fully-qualified class name of the target Spring bean. This parameter is required and used only if no `targetBeanName` is specified. 

`targetBeanMethodName`: a name of the Spring bean method to invoke. The method must not have any parameters.

Note: SpringBeanInvokerJob relies on an accessor bean that provides access to the current Spring application context and its beans. The accessor bean can have an arbitrary name and its class must be set to `com.quartzdesk.api.spring.ApplicationContextAccessor`. The accessor bean can be defined as follows:

```
<bean id="applicationContextAccessor"
      class="com.quartzdesk.api.spring.ApplicationContextAccessor"/>
```

Both Spring bean bean implementation classes are part of the QuartzDesk Public API Library JAR.


# Installation

**QTWA can be deployed to any Java servlet container or application server**. At this time we only provide installation instructions for Apache Tomcat. For details, please refer to [INSTALLATION-Tomcat.md](INSTALLATION-Tomcat.md). 


# Forking

We encourage you to fork, extend, repackage and distribute QTWA and all derivative work as you want. We only kindly ask you to follow these guidelines:

* Please change the root package name of your forked version from `com.quartzdesk` to a different package name so that it is clear your forked version is not the original QTWA version that is maintained by us. 
* Please do not refer to your forked version as QuartzDesk Test Web Applications, nor as QTWA. 
* Please consider including a link to our QuartzDesk Test Web Application GitHub repository (https://github.com/quartzdesk/quartzdesk-test-webapps) in your documentation. 

Thank you.


# Contributing

If you want to contribute your changes and improvements, please contact us so that we can send you our contributing guidelines. Included are coding standards, and notes on development.


# Copyright and License

Code and documentation copyright 2013-2019 the QuartzDesk Test Web Applications authors and QuartzDesk.com. Code and docs released under the MIT license.
