package com.ziamor.incadium.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.LinkedList;
import java.util.Random;

public class BSP {
    public Node root;
    private Random rand;
    private final int min_size = 4;

    public class Node {
        public Node left, right;
        public Rectangle area;
        public boolean verticalSplit;
    }

    public class LeafNode extends Node {
        public Rectangle room;

        public LeafNode(Node node) {
            this.area = node.area;
            this.left = node.left;
            this.right = node.right;
            this.verticalSplit = node.verticalSplit;
        }
    }

    public BSP(int width, int height, int depth) {
        rand = new Random(0x123);
        root = split(new Rectangle(0, 0, width, height), depth);
    }

    private Node split(Rectangle prevArea, int depth) {
        Node node = new Node();

        if (depth == 0) {
            LeafNode leaf = new LeafNode(node);
            leaf.area = prevArea;
            return generateRoom(leaf);
        }

        boolean verticalSplit;

        /*float sizeTotal = prevArea.width + prevArea.height;
        float widthPercent = prevArea.width / sizeTotal;
       if (rand.nextFloat() <= widthPercent)
            verticalSplit = true;
        else
            verticalSplit = false;*/

        if (prevArea.width > prevArea.height)
            verticalSplit = true;
        else
            verticalSplit = false;

        //TODO add better room splitter, eg better ratios

        //TODO add a max and min so that rooms of size 0 are generated
        float randomNormal = (float) MathUtils.clamp(rand.nextGaussian() * 0.1f + 0.5, 0.4, 0.6);
        node.area = prevArea;
        node.verticalSplit = verticalSplit;
        if (verticalSplit) {
            int splitPoint = (int) (randomNormal * prevArea.width);//rand.nextInt((int) prevArea.width);
            if (splitPoint <= min_size) {
                LeafNode leaf = new LeafNode(node);
                return generateRoom(leaf);
            }
            node.left = split(new Rectangle(prevArea.x, prevArea.y, splitPoint, prevArea.height), depth - 1);
            node.right = split(new Rectangle(prevArea.x + splitPoint, prevArea.y, prevArea.width - splitPoint, prevArea.height), depth - 1);
        } else {
            int splitPoint = (int) (randomNormal * prevArea.height);//rand.nextInt((int) prevArea.height);
            if (splitPoint <= min_size) {
                LeafNode leaf = new LeafNode(node);
                return generateRoom(leaf);
            }
            node.left = split(new Rectangle(prevArea.x, prevArea.y, prevArea.width, splitPoint), depth - 1);
            node.right = split(new Rectangle(prevArea.x, prevArea.y + splitPoint, prevArea.width, prevArea.height - splitPoint), depth - 1);
        }
        return node;
    }

    private LeafNode generateRoom(LeafNode node) {
       /* int x = ((int) node.area.width - min_size) <= 1 ? 1 : rand.nextInt((int) node.area.width - min_size);
        int y = ((int) node.area.height - min_size) <= 1 ? 1 : rand.nextInt((int) node.area.height - min_size);
        int width = Math.max(rand.nextInt((int) node.area.width - x - 1), min_size);
        int height = Math.max(rand.nextInt((int) node.area.height - y - 1), min_size);*/
        int x = rand.nextInt((int) (node.area.width / 3 - 1) + 1) + 1;
        int y = rand.nextInt((int) (node.area.height / 3 - 1) + 1) + 1;
        int width = (int) node.area.width - 1 - x;
        int height =  (int) node.area.height - 1 - y;

        width -= rand.nextInt((int) (node.area.width / 3));
        height -= rand.nextInt((int) (node.area.height / 3));

        node.room = new Rectangle(x + node.area.x, y + node.area.y, width, height);
        return node;
    }

    public BSPLeafIterator getLeafIterator() {
        LinkedList<LeafNode> leafs = getLeafs(new LinkedList<LeafNode>(), root);
        return new BSPLeafIterator(leafs);
    }

    private LinkedList<LeafNode> getLeafs(LinkedList<LeafNode> leafs, Node node) {
        if (node == null)
            return leafs;
        // leaf node
        if (node instanceof LeafNode) {
            leafs.add((LeafNode) node);
            return leafs;
        }
        leafs = getLeafs(leafs, node.left);
        leafs = getLeafs(leafs, node.right);
        return leafs;
    }

    public BSPPostorderIterator getPostOrderIterator() {
        LinkedList<Node> nodes = getNodesPostOrder(new LinkedList<Node>(), root);
        return new BSPPostorderIterator(nodes);
    }

    private LinkedList<Node> getNodesPostOrder(LinkedList<Node> nodes, Node node) {
        if (node == null)
            return nodes;
        nodes = getNodesPostOrder(nodes, node.left);
        nodes = getNodesPostOrder(nodes, node.right);
        nodes.add(node);
        return nodes;
    }
}
