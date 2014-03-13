package fr.xebia.codeelevator;

public class ElevatorEngine {
    private static final int DEFAULT_LOWER_FLOOR = 0;
    private static final int DEFAULT_HIGHER_FLOOR = 5;

    private int lowerFloor = DEFAULT_LOWER_FLOOR;
    private int higherFloor = DEFAULT_HIGHER_FLOOR;
    private Direction currentDirection;
    private Command currentCommand;
    private int floor;

    public ElevatorEngine() {
        reset("", Integer.toString(DEFAULT_LOWER_FLOOR), Integer.toString(DEFAULT_HIGHER_FLOOR));
    }

    public Command nextCommand() {
        switch (currentCommand) {
            case OPEN:
                currentCommand = Command.CLOSE;
                break;
            case CLOSE:
                switch (currentDirection) {
                    case UP:
                        if (floor == higherFloor) {
                            currentDirection = Direction.DOWN;
                        }
                        break;
                    case DOWN:
                        if (floor == lowerFloor) {
                            currentDirection = Direction.UP;
                        }
                }
                switch (currentDirection) {
                    case UP:
                        floor++;
                        currentCommand = Command.UP;
                        break;
                    case DOWN:
                        floor--;
                        currentCommand = Command.DOWN;
                        break;
                }
                break;
            default:
                currentCommand = Command.OPEN;
        }
        return currentCommand;
    }

    public void call(int atFloor, Direction to) {
    }

    public void go(int floorToGo) {
    }

    public void userHasEntered() {
    }

    public void userHasExited() {
    }

    public void reset(String cause, String lowerFloor, String higherFloor) {
        this.lowerFloor = lowerFloor != null ? Integer.parseInt(lowerFloor) : DEFAULT_LOWER_FLOOR;
        this.higherFloor = higherFloor != null ? Integer.parseInt(higherFloor) : DEFAULT_HIGHER_FLOOR;
        this.currentDirection = Direction.UP;
        this.currentCommand = Command.UP;
        this.floor = 0;
    }
}
