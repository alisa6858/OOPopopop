package ru.ssau.tk.pepper.oopopopop.functions;

import ru.ssau.tk.pepper.oopopopop.exceptions.InterpolationException;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {
    Node head = null;

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {
        checkLengthIsTheSame(xValues, yValues);
        checkSorted(xValues);
        if (xValues.length < 2) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < xValues.length; ++i) {
            addNode(xValues[i], yValues[i]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 2) {
            throw new IllegalArgumentException();
        }
        if (xFrom > xTo) {
            double t = xFrom;
            xFrom = xTo;
            xTo = t;
        }
        double step = (xTo - xFrom) / (count - 1);
        for (int i = 0; i < count; ++i) {
            double x = xFrom + step * i;
            double y = source.apply(x);
            addNode(x, y);
        }
    }

    private void addNode(double x, double y) {
        Node node = new Node(x, y);
        if (head == null) {
            head = node.next = node.prev = node;
        } else {
            Node tail = head.prev;
            tail.link(node);
        }
        count += 1;
    }

    private Node getNode(int index) {
        // TODO: loop backward if index > count/2
        Node node = head;
        for (int i = 0; i < index; ++i) {
            node = node.next;
        }
        return node;
    }

    @Override
    protected int floorIndexOfX(double x) {
        if (count == 0) {
            throw new IllegalStateException();
        }
        if (x < leftBound()) {
            throw new IllegalArgumentException();
        }
        Node node = head.prev;
        for (int i = count - 1; i >= 0; --i) {
            if (x >= node.x) {
                return i;
            }
            node = node.prev;
        }
        throw new IllegalStateException(); // unreachable
    }

    private Node floorNodeOfX(double x) {
        if (count == 0) {
            throw new IllegalStateException();
        }
        if (x < leftBound()) {
            throw new IllegalArgumentException();
        }
        Node node = head.prev;
        for (int i = count - 1; i >= 0; --i) {
            if (x >= node.x) {
                return node;
            }
            node = node.prev;
        }
        throw new IllegalStateException(); // unreachable
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (count < 2) {
            throw new IllegalStateException();
        }
        return interpolate(x, head.x, head.next.x, head.y, head.next.y);
    }

    @Override
    protected double extrapolateRight(double x) {
        if (count < 2) {
            throw new IllegalStateException();
        }
        Node tail = head.prev;
        return interpolate(x, tail.prev.x, tail.x, tail.prev.y, tail.y);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (count < 2) {
            throw new IllegalStateException();
        }
        Node nodeLeft = getNode(floorIndex);
        Node nodeRight = nodeLeft.next;
        if (x < nodeLeft.x || x > nodeRight.x) {
            throw new InterpolationException();
        }
        return interpolate(x, nodeLeft.x, nodeRight.x, nodeLeft.y, nodeRight.y);
    }

    @Override
    public double getX(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IllegalArgumentException();
        }
        return getNode(index).x;
    }

    @Override
    public double getY(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IllegalArgumentException();
        }
        return getNode(index).y;
    }

    @Override
    public void setY(int index, double value) {
        if (index < 0 || index >= getCount()) {
            throw new IllegalArgumentException();
        }
        getNode(index).y = value;
    }

    @Override
    public int indexOfX(double x) {
        Node node = head;
        for (int i = 0; i < count; ++i) {
            if (node.x == x) {
                return i;
            }
            node = node.next;
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        Node node = head;
        for (int i = 0; i < count; ++i) {
            if (node.y == y) {
                return i;
            }
            node = node.next;
        }
        return -1;
    }

    @Override
    public double leftBound() {
        if (count == 0) {
            throw new IllegalStateException();
        }
        return head.x;
    }

    @Override
    public double rightBound() {
        if (count == 0) {
            throw new IllegalStateException();
        }
        return head.prev.x;
    }

    @Override
    public void insert(double x, double y) {
        int idx = indexOfX(x);
        if (idx != -1) {
            Node node = getNode(idx);
            node.y = y;
        } else {
            if (count == 0 || x > rightBound() || x < leftBound()) {
                boolean needRebase = count != 0 && x < leftBound();
                addNode(x, y);
                if (needRebase) {
                    head = head.prev;
                }
            } else {
                Node newNode = new Node(x, y);
                Node node = floorNodeOfX(x);
                node.link(newNode);
                count += 1;
            }
        }
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IllegalArgumentException();
        }
        Node node = getNode(index);
        if (node == head) {
            head = head.next;
        }
        node.unlink();
        --count;
        if (count == 0) {
            head = null;
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new Iterator<>() {
            Node node = head;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public Point next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Point p = new Point(node.x, node.y);
                node = node.next == head ? null : node.next;
                return p;
            }
        };
    }

    static class Node {
        double x;
        double y;

        Node next;

        Node prev;

        Node(double x, double y, Node next, Node prev) {
            this.x = x;
            this.y = y;
            this.next = next;
            this.prev = prev;
        }

        Node(double x, double y) {
            this(x, y, null, null);
        }

        void link(Node successor) {
            successor.next = next;
            successor.prev = this;
            next.prev = successor;
            next = successor;
        }

        void unlink() {
            next.prev = prev;
            prev.next = next;
        }
    }
}