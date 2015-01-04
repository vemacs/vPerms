package me.vemacs.vperms.test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import me.vemacs.vperms.data.Group;
import me.vemacs.vperms.storage.GroupDataSource;

import java.util.*;

public class TestGroupDataSource extends GroupDataSource {
    public Map<String, Group> testDataSet = new LinkedHashMap<>();

    public TestGroupDataSource() {
        Map<String, Boolean> defaultPerms = Maps.newHashMap(new ImmutableMap.Builder<String, Boolean>()
                .put("smoke.weed", true)
                .put("dank.memes", false)
                .build());
        Group defaultGroup = new Group("default", Collections.<String>emptyList(), defaultPerms);
        Map<String, Boolean> modPerms = new ImmutableMap.Builder<String, Boolean>()
                .put("ban.hammer", true)
                .build();
        Group modGroup = new Group("mod", Arrays.asList(defaultGroup.getName()), modPerms);
        Map<String, Boolean> adminPerms = new ImmutableMap.Builder<String, Boolean>()
                .put("hack.the.gibson", true)
                .build();
        Group adminGroup = new Group("admin", Arrays.asList(modGroup.getName()), adminPerms);
        Map<String, Boolean> premiumPerms = new ImmutableMap.Builder<String, Boolean>()
                .put("dank.memes", true)
                .build();
        Group premiumGroup = new Group("premium", Arrays.asList(defaultGroup.getName()), premiumPerms);
        Group premiumMod = new Group("premmod", Arrays.asList(premiumGroup.getName(), modGroup.getName()),
                Collections.<String, Boolean>emptyMap());
        // Double inheritance is intended
        Group specialAdminUser =  new Group("special", Arrays.asList(premiumGroup.getName(),
                modGroup.getName(), adminGroup.getName()),
                Collections.<String, Boolean>emptyMap());

        Group circularDependency1, circularDependency2;
        circularDependency1 = new Group("circ1", Collections.<String>emptyList(), defaultPerms);
        circularDependency2 = new Group("circ2", Arrays.asList(circularDependency1.getName()), defaultPerms);
        circularDependency1.setParents(Arrays.asList(circularDependency2.getName()));

        Group ub1, ub2, ub3;
        ub1 = new Group("ub1", Collections.<String>emptyList(), defaultPerms);
        ub2 = new Group("ub2", Arrays.asList(ub1.getName()), defaultPerms);
        ub3 = new Group("ub3", Arrays.asList(ub2.getName()), defaultPerms);
        ub1.setParents(Arrays.asList(ub3.getName()));

        List<Group> groups = Arrays.asList(defaultGroup, modGroup, adminGroup, premiumGroup, premiumMod, specialAdminUser,
                circularDependency1, circularDependency2, ub1, ub2, ub3);

        for (Group g : groups)
            testDataSet.put(g.getName(), g);
    }

    @Override
    public List<String> getDefinedGroups() {
        return new ArrayList<>(testDataSet.keySet());
    }

    @Override
    protected Group loadGroup(String name) {
        if (!groupCache.containsKey(name))
            return testDataSet.get(name);
        // Purely for testing, our perms maps should be mutable in normal cases
        groupCache.get(name).getPermissions().put("ayy.lmao", true);
        return groupCache.get(name);
    }

    @Override
    public void saveGroup(Group toSave) {

    }

    @Override
    public void deleteGroup(Group toDelete) {

    }
}
