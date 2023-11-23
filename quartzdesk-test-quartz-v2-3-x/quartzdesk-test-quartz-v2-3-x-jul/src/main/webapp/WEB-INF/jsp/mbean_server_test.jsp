<%@ page import="javax.management.MBeanServer" %>
<%@ page import="javax.management.ObjectInstance" %>
<%@ page import="java.lang.management.ManagementFactory" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jstl/fn" %>

<%--
  ~ Copyright (c) 2013-2024 QuartzDesk.com.
  ~ Licensed under the MIT license (https://opensource.org/licenses/MIT).
  --%>

<html>
  <body>
    <pre>
    Domains:
    <%
      MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
      String[] domains = mbeanServer.getDomains();

      for ( String domain : domains )
      {
    %>
    <%= domain %>
    <%
      }
    %>

    MBeans:
    <%
      // we do not know the object name
      Set<ObjectInstance> mbeans = mbeanServer.queryMBeans( null, null );
      for ( ObjectInstance mbean : mbeans )
      {
    %>
    Objectname: <%= mbean.getObjectName() %> Classname: <%= mbean.getClassName() %>
    <%
      }
    %>
    </pre>
  </body>
</html>