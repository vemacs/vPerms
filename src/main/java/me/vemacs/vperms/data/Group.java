package me.vemacs.vperms.data;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class Group {
    @NonNull
    private String name;
    @NonNull
    private List<Group> parents;

    public static <T> List<T> squash(List<T> toSquash) {
        List<T> tmp = new ArrayList<>();
        for (T element : toSquash)
            if (!tmp.contains(element)) tmp.add(element);
        return tmp;
    }

    public List<Group> calculateGroupTree() {
        List<Group> tree = new ArrayList<>();
        tree.add(0, this);
        for (Group top : parents) {
            if (top.getName().equalsIgnoreCase(getName()))
                continue;
            for (Group trunk : calculateBackwardTree(top))
                tree.add(0, trunk);
        }
        return squash(tree);
    }

    private List<Group> calculateBackwardTree(Group group) {
        List<Group> tree = new ArrayList<>();
        tree.add(group);
        for (Group top : group.getParents()) {
            if (top.getName().equalsIgnoreCase(group.getName()))
                continue;
            for (Group trunk : calculateBackwardTree(top))
                tree.add(trunk);
        }
        return tree;
    }
}

