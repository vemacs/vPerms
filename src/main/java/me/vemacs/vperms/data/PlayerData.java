package me.vemacs.vperms.data;

import java.util.Arrays;
import java.util.Map;

public class PlayerData extends Group {
    public PlayerData(String name, Group primaryGroup, Map<String, Boolean> permissions) {
        super(name, Arrays.asList(primaryGroup), permissions);
    }
}
