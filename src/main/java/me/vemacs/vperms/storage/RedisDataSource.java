package me.vemacs.vperms.storage;

import com.google.gson.Gson;
import me.vemacs.vperms.data.Group;
import me.vemacs.vperms.vPermsPlugin;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RedisDataSource extends GroupDataSource {
    private static final Gson gson = new Gson();
    private static final String PREFIX = "vperms.group.";
    private static final String DEFINED_KEY = PREFIX + "defined";

    public RedisDataSource() {
        for (String s : getDefinedGroups())
            updateGroup(s);
    }

    @Override
    public List<String> getDefinedGroups() {
        try (Jedis jedis = vPermsPlugin.getJedisPool().getResource()) {
            return new ArrayList<>(jedis.smembers(DEFINED_KEY));
        }
    }

    @Override
    protected Group loadGroup(String name) {
        Group group;
        try (Jedis jedis = vPermsPlugin.getJedisPool().getResource()) {
            group = gson.fromJson(jedis.get(PREFIX + name), Group.class);
        }
        if (group == null) {
            // Everything here should be mutable, assume creation of nonexistent group?
            // TODO: handle group creation specifically
            group = new Group(name, new ArrayList<String>(), new LinkedHashMap<String, Boolean>());
        }
        return group;
    }

    @Override
    public void saveGroup(Group group) {
        String json = gson.toJson(group);
        try (Jedis jedis = vPermsPlugin.getJedisPool().getResource()) {
            jedis.set(PREFIX + group.getName(), json);
            jedis.sadd(DEFINED_KEY, group.getName());
        }
    }

    @Override
    public void deleteGroup(Group group) {
        groupCache.remove(group.getName());
        try (Jedis jedis = vPermsPlugin.getJedisPool().getResource()) {
            jedis.srem(DEFINED_KEY, group.getName());
            jedis.del(PREFIX + group.getName());
        }
    }
}
