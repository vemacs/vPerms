package me.vemacs.vperms.storage;

import me.vemacs.vperms.data.Group;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GroupDataSource {
    protected Map<String, Group> groupCache = new ConcurrentHashMap<>();

    /**
     * Loads the list of defined groups from backend
     * @return list of defined groups
     */
    public abstract List<String> getDefinedGroups();

    /**
     * Returns cached group with given name, loads if not cached
     * @param name
     * @return Group with given name
     */
    public Group getGroup(String name) {
        name = name.toLowerCase();
        if (groupCache.containsKey(name)) return groupCache.get(name);
        return updateGroup(name);
    }

    /**
     * Puts latest version of group with given name in cache
     * @param name
     * @return Latest version of group
     */
    public Group updateGroup(String name) {
        name = name.toLowerCase();
        Group g = loadGroup(name);
        groupCache.put(name, g);
        return g;
    }

    /**
     *
     * @param name
     * @return Latest version of group from backend
     */
    protected abstract Group loadGroup(String name);

    /**
     * Serializes the group to the backend
     * @param toSave
     */
    public abstract void saveGroup(Group toSave);

    public abstract void deleteGroup(Group toDelete);
}
