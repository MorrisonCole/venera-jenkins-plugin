# Venera Jenkins Plugin

The Venera Jenkins Plugin maintains a HSQL database of standard test artifacts and their associated Venera JSON logs.
It will eventually be responsible for Venera result presentation and analysis.

## Developer Guide

### Requirements

To build the project from source, the following tools are required:

- [Java Development Kit (v.7+)](http://www.oracle.com/technetwork/java/javase/downloads/index.html?ssSourceSiteId=otnjp)
- [Maven (v.3+)](http://maven.apache.org/download.cgi)

I recommend using [IntelliJ](http://www.jetbrains.com/idea/) or some other Java-centric IDE, although anything is fine
in principal.

### Running the Plugin

From the command line:

```
mvn hpi:run
```

IntelliJ users can create a build configuration with type Maven and working directory set to the root of the project to
execute the same command.

## Common Problems

When executing mvn hpi:run I get "java.net.BindException: Address already in use".

Something is using the TCP port (probably a previous instance that failed to shut down correctly). On Linux, running lsof -i tcp:$PORT will give you the list of processes using TCP port $PORT, which you can then kill with 'kill $PID'.
