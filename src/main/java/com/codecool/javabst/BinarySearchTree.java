package com.codecool.javabst;

import com.codecool.javabst.model.Node;


import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// Skeleton for the Binary search tree. Feel free to modify this class.

public class BinarySearchTree {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static int tier = 1;

    private Set<Node> tree = new HashSet<>();
    private Set<Node> unSettedNodes = new HashSet<>();


    //yeah dude
    public BinarySearchTree(List<Integer> elements) {
        // TODO construct a binary search tree here
        Collections.sort(elements);
        int middleIndex = elements.size() / 2;
        int rootNumber = elements.get(middleIndex);
        elements.remove(middleIndex);
        Node root = new Node(rootNumber);
        root.setRoot(true);
        elements.forEach(number -> unSettedNodes.add(new Node(number)));
        unSettedNodes.add(root);
        createTree();
        setRemaningLeaves();    // because the while loop terminate when the unSettedNodes size reduce to 0 -> better solution?
    }

    private void setRemaningLeaves() {
        List<Node> lastLeaves = tree.stream().filter(nodee -> nodee.getTier() == tier).collect(Collectors.toList());
        lastLeaves.forEach(leaf -> leaf.setLeaf(true));
    }


    private Node[] getChildNodes(Node node) {
        Node[] resultNode = new Node[2];
        resultNode[0] = getChild(node, true);
        resultNode[1] = getChild(node, false);
        return resultNode;
    }

    private Node getChild(Node node, boolean smaller) {
        List<Node> nodes;
        if (smaller) {
            if (node.getParent() != null) {
                nodes = getSmallSideBatch(node);
            } else {
                nodes = unSettedNodes.stream().filter(node1 -> node1.getNodeValue() < node.getNodeValue()).collect(Collectors.toList());
            }
            return getNode(nodes);
        } else {
            if (node.getParent() != null) {
                nodes = getHighSideBatch(node);
            } else {
                nodes = unSettedNodes.stream().filter(node2 -> node2.getNodeValue() > node.getNodeValue()).collect(Collectors.toList());
            }
            return getNode(nodes);
        }
    }

    private List<Node> getHighSideBatch(Node node) {
        int from = node.getNodeValue();
        int theHighestPossible;
        List<Node> resultNodes;
        while (true) {
            if (node.getParent() != null) {
                if (node.getParent().getNodeValue() > node.getNodeValue()) {
                    theHighestPossible = node.getParent().getNodeValue();
                    resultNodes = unSettedNodes
                            .stream()
                            .filter(node1 -> (node1.getNodeValue() > from
                                    && node1.getNodeValue() < theHighestPossible))
                            .collect(Collectors.toList());
                    break;
                } else {
                    node = node.getParent();
                }
            } else {
                resultNodes = unSettedNodes
                        .stream()
                        .filter(node2 -> node2.getNodeValue() > from)
                        .collect(Collectors.toList());
                break;
            }
        }
        return resultNodes;
    }

    private List<Node> getSmallSideBatch(Node node) {
        int from = node.getNodeValue();
        int theSmallestPossible;
        List<Node> resultNodes;
        while (true) {
            if (node.getParent() != null) {
                if (node.getParent().getNodeValue() < node.getNodeValue()) {
                    theSmallestPossible = node.getParent().getNodeValue();
                    resultNodes = unSettedNodes
                            .stream()
                            .filter(node1 -> (node1.getNodeValue() < from
                                    && node1.getNodeValue() > theSmallestPossible))
                            .collect(Collectors.toList());
                    break;
                } else {
                    node = node.getParent();
                }
            } else {
                resultNodes = unSettedNodes
                        .stream()
                        .filter(node2 -> node2.getNodeValue() < from)
                        .collect(Collectors.toList());
                break;
            }
        }
        return resultNodes;
    }


    private Node getNode(List<Node> nodes) {
        if (nodes.size() == 0) {
            return null;
        } else if (nodes.size() == 1) {
            return nodes.get(0);
        }
        nodes = sortNodesByValueIncremently(nodes);
        int middle = nodes.size() % 2 == 0 ? nodes.size() / 2 : nodes.size() / 2 + 1;
        return nodes.get(middle);
    }


    //bubble sort with nodes
    private List<Node> sortNodesByValueIncremently(List<Node> nodes) {
        int size = nodes.size();
        for (int i = 0; i < size - 1; i++)
            for (int j = 0; j < size - i - 1; j++)
                if (nodes.get(j).getNodeValue() > nodes.get(j + 1).getNodeValue()) {
                    Node temp = new Node(nodes.get(j).getNodeValue());
                    nodes.add(j, nodes.get(j + 1));
                    nodes.remove(j + 1);
                    nodes.add(j + 1, temp);
                    nodes.remove(j + 2);
                }
        return nodes;
    }

