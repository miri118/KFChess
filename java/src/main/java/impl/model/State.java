package impl.model;
import java.util.HashMap;
import java.util.Map;

import impl.command.Command;
import impl.physics.Physics;

public class State {
    private Graphics graphics;
    private Physics physics;
    private Map<String, State> transitions;
    private Moves moves;
    /**
     * Initialize state with moves, graphics, and physics components.
     */
    public State(Moves moves, Graphics graphics, Physics physics) {
        this.graphics = graphics;
        this.physics = physics;
        this.transitions = new HashMap<>();
    }

    /**
     * Set a transition from this state to another state on an event.
     */
    public void setTransition(String event, State target) {
        this.transitions.put(event, target);
    }

    /**
     * Reset the state with a new command.
     */
    public void reset(Command cmd) {
        if (cmd == null) {
        // כשאין פקודה, נשתמש רק במצב הנוכחי ולא נזרוק NPE
        return;
    }
        cmd.setTimestamp((int) System.currentTimeMillis());
        graphics.reset(cmd);
        physics.reset(cmd);
    }

    /**
     * Update the state based on current time.
     */
    public State update(int nowMs) {
        graphics.reset(null);
        Command cmd = getCommand();
        if (cmd != null) {
            return processCommand(cmd, nowMs);
        }
        return this;
    }

    /**
     * Get the next state after processing a command.
     */
    public State processCommand(Command cmd, int nowMs) {
        State res = transitions.get(cmd.getType());
        if (res == null) {
            return null;
        }
        res.reset(cmd);
        return res;
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

    public Map<String, State> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<String, State> transitions) {
        this.transitions = transitions;
    }
}