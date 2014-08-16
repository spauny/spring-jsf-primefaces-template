spring-jsf-primefaces-template
==========

This is a template project using latest Spring 3.2, latest JSF and latest Primefaces. It's not just a hello world. It has some jsf templating in place, spring components, lombok annotation, primefaces interaction. 

The tool is using the port 9996. You also need JAVA 7 for it to run!

To start it either : 

use startTool.sh 
======== 
(the sh file also starts the browser after the server started). When you start it for the first time it may take a while to download all the dependencies and the page might be opened before the server starts. What you can do is wait for the maven package to end and for the tool to start (all is done when the last line is: INFO: Starting ProtocolHandler ["http-bio-9996"]) and after that refresh the page. That's it! Next time it will take only between 20 and 30 seconds.

or 

mvn package tomcat7:run
