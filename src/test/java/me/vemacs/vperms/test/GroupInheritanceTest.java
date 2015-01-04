package me.vemacs.vperms.test;

import me.vemacs.vperms.data.Group;
import me.vemacs.vperms.storage.GroupDataSource;
import me.vemacs.vperms.vPermsPlugin;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class GroupInheritanceTest {
    @Test
    public void testGroupInheritance() {
        GroupDataSource gds = new TestGroupDataSource();
        vPermsPlugin.setDataSource(gds);
        System.out.println("--Running group test--");
        testAllGroups(gds);
        System.out.println("--Running updated group test--");
        gds.updateGroup("default");
        testAllGroups(gds);
    }

    public void testAllGroups(GroupDataSource gds) {
        for (String gName : gds.getDefinedGroups()) {
            Group g = gds.getGroup(gName);
            System.out.println("Testing " + g.getName());
            System.out.println("Ordered parents: " + joiner(g.getParents()));
            System.out.println("Group tree: " + joiner(g.calculateGroupTree()));
            System.out.println("Effective permissions: ");
            for (Map.Entry<String, Boolean> entry : g.computeEffectivePermissions().entrySet())
                System.out.println("- " + entry.getKey() + ", " + entry.getValue().toString());
        }
    }

    public String joiner(List<String> toJoin) {
        if (toJoin.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (String s : toJoin)
            sb.append(s).append(", ");
        return sb.substring(0, sb.length() - 2);
    }
}