package alpha.attack;

import alpha.model.*;

import alpha.GameState;

public interface Attack {
    void tick(GameState gs);
    default void start(GameState gs) {}
    default void end(GameState gs) {}
}
