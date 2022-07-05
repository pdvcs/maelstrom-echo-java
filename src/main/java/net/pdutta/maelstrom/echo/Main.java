package net.pdutta.maelstrom.echo;

public class Main {
    public static void main(String[] args) {
        final var app = new EchoApp();
        app.loopOverStdin();
    }
}