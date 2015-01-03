package me.vemacs.vperms.test;

import me.vemacs.vperms.data.Group;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupInheritanceTest {
    @Test
    public void testGroupInheritance() {
        Group defaultGroup = new Group("default", Collections.<Group>emptyList());
        Group modGroup = new Group("mod", Arrays.asList(defaultGroup));
        Group adminGroup = new Group("admin", Arrays.asList(modGroup));
        Group premiumGroup = new Group("premium", Arrays.asList(defaultGroup));
        Group premiumMod = new Group("premmod", Arrays.asList(premiumGroup, modGroup));
        Group specialAdminUser =  new Group("special", Arrays.asList(premiumGroup, modGroup, adminGroup));
        Group[] toTest = {defaultGroup, modGroup, adminGroup, premiumGroup, premiumMod, specialAdminUser};
        for (Group g : toTest) {
            System.out.println("Testing " + g.getName());
            System.out.println("Ordered parents: " + groupCollectionToString(g.getParents()));
            System.out.println("Group tree: " + groupCollectionToString(g.calculateGroupTree()));
        }
    }

    public String groupCollectionToString(List<Group> toDebug) {
        if (toDebug.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (Group g : toDebug)
            sb.append(g.getName()).append(", ");
        return sb.substring(0, sb.length() - 2);
    }
}