package me.vemacs.vperms.test;

import com.google.common.collect.ImmutableMap;
import me.vemacs.vperms.data.Group;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GroupInheritanceTest {
    @Test
    public void testGroupInheritance() {
        Map<String, Boolean> defaultPerms = new ImmutableMap.Builder<String, Boolean>()
                .put("smoke.weed", true)
                .put("dank.memes", false)
                .build();
        Group defaultGroup = new Group("default", Collections.<Group>emptyList(), defaultPerms);
        Map<String, Boolean> modPerms = new ImmutableMap.Builder<String, Boolean>()
                .put("ban.hammer", true)
                .build();
        Group modGroup = new Group("mod", Arrays.asList(defaultGroup), modPerms);
        Map<String, Boolean> adminPerms = new ImmutableMap.Builder<String, Boolean>()
                .put("hack.the.gibson", true)
                .build();
        Group adminGroup = new Group("admin", Arrays.asList(modGroup), adminPerms);
        Map<String, Boolean> premiumPerms = new ImmutableMap.Builder<String, Boolean>()
                .put("dank.memes", true)
                .build();
        Group premiumGroup = new Group("premium", Arrays.asList(defaultGroup), premiumPerms);
        Group premiumMod = new Group("premmod", Arrays.asList(premiumGroup, modGroup),
                Collections.<String, Boolean>emptyMap());
        // Double inheritance is intended
        Group specialAdminUser =  new Group("special", Arrays.asList(premiumGroup, modGroup, adminGroup),
                Collections.<String, Boolean>emptyMap());

        Group circularDependency1;
        Group circularDependency2;
        circularDependency1 = new Group("circ1", Collections.<Group>emptyList(), defaultPerms);
        circularDependency2 = new Group("circ2", Arrays.asList(circularDependency1), defaultPerms);
        circularDependency1.setParents(Arrays.asList(circularDependency2));

        Group[] toTest = {defaultGroup, modGroup, adminGroup, premiumGroup, premiumMod, specialAdminUser,
                circularDependency1, circularDependency2};
        for (Group g : toTest) {
            System.out.println("Testing " + g.getName());
            System.out.println("Ordered parents: " + groupCollectionToString(g.getParents()));
            System.out.println("Group tree: " + groupCollectionToString(g.calculateGroupTree()));
            System.out.println("Effective permissions: ");
            for (Map.Entry<String, Boolean> entry : g.computeEffectivePermissions().entrySet())
                System.out.println("- " + entry.getKey() + ", " + entry.getValue().toString());
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