package org.vinsert.api.impl.game;

import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.vinsert.api.Worlds;
import org.vinsert.api.wrappers.World;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.util.Collections.sort;
import static org.vinsert.api.util.Utilities.random;

/**
 * API implementation for retrieving information from the world list.
 *
 * @author tobiewarburton
 */
public final class WorldsImpl implements Worlds {
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_" +
            "9_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";

    public WorldsImpl() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public World getEmptiest() {
        List<World> worlds = getAll();
        sort(worlds, new Comparator<World>() {
            @Override
            public int compare(World o1, World o2) {
                return o1.getPlayers() - o2.getPlayers();
            }
        });
        return worlds.size() > 0 ? worlds.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public World getBusiest() {
        List<World> worlds = getAll();
        sort(worlds, new Comparator<World>() {
            @Override
            public int compare(World o1, World o2) {
                return o2.getPlayers() - o1.getPlayers();
            }
        });
        return worlds.size() > 0 ? worlds.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public World getAny() {
        List<World> worlds = getAll();
        return (worlds.size() > 0) ? worlds.get(random(0, worlds.size() - 1)) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void switchTo(World world) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<World> getAll() {
        final List<World> worlds = newArrayList();
        try {
            String worldListTxt = Request.Get("http://oldschool.runescape.com/slu")
                    .userAgent(USER_AGENT).version(HttpVersion.HTTP_1_1)
                    .execute().returnContent().toString();
            Pattern pattern = Pattern.compile(".\\((.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?)\\);");
            Matcher matcher = pattern.matcher(worldListTxt);
            while (matcher.find()) {
                int worldNumber = Integer.parseInt(matcher.group(1));
                boolean members = parseBoolean(matcher.group(2));
                String worldIdent = matcher.group(4).replaceAll("\"", "");
                int players = parseInt(matcher.group(5));
                String country = matcher.group(6).replaceAll("\"", "");
                String worldName = matcher.group(8).replaceAll("\"", "").split(",")[0];

                World model = new World();
                model.setName(worldName);
                model.setCountry(country);
                model.setMembers(members);
                model.setWorldNo(worldNumber);
                model.setPlayers(players);
                model.setIdent(worldIdent);

                worlds.add(model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return worlds;
    }

}