    private void createTree() {
        initTree();
        while (unSettedNodes.size() != 0) {
            List<Node> currentTierNodes = tree.stream().filter(nodee -> nodee.getTier() == tier).collect(Collectors.toList());
            for (Node node : currentTierNodes) {
                Node[] currentNodeChilds = getChildNodes(node);
                if (currentNodeChilds[0] != null) {
                    Node smallBranch = currentNodeChilds[0];
                    Node bigBranch = currentNodeChilds[1];
                    setNode(node, smallBranch, bigBranch);
                }
                if (currentNodeChilds[1] != null) {
                    Node bigBranch = currentNodeChilds[1];
                    Node smallBranch = currentNodeChilds[0];
                    setNode(node, bigBranch, smallBranch);
                }
                if (currentNodeChilds[0] == null && currentNodeChilds[1] == null) {
                    node.setLeaf(true);
                }
                node.setChildNodes(currentNodeChilds);
                unSettedNodes.remove(node);
            }
            tier++;
        }
    }

    private void setNode(Node node, Node sibling, Node otherSibling) {
        sibling.setSibling(otherSibling);
        sibling.setParent(node);
        sibling.setTier(tier + 1);
        tree.add(sibling);
        unSettedNodes.remove(sibling);
    }

    private void initTree() {
        Node root = unSettedNodes.stream().filter(node -> node.isRoot()).findFirst().orElse(null);
        Node[] rootChildren = getChildNodes(root);
        root.setChildNodes(rootChildren);
        rootChildren[0].setParent(root);
        rootChildren[0].setSibling(rootChildren[1]);

        rootChildren[1].setParent(root);
        rootChildren[1].setSibling(rootChildren[0]);
        rootChildren[0].setTier(tier);
        rootChildren[1].setTier(tier);

        unSettedNodes.remove(root);
        unSettedNodes.remove(rootChildren[0]);
        unSettedNodes.remove(rootChildren[1]);
        tree.addAll(Arrays.asList(root, rootChildren[0], rootChildren[1]));
    }

    public boolean search(Integer toFind) {
        // TODO return true if the element has been found, false if its not in the tree.
        Node root = tree.stream().filter(node -> node.isRoot()).findFirst().orElse(null);
        if (root.getNodeValue() == toFind) {
            LOGGER.info("SEARCH: value: " + toFind + "    ----> This is the root node");
            return true;
        }
        Node node = root;
        while (true) {
            if (checking(node, toFind)) {
                LOGGER.info("SEARCH: Found node with value = " + toFind + " in level " + node.getTier() + ". Parent=" + node.getParent().getNodeValue()
                        + " & leaf=" + node.isLeaf());
                return true;
            }
            node = getNewTarget(node, toFind);
            if (node == null) {
                LOGGER.info("SEARCH: Not found. Node with value: " + toFind + " is not on the tree");
                return false;
            }
        }
    }

    private Node getNewTarget(Node node, Integer toFind) {
        if (node.getChildNodes() == null) {
            return null;
        }
        return toFind > node.getNodeValue() ? node.getChildNodes()[1] : node.getChildNodes()[0];
    }

    private boolean checking(Node node, Integer toFind) {
        if (node == null) {
            return false;
        }
        return node.getNodeValue() == toFind;
    }


    public void add(Integer toAdd) {
        // TODO adds an element. Throws an error if it exist.

        Node node = tree.stream().filter(node1 -> node1.isRoot()).findFirst().orElse(null);
        while (true) {
            if (checking(node, toAdd)) {
                throw new NodeAddingException("Node is already on the tree");   // check if the new node is the toAdd
            }
            if (node.getNodeValue() > toAdd) {
                if (checking(node.getChildNodes()[0], toAdd)) {                //check if the small child is the toAdd
                    throw new NodeAddingException("Node is already on the tree");
                }// if the node small child is free it's insert the toAdd node
                if (node.getChildNodes()[0] == null) {
                    addNode(toAdd, node, true);
                    LOGGER.info("ADD: Node created and added to the tree with value: "+toAdd);
                    break;
                }
                node = node.getChildNodes()[0];
                continue;                 // because it assign the node to the small child and I want to check if this node is the toAdd
            }

            if (node.getNodeValue() < toAdd) {
                if (checking(node.getChildNodes()[1], toAdd)) {
                    throw new NodeAddingException("Node is already on the tree");
                }
                if (node.getChildNodes()[1] == null) {
                    addNode(toAdd, node, false);
                    LOGGER.info("ADD: Node created and added to the tree with value: "+toAdd);
                    break;
                }
                node = node.getChildNodes()[1];
            }
        }
    }

    private void addNode(Integer toAdd, Node node, boolean smallSide) {
        Node insertNode = new Node(toAdd);
        if (smallSide) {
            node.addSmallChild(insertNode);
        }
        node.addHighChild(insertNode);
        insertNode.setParent(node);
        insertNode.setLeaf(true);
        int index = smallSide ? 1 : 0;
        insertNode.setSibling(node.getChildNodes()[index]);
        if (node.getChildNodes()[index] != null) {
            node.getChildNodes()[index].setSibling(insertNode);
        }
        insertNode.setTier(node.getTier() + 1);
        tree.add(insertNode);
    }


