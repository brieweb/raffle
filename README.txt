Wed Jan 31, 2013

This is "Raffle it Up" application for doing a raffle. It is written in
Java and targeted for JBoss AS7. Although, it could run on other 
application servers such as Glassfish. I just have not tried it.

Prerequisites:
Maven (3.0.x or greater)
http://maven.apache.org

Check it with the following
$ mvn -version

JBoss AS7
http://www.jboss.org/as7

Start your jboss server. If you run Windows, use the ".bat" file instead.
$ cd <location of jboss AS 7.1>
$ bin/standalone.sh

Go to where you pulled the source for "Raffle It Up". Set your jboss_home folder.
Do this in a separate terminal
$ export JBOSS_HOME=<location of jboss AS 7.1>
$ cd raffle
$ mvn jboss-as:deploy

The following also might be useful.

The following will generate the meta classes. You probably don't have to run this,
but if you have missing Prize_.java and friends, this will generate the classes.

$ mvn processor:process

$ mvn compile package

If all goes well, you will see SUCCESS. Copy the resulting war file
to the standalone/deployments folder in the JBoss server. Start 
JBoss with bin/standalone.sh(bat) and go to your browser
http://localhost:8080/raffle

brian
