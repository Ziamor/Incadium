package com.ziamor.incadium;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class SystemSetupBuilder {
    Array<BaseSystem> systems;
    ArrayMap<String, Array<BaseSystem>> systemGroups;

    public SystemSetupBuilder() {
        systems = new Array<BaseSystem>();
        systemGroups = new ArrayMap<String, Array<BaseSystem>>();
    }

    public SystemSetupBuilder add(BaseSystem system, String group) {
        if (system == null || group == null) {
            Gdx.app.log("System Builder", "Error adding new system"); //TODO throw some kind of exception
            return this;
        }
        systems.add(system);
        if (!systemGroups.containsKey(group))
            systemGroups.put(group, new Array<BaseSystem>());

        Array<BaseSystem> currentGroup = systemGroups.get(group);
        currentGroup.add(system);
        return this;
    }

    public BaseSystem[] getSystemArray() {
        BaseSystem[] arr = new BaseSystem[systems.size];
        for (int i = 0; i < arr.length; i++)
            arr[i] = systems.get(i);
        return arr;
    }

    public Array<BaseSystem> getGroup(String group) {
        return systemGroups.get(group);
    }
}
