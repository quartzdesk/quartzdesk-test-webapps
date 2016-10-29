# Installation Instructions For Apache Tomcat

## 1. Logs Directory

Create an empty directory where QuartzDesk Test Web Application (QTWA) will store their log files.
          
Add the following JVM system property at the top of `TOMCAT_HOME/bin/catalina.bat` (Windows) or `TOMCAT_HOME/bin/catalina.sh` (Unix, Linux, Mac). Alternatively, you can add the property to `TOMCAT_HOME/bin/setenv.bat` (Windows) or `TOMCAT_HOME/bin/setenv.sh` (Unix, Linux, Mac). 

#### Windows
```
set CATALINA_OPTS=%CATALINA_OPTS% -Dquartzdesk-test.logs.dir=<LOGS_DIR>
```

#### Unix, Linux, Mac
```
CATALINA_OPTS="${CATALINA_OPTS} -Dquartzdesk-test.logs.dir=<LOGS_DIR>"
```



## 2. Enable Remote JMX Access

In order to connect and manage Quartz schedulers embedded in any of the QTWA application from the QuartzDesk GUI, you need to enable remote JMX access to the JVM the QTWA application will be running on. The steps depend on the QuartzDesk edition that you intend to use.

This section uses the following placeholders in the described configuration properties:

| Placeholder | Description |
|-------------|-------------|
| JMX_HOST    | Hostname or IP of the host the QTWA application will be deployed on. | 
| JMX_PORT    | Arbitrary port number, the JMX connection will be established on. |

### QuartzDesk Lite Edition

Add the following JVM system properties to `TOMCAT_HOME/bin/catalina.bat` (Windows) or `TOMCAT_HOME/bin/catalina.sh`. Alternatively, you can add the properties to `TOMCAT_HOME/bin/setenv.bat` (Windows) or `TOMCAT_HOME/bin/setenv.sh` (Unix, Linux, Mac).

#### Windows
```
set CATALINA_OPTS=%CATALINA_OPTS% -Djava.rmi.server.hostname=JMX_HOST
set CATALINA_OPTS=%CATALINA_OPTS% -Djavax.management.builder.initial
set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote=true
set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote.port=JMX_PORT
set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote.ssl=false
set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote.authenticate=false
```

#### Unix, Linux, Mac
```
CATALINA_OPTS="${CATALINA_OPTS} -Djava.rmi.server.hostname=JMX_HOST" 
CATALINA_OPTS="${CATALINA_OPTS} -Djavax.management.builder.initial" 
CATALINA_OPTS="${CATALINA_OPTS} -Dcom.sun.management.jmxremote=true" 
CATALINA_OPTS="${CATALINA_OPTS} -Dcom.sun.management.jmxremote.port=JMX_PORT" 
CATALINA_OPTS="${CATALINA_OPTS} -Dcom.sun.management.jmxremote.ssl=false"
CATALINA_OPTS="${CATALINA_OPTS} -Dcom.sun.management.jmxremote.authenticate=false"
```

### QuartzDesk Standard / Enterprise Edition
 
In order to use the advanced functionality of the QuartzDesk platform, you will need to install the QuartzDesk JVM Agent. For details on how to install the agent, please refer to the [QuartzDesk JVM Agent Installation and Upgrade Guide](https://www.quartzdesk.com/documentation/installation-and-upgrade-guides) for your application server / servlet container.
  
The QuartzDesk JVM Agent provides two JMX connectors that can be enabled in the agent configuration file (`quartzdesk-agent.properties`). If you decide not to use the QuartzDesk JVM Agent nor these JMX connectors, please follow the steps for the QuartzDesk Lite edition. 
  
#### JMX/RMI Connector
```
jmxConnector.rmi.enabled = true
jmxConnector.rmi.registryPort = JMX_PORT
jmxConnector.rmi.serverPort = JMX_PORT
```

*Please note the registry port number and server port number can be identical.*

#### JMXMP Connector
```
jmxConnector.jmxmp.enabled = true
jmxConnector.jmxmp.port = JMX_PORT
```
 


## 3. Deploy QTWA WAR(s) 

Deploy the selected QTWA application(s) by copying its/their WAR file(s) to the `TOMCAT_HOME/webapps` directory.

Start Tomcat and check its logs (`TOMCAT_HOME/logs`) and see if the QTWA application was started successfully. You may also want to check the QTWA log files created in the configured logs directory. 
