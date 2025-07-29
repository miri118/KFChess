package impl.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import impl.enums.CommandType;

public class Command {
    private int timestamp;
    private String pieceId;
    private CommandType type;
    private List<String> params;

    public Command(int timestamp, String pieceId, CommandType type, List<String> params) {
        if (pieceId == null || type == null)
            throw new IllegalArgumentException("pieceId and type must not be null");
        this.timestamp = timestamp;
        this.pieceId = pieceId;
        this.type = type;
        this.params = (params != null) ? new ArrayList<>(params) : new ArrayList<>();
    }

    public Command(int timestamp, String pieceId, CommandType type) {
        this(timestamp, pieceId, type, new ArrayList<>());
    }

    @Override
    public Command clone() {
        return new Command(timestamp, pieceId, type, new ArrayList<>(params));
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getPieceId() {
        return pieceId;
    }

    public CommandType getType() {
        return type;
    }

    public List<String> getParams() {
        return new ArrayList<>(params);
    }

    public CommandType getCommandType() {
        return type;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setPieceId(String pieceId) {
        this.pieceId = pieceId;
    }

    public void setType(String typeStr) {
        this.type = CommandType.valueOf(typeStr.toUpperCase());
    }

    public void setCommandType(CommandType type) {
        this.type = type;
    }

    public void setParams(List<String> params) {
        this.params = (params != null) ? new ArrayList<>(params) : new ArrayList<>();
    }

    public void addParam(String param) {
        this.params.add(param);
    }

    @Override
    public String toString() {
        return String.format("Command{timestamp=%d, pieceId='%s', type='%s', params=%s}",
                timestamp, pieceId, type.name(), params);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Command))
            return false;
        Command other = (Command) obj;
        return timestamp == other.timestamp &&
                pieceId.equals(other.pieceId) &&
                type == other.type &&
                params.equals(other.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, pieceId, type, params);
    }
}