package com.ziamor.incadium.utils;

import java.util.Iterator;
import java.util.LinkedList;

public class BSPPostorderIterator implements Iterator<BSP.Node> {
    private LinkedList<BSP.Node> nodes;

    public BSPPostorderIterator(LinkedList<BSP.Node> list) {
        this.nodes = list;
    }

    @Override
    public boolean hasNext() {
        return !nodes.isEmpty();
    }

    @Override
    public BSP.Node next() {
        return nodes.removeFirst();
    }
}
