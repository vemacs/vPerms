package me.vemacs.vperms.data;

import java.util.List;
import java.util.Map;

public class PlayerData extends Group {
    public PlayerData(String name, List<Group> parents, Map<String, Boolean> permissions) {
        super(name, parents, permissions);
    }
}
