Common Problems
===============

When executing mvn hpi:run I get "java.net.BindException: Address already in use".

Something is using the TCP port (probably a previous instance that failed to shut down correctly). On Linux, running lsof -i tcp:$PORT will give you the list of processes using TCP port $PORT, which you can then kill with 'kill $PID'.
