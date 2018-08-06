package amov.danieloliveira.batalhanaval.engine;

public class JsonMessage<T> {
    private T object;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
