package com.codecool.javabst.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Component
public class Node {

    private boolean root = false;
    private int nodeValue;
    private int tier;

    /**
     * The node in index 0, will be the node with lower value
     * The node in index 1, will be the node with higher value
     */
    private Node[] childNodes = new Node[2];
    private Node parent;
    private Node sibling;
    private boolean leaf = false;

    public Node(int nodeValue) {
        this.nodeValue = nodeValue;
    }

    public boolean isRoot() {
        return root;
    }

    public void setNodeValue(int nodeValue) {
        this.nodeValue = nodeValue;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public Node[] getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(Node[] childNodes) {
        this.childNodes = childNodes;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getSibling() {
        return sibling;
    }

    public void addSmallChild(Node node){
        this.childNodes[0] = node;
    }

    public void addHighChild(Node node){
        this.childNodes[1] = node;
    }

    public void setSibling(Node sibling) {
        this.sibling = sibling;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public int getNodeValue() {
        return this.nodeValue;
    }

    public void setRoot(boolean b) {
        this.root = b;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            if (((Node) o).getNodeValue() == this.getNodeValue()) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeValue);
    }
}
