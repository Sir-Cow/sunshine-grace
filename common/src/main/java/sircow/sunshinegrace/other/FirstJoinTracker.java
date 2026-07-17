package sircow.sunshinegrace.other;

import java.util.Optional;

public interface FirstJoinTracker {
    Optional<Boolean> sunshinegrace$getHasJoinedBefore();
    void sunshinegrace$setHasJoinedBefore(Optional<Boolean> value);

    boolean sunshinegrace$getHasCheckedFirstJoin();
    void sunshinegrace$setHasCheckedFirstJoin(boolean value);
}
