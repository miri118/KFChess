package impl.command;
import java.util.List;

import impl.enums.CommandType;
/**
 * Produces Command instances from game input or events.
 */
public class CommandProducer{

    
    public Command createMoveCommand(String pieceId, String from, String to, int timestamp) {
        return new Command(timestamp, pieceId, CommandType.MOVE, List.of(from, to));
    }

    public Command createJumpCommand(String pieceId, String from, String to, int timestamp) {
        return new Command(timestamp, pieceId, CommandType.JUMP, List.of(from, to));
    }

    public Command createIdleCommand(String pieceId, int timestamp) {
        return new Command(timestamp, pieceId, CommandType.IDLE);
    }

    public Command createLongRestCommand(String pieceId, int timestamp) {
        return new Command(timestamp, pieceId, CommandType.LONG_REST);
    }

    public Command createShortRestCommand(String pieceId, int timestamp) {
        return new Command(timestamp, pieceId, CommandType.SHORT_REST);
    }

    public Command createGenericCommand(String pieceId, String type, List<String> params, int timestamp) {
        CommandType cmdType = CommandType.valueOf(type.toUpperCase());
        return new Command(timestamp, pieceId, cmdType, params);
    }
}