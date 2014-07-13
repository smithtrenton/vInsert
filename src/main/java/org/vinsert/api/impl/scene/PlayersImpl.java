package org.vinsert.api.impl.scene;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Players;
import org.vinsert.api.collection.queries.PlayerQuery;
import org.vinsert.api.wrappers.Player;
import org.vinsert.game.engine.renderable.entity.IPlayer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * API implementation for accessing RuneScape player objects.
 *
 * @see org.vinsert.api.Players
 */
public final class PlayersImpl implements Players {
    private final MethodContext ctx;

    @Inject
    public PlayersImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getLocal() {
        IPlayer local = ctx.client.getLocalPlayer();
        if (local != null) {
            return new Player(ctx, local);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Player> getAll() {
        List<Player> players = newArrayList();
        IPlayer[] playerArray = ctx.client.getPlayers();
        for (IPlayer player : playerArray) {
            if (player != null) {
                players.add(new Player(ctx, player));
            }
        }
        return players;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerQuery find() {
        return new PlayerQuery(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerQuery find(String... names) {
        return find().named(names);
    }

}
