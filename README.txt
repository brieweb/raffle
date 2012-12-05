Tue Nov 20 00:26:48 PST 2012

This is "Raffle it Up" application for doing a raffle. It is written in
Java and targetted for JBoss AS7. Although, it could run on other 
application servers such as Glassfish. I just have not tried it.

Prerequisites:
Maven (3.0.x or greater)
http://maven.apache.org

JBoss AS7
http://www.jboss.org/as7

In order to build
$ cd raffle

The following will generate the meta classes. You probably don't have to run this,
but if you have missing Prize_.java and friends, this will generate the classes.

$ mvn processor:process

$ mvn compile package

If all goes well, you will see SUCCESS. Copy the resulting war file
to the standalone/deployments folder in the JBoss server. Start 
JBoss with bin/standalone.sh(bat) and go to your browser
http://localhost:8080/raffle

brian
