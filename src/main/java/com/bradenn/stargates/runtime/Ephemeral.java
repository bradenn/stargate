package com.bradenn.stargates.runtime;


import java.util.List;
import java.util.UUID;

/**
 * Adjective: ephemeral
 * lasting for a very short time.
 */
public interface Ephemeral extends Distinct {

    float getTicks();

    void tick();

    float getLifeTicks();

    float getUpdateTicks();

    List<UUID> getUUIDS();

    void initiate();

    void update();

    void terminate();

}
