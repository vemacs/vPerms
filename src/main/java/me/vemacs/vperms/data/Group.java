package me.vemacs.vperms.data;

import com.google.common.collect.ImmutableSet;
import lombok.Data;
import lombok.NonNull;
import me.vemacs.vperms.vPermsPlugin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class Group {
    @NonNull
    private String name;
    @NonNull
    private List<String> parents;
    @NonNull
    private Map<String, Boolean> permissions;

    public Group(String name, List<String> parents, Map<String, Boolean> permissions) {
        this.name = name.toLowerCase();
        this.parents = parents;
        this.permissions = permissions;
    }

    public Map<String, Boolean> computeEffectivePermissions() {
        Map<String, Boolean> perms = new LinkedHashMap<>();
        for (String ancestor : calculateGroupTree())
            perms.putAll(getGroupFor(ancestor).getPermissions());
        return perms;
    }

    public static <T> List<T> squash(List<T> toSquash) {
        return ImmutableSet.copyOf(toSquash).asList();
    }

    // Adapted from Privileges code
    public List<String> calculateGroupTree() {
        List<String> tree = new ArrayList<>();
        tree.add(0, getName());
        for (String top : parents) {
            if (top.equalsIgnoreCase(getName())) {
                continue;
            }
            for (String trunk : calculateBackwardTree(top)) {
                tree.add(0, trunk);
            }
        }
        return squash(tree);
    }

    private List<String> calculateBackwardTree(String group) {
        List<String> tree = new ArrayList<>();
        tree.add(group);
        for (String top : getGroupFor(group).getParents()) {
            if (top.equalsIgnoreCase(group)) {
                continue;
            }
            if (getGroupFor(top).getParents().contains(group)) {
                String errorMessage = "Group " + getName() + " has a circular inheritance issue.";
                try {
                    vPermsPlugin.getInstance().getLogger().warning(errorMessage);
                } catch (NullPointerException e) {
                    System.out.println("[WARNING] " + errorMessage);
                }
                continue;
            }
            for (String trunk : calculateBackwardTree(top)) {
                tree.add(trunk);
            }
        }
        return tree;
    }

    public static Group getGroupFor(String name) {
        return vPermsPlugin.getDataSource().getGroup(name);
    }
}

