package amov.danieloliveira.batalhanaval.engine;

import amov.danieloliveira.batalhanaval.engine.enums.MsgType;

public class JsonMessage<T> {
    private MsgType type;
    private T object;

    public JsonMessage(T object, MsgType type) {
        this.type = type;
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public MsgType getType() {
        return type;
    }
}
