package fr.xebia.codeelevator;

import java.util.List;

import static java.util.Arrays.asList;

public class ElevatorEngine {
    private List<Command> commands = asList(
            Command.OPEN, Command.CLOSE, Command.UP,
            Command.OPEN, Command.CLOSE, Command.UP,
            Command.OPEN, Command.CLOSE, Command.UP,
            Command.OPEN, Command.CLOSE, Command.UP,
            Command.OPEN, Command.CLOSE, Command.UP,
            Command.OPEN, Command.CLOSE, Command.DOWN,
            Command.OPEN, Command.CLOSE, Command.DOWN,
            Command.OPEN, Command.CLOSE, Command.DOWN,
            Command.OPEN, Command.CLOSE, Command.DOWN,
            Command.OPEN, Command.CLOSE, Command.DOWN
    );

    private int currentCommand = -1;

    public Command nextCommand() {
        return commands.get(currentCommand++ % commands.size());
    }

    public void call(int atFloor, Direction to) {
    }

    public void go(int floorToGo) {
    }

    public void userHasEntered() {
    }

    public void userHasExited() {
    }

    public void reset(String cause) {
        currentCommand = -1;
    }
}
