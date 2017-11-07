package com.ziamor.incadium.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

public abstract class SortedIteratingSystem extends EntitySystem {
    protected Array<Integer> sorted;
    protected boolean unsorted = true;

    @Override
    public void removed(Entity e) {
        unsorted = true;
    }

    @Override
    public void inserted(Entity e) {
        unsorted = true;
    }

    /**
     * Creates a new SortedIteratingSystem.
     *
     * @param aspect the aspect to match entities
     */
    public SortedIteratingSystem(Aspect.Builder aspect) {
        super(aspect);
    }


    /**
     * Process a entity this system is interested in.
     *
     * @param entityId the entity to process
     */
    protected abstract void process(int entityId);

    /**
     * @inheritDoc
     */
    @Override
    protected final void processSystem() {
        if (unsorted) {
            unsorted = false;
            if (sorted == null)
                sorted = new Array<Integer>();
            sorted.clear();
            IntBag actives = subscription.getEntities();
            int[] ids = actives.getData();
            for (int i = 0; i < ids.length; i++)
                sorted.add(ids[i]);
            sorted.sort(getComparator());
        }

        for (int i = 0; i < sorted.size; i++)
            process(sorted.get(i));

    }

    public abstract Comparator<Integer> getComparator();
}
