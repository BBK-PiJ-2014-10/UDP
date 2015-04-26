# UDP data relay server and client

CS coursework project, client initializes connection with the server over TCP and either broadcasts (if it is the first client) or receives data in a loop.

## Server setup

    git clone https://github.com/BBK-PiJ-2014-10/UDP
    cd UDP/src
    javac *.java
    java -cp . RunServer

## Client setup

Only 2 client instances can run locally - one sender and one receiver (or ``java.net.BindException: Address already in use`` will be raised).

    git clone https://github.com/BBK-PiJ-2014-10/UDP
    cd UDP/src
    javac *.java
    java -cp . RunClient

## Configuration

By default ``UDP/src/test_data.txt`` is being broadcasted, which can be changed in ``DataRelayClientImpl.initialize()`` method.

For presentation purposes the broadcasted data is also being printed by default.
This can be disabled in ``RunServer.main`` and ``RunClient.main``.

8887-8888 UDP / TCP ports need to be available for use.
