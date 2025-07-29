package impl.command;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandQueue {

    private final Queue<Command> queue;
    public CommandQueue() {
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void add(Command cmd) {
        if (cmd == null) throw new IllegalArgumentException("Command cannot be null");
        queue.add(cmd);
    }

    public Command poll() {
        return queue.poll();
    }

    public Command peek() {
        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void clear() {
        queue.clear();
    }

    public int size() {
        return queue.size();
    }

    public List<Command> getAll() {
        return new ArrayList<>(queue);
    }

    public List<Command> popUntil(int nowMs) {
        List<Command> result = new ArrayList<>();
        while (!queue.isEmpty() && queue.peek().getTimestamp() <= nowMs) {
            result.add(queue.poll());
        }
        return result;
    }
}