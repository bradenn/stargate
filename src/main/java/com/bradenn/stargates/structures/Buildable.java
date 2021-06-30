package com.bradenn.stargates.structures;

public interface Buildable {

    default void rebuild() {
        destroy();
        build();
    }

    void build();

    void destroy();

}
