package impl.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import impl.command.Command;
import impl.enums.CommandType;

public class State {
    private Graphics graphics;
    private Physics physics;
    private Map<String, Supplier<State>> transitions;
    private Moves moves;
    private String stateName;
    private Supplier<State> stateLoader; // טוען את עצמו מחדש

    /**
     * Initialize state with moves, graphics, and physics components.
     */
    public State(Moves moves, Graphics graphics, Physics physics) {
        this(moves, graphics, physics, null, null);
    }

    public State(Moves moves, Graphics graphics, Physics physics, String stateName, Supplier<State> selfLoader) {
        this.moves = moves;
        this.graphics = graphics;
        this.physics = physics;
        this.stateName = stateName;
        this.stateLoader = selfLoader;
        this.transitions = new HashMap<>();
    }

    /**
     * Set a transition from this state to another state on an event.
     */
    public void setTransition(String event, Supplier<State> target) {
        this.transitions.put(event, target);
    }

    /**
     * Reset the state with a new command.
     */
    public void reset(Command cmd) {
        int now = (int) System.currentTimeMillis();
        Command safeCmd = (cmd != null) ? cmd : new Command(now, "system", CommandType.IDLE);
        safeCmd.setTimestamp(now);
        graphics.reset(safeCmd);
        int[] target = (cmd != null && cmd.getParams().size() >= 2)
                ? parseCell(cmd.getParams().get(1)) // target cell from command
                : physics.getEndCell(); // default to physics end cell

        physics.reset(safeCmd, target, now);
    }

    /**
     * Update the state based on current time.
     */
    public State update(int nowMs) {
        // Command cmd = getCommand();
        // if (cmd != null) {
        //     return processCommand(cmd, nowMs);
        // }

        graphics.update(nowMs);

        if (physics.isFinished(nowMs)) {
            Supplier<State> supplier = transitions.get("done");
            if (supplier != null) {
                State nextState = supplier.get();
                System.out.printf("[STATE] %s → %s%n", this.stateName, nextState.stateName);
                nextState.reset(null); // חשוב! כדי שה-graphics ייטען מחדש
                return nextState;
            } else {
                System.out.printf("[STATE] Missing transition 'done' in state '%s'%n", stateName);
            }
        }

        return this; // stay in the same state
    }

    private int[] parseCell(String cell) {
        char colChar = Character.toUpperCase(cell.charAt(0));
        int col = colChar - 'A';
        int row = Integer.parseInt(cell.substring(1)) - 1;
        return new int[] { row, col };
    }

    /**
     * Get the next state after processing a command.
     */
    public State processCommand(Command cmd) {
        String type = cmd.getType().toString().toLowerCase();

        if (transitions.containsKey(type)) {
            State next = transitions.get(type).get();
            next.reset(cmd);
            return next;
        }

        System.out.println("[STATE] No transition for command " + type);
        return null;
    }

    /**
     * Check if the state can transition.
     * Customise per state.
     */
    public boolean canTransition(int nowMs) {
        // ...existing code...
        return false;
    }

    /**
     * Get the current command for this state.
     */
    public Command getCommand() {
        // ...existing code...
        return null;
    }

    public Img getCurrentSprite(int nowMs) {
        return graphics.getImg(); // Placeholder, replace with actual implementation
    }

    // Getters and setters
    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public Physics getPhysics() {
        return physics;
    }

    public void setPhysics(Physics physics) {
        this.physics = physics;
    }

    public Map<String, Supplier<State>> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<String, Supplier<State>> transitions) {
        this.transitions = transitions;
    }

    public Moves getMoves() {
        return moves;
    }
}