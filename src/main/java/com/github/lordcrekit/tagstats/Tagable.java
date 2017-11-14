package com.github.lordcrekit.tagstats;

import java.util.Objects;

public abstract class Tagable {

    public final String Name;

    protected Tagable(final String name) {
        Name = Objects.requireNonNull(name);
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof Tagable
                && this.Name.equals(((Tagable) o).Name);
    }

    @Override
    public int hashCode() {
        return Name.hashCode();
    }

    @Override
    public String toString() {
        return "T:" + Name;
    }
}
