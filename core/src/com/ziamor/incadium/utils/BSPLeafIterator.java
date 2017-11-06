package com.ziamor.incadium.utils;

import java.util.Iterator;
import java.util.LinkedList;

public class BSPLeafIterator implements Iterator<BSP.LeafNode> {
    private LinkedList<BSP.LeafNode> leafs;

    public BSPLeafIterator(LinkedList<BSP.LeafNode> list) {
        this.leafs = list;
    }

    @Override
    public boolean hasNext() {
        return !leafs.isEmpty();
    }

    @Override
    public BSP.LeafNode next() {
        return leafs.removeFirst();
    }
}
