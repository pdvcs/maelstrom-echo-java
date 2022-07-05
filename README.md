# A Simple Echo Server in Java

This is a Java version of the Echo Server [described here](https://github.com/jepsen-io/maelstrom/blob/main/doc/02-echo/index.md).

Requires Java 17 and Linux.

## Build and Deploy

```shell
mvn package    # produces target/JavaEcho-0.1-SNAPSHOT-jar-with-dependencies.jar
cp target/JavaEcho-0.1-SNAPSHOT-jar-with-dependencies.jar ~/repos/maelstrom-bin/JavaEcho.jar 
cp jecho.sh ~/repos/maelstrom-bin/
```

## Run

```shell
./maelstrom test -w echo --bin ./jecho.sh --time-limit 5
```
