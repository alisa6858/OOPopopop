package ru.ssau.tk.pepper.oopopopop.functions;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {
    Node head = null;

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException();
        }
        if (xValues.length == 0) {
            throw new IllegalArgumentException();
        }
        if (!isSorted(xValues)) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < xValues.length; ++i) {
            addNode(xValues[i], yValues[i]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 1) {
            throw new IllegalArgumentException();
        }
        if (xFrom > xTo) {
            double t = xFrom;
            xFrom = xTo;
            xTo = t;
        }
        if (count == 1) {
            addNode(xFrom, source.apply(xFrom));
        } else {
            double step = (xTo - xFrom) / (count - 1);
            for (int i = 0; i < count; ++i) {
                double x = xFrom + step * i;
                double y = source.apply(x);
                addNode(x, y);
            }
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
        Node node = head;
        for (int i = 0; i < count; ++i) {
            if (node.x < x) {
                return i;
            }
            node = node.next;
        }
        return count;
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (getCount() == 1) {
            return head.y;
        }
        return interpolate(x, head.x, head.next.x, head.y, head.next.y);
    }

    @Override
    protected double extrapolateRight(double x) {
        if (getCount() == 1) {
            return head.y;
        }
        Node tail = head.prev;
        return interpolate(x, tail.prev.x, tail.x, tail.prev.y, tail.y);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (getCount() == 1) {
            return head.y;
        }
        Node nodeLeft = getNode(floorIndex);
        Node nodeRight = nodeLeft.next;
        return interpolate(x, nodeLeft.x, nodeRight.x, nodeLeft.y, nodeRight.y);
    }

    @Override
    public double getX(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException();
        }
        return getNode(index).x;
    }

    @Override
    public double getY(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException();
        }
        return getNode(index).y;
    }

    @Override
    public void setY(int index, double value) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException();
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
        return head.x;
    }

    @Override
    public double rightBound() {
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
                idx = floorIndexOfX(x);
                Node newNode = new Node(x, y);
                Node node = getNode(idx);
                node.link(newNode);
                count += 1;
            }
        }
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException();
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
}

class Node {
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