package com.ziamor.incadium;

import com.artemis.BaseSystem;
import com.artemis.SystemInvocationStrategy;
import com.artemis.managers.TagManager;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;


public class IncadiumInvocationStrategy extends SystemInvocationStrategy {
    public enum Phase {
        Render,
        Turn,
        POST_TURN
    }

    protected Array<Class<? extends BaseSystem>> mandatorySystems;
    protected Array<Class<? extends BaseSystem>> renderSystems;
    protected Array<Class<? extends BaseSystem>> turnSystems;
    protected Array<Class<? extends BaseSystem>> postTurnSystems;

    public Phase phase;

    public IncadiumInvocationStrategy() {
        mandatorySystems = new Array<Class<? extends BaseSystem>>();
        renderSystems = new Array<Class<? extends BaseSystem>>();
        turnSystems = new Array<Class<? extends BaseSystem>>();
        postTurnSystems = new Array<Class<? extends BaseSystem>>();

        phase = Phase.Render;
    }

    public void setMandatorySystems(Class<? extends BaseSystem>... newSystems) {
        mandatorySystems.clear();
        for (Class<? extends BaseSystem> s : newSystems) {
            mandatorySystems.add(s);
        }
    }

    public void setRenderSystems(Class<? extends BaseSystem>... newSystems) {
        renderSystems.clear();
        for (Class<? extends BaseSystem> s : newSystems) {
            renderSystems.add(s);
        }
    }

    public void setTurnSystems(Class<? extends BaseSystem>... newSystems) {
        turnSystems.clear();
        for (Class<? extends BaseSystem> s : newSystems) {
            turnSystems.add(s);
        }
    }

    public void setPostTurnSystems(Class<? extends BaseSystem>... newSystems) {
        postTurnSystems.clear();
        for (Class<? extends BaseSystem> s : newSystems) {
            postTurnSystems.add(s);
        }
    }

    @Override
    protected void process() {
        BaseSystem[] systemsData = systems.getData();
        for (int i = 0, s = systems.size(); s > i; i++) {
            if (disabled.get(i))
                continue;

            if (useSystem(systemsData[i])) {
                updateEntityStates();
                systemsData[i].process();
            }
        }

        updateEntityStates();
    }

    protected boolean useSystem(BaseSystem system) {
        if (mandatorySystems.contains(system.getClass(), true))
            return true;

        if (phase == Phase.Render) {
            if (renderSystems.contains(system.getClass(), true))
                return true;
        } else if (phase == Phase.Turn) {
            if (turnSystems.contains(system.getClass(), true))
                return true;
        }
        return false;
    }
}