    public void remove(Integer toRemove) {
        // TODO removes an element. Throws an error if its not on the tree.
        Node node = tree.stream().filter(node1 -> node1.isRoot()).findFirst().orElse(null);
        while(true){
            if(checking(node, toRemove)){           // if found it
                if(node.isLeaf()){                  // case node is a leaf
                    if(node.getParent().getNodeValue() > toRemove){    // case leaf is smaller than parent
                        deleteLeaf(node, true);
                        LOGGER.info("REMOVE: Remove complete with node value: " + toRemove);
                        break;
                    }
                    deleteLeaf(node, false);    // case leaf is higher then it's parent
                    LOGGER.info("REMOVE: Remove complete with node value: " + toRemove);
                    break;
                }
                if(nodeHasOnlyOneChild(node)){                    //  case node has only one child
                    if(node.getChildNodes()[0] != null){
                        if(node.getParent().getNodeValue() > toRemove){
                            node.getParent().getChildNodes()[0] = node.getChildNodes()[0];  // set node small child to parent small child
                            nodeHasOnlyOneNodeDelete(node, true);
                            LOGGER.info("REMOVE: Remove complete with node value: " + toRemove);
                            break;
                        }
                        node.getParent().getChildNodes()[1] = node.getChildNodes()[0];
                        nodeHasOnlyOneNodeDelete(node, true);
                        LOGGER.info("REMOVE: Remove complete with node value: " + toRemove);
                        break;
                    }
                    if(node.getParent().getNodeValue() > toRemove){                      // same things are happen with the higher child
                        node.getParent().getChildNodes()[0] = node.getChildNodes()[1];
                        nodeHasOnlyOneNodeDelete(node, false);
                        LOGGER.info("REMOVE: Remove complete with node value: " + toRemove);
                        break;
                    }
                    node.getParent().getChildNodes()[1] = node.getChildNodes()[1];
                    nodeHasOnlyOneNodeDelete(node, false);
                    LOGGER.info("REMOVE: Remove complete with node value: " + toRemove);
                    break;
                }
                Node successor = findSuccessor(node.getChildNodes()[1]);
                successing(node, successor);
                eraseNodeFromTheEnd(successor);
                tree.add(successor);
                tree.remove(node);
                LOGGER.info("REMOVE: Remove complete with node value: " + toRemove);
                break;
            }
            node = getNewTarget(node, toRemove);
            if(node == null){
                throw new RemoveFromTreeException("Node with value: "+toRemove + " is not on the tree");
            }
        }
    }

    private void successing(Node node, Node successor) {
        successor.setParent(node.getParent());
        successor.setTier(node.getTier());
        successor.setSibling(node.getSibling());
        successor.setChildNodes(node.getChildNodes());
        successor.setRoot(node.isRoot());
        if(node.getParent().getNodeValue() > node.getNodeValue()){
            successor.getParent().addSmallChild(successor);
        }
        successor.getParent().addHighChild(successor);
        successor.getChildNodes()[0].setParent(successor);
        successor.getChildNodes()[1].setParent(successor);
        if(successor.getSibling() != null){
            successor.getSibling().setSibling(successor);
        }
    }

    private Node findSuccessor(Node node) {
        while(node.getChildNodes()[0] != null){
            node = node.getChildNodes()[0];
        }
        return node;
    }

    private void eraseNodeFromTheEnd(Node node) {
        if(node.getSibling() != null){
            node.getSibling().setSibling(null);     // delete as sibling from it's sibling
        }
        node.getParent().getChildNodes()[0] = null;   // delete as small child from it's parent
        node.setLeaf(false);                           // node is not leaf anymore
    }

    private void nodeHasOnlyOneNodeDelete(Node node, boolean small) {
        int index = small ? 0 : 1;
        node.getChildNodes()[index].setParent(node.getParent());           // set parent to node child to node parent
        //replace parent child with a new one
        if(node.getParent().getNodeValue() > node.getNodeValue()){
            node.getParent().addSmallChild(node.getChildNodes()[index]);
        }
        else{
            node.getParent().addHighChild(node.getChildNodes()[index]);
        }
        node.getChildNodes()[index].setTier(node.getTier());      // setting node to level as was the removed one
        node.getChildNodes()[index].setRoot(node.isRoot());
        tree.remove(node);
    }

    private boolean nodeHasOnlyOneChild(Node node) {
        boolean isJustOneChild = false;
        if(node.getChildNodes()[0] == null && node.getChildNodes()[1] != null){
            isJustOneChild = true;
        }
        if(node.getChildNodes()[1] == null && node.getChildNodes()[0] != null){
            isJustOneChild= true;
        }
        return isJustOneChild;
    }

    private void deleteLeaf(Node node, boolean smaller) {
        int index = smaller ? 0 : 1;
        node.getParent().getChildNodes()[index] = null;    // set node parent  child to null
        if(node.getSibling() != null){                 // erase node it's sibling sibling status
            node.getSibling().setSibling(null);
        }
        tree.remove(node);     // delete node from the tree
    }
}
