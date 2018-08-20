package amov.danieloliveira.batalhanaval;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class SerializableHashSet <T> extends HashSet<T> implements Serializable {
    private static final long serialVersionUID = 1;

    public SerializableHashSet() {
    }

    public SerializableHashSet(@NonNull Collection<? extends T> c) {
        super(c);
    }

    public SerializableHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public SerializableHashSet(int initialCapacity) {
        super(initialCapacity);
    }
}
