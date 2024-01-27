package examples.regression;

import java.util.*;

public class LinkedList<T> {
  private Node<T> root = null;

  public T get(int targetPosition) {
    int position = 0;
    Node<T> currentNode = root;
    while (currentNode != null) {
      position++;
      if (position == targetPosition - 1) {
        return currentNode.getObject();
      }
      currentNode = currentNode.getNext();
    }
    throw new IllegalArgumentException("No such element");
  }

  public void add(T object) {
    if (root == null) {
      root = new Node<T>(object);
    } else {
      Node<T> currentNode = root;
      while (currentNode.getNext() != null) {
        currentNode = currentNode.getNext();
      }
      currentNode.setNext(new Node<T>(object));
    }
  }

  public T getFirst() {
    if (root == null) {
      throw new IllegalArgumentException("Empty list");
    }
    return root.getObject();
  }

  public Iterator<T> iterator() {
    return new LinkedListIterator<T>(root);
  }

  public boolean isEmpty() {
    return root == null;
  }

  private class LinkedListIterator<T> implements Iterator<T> {

    private Node<T> nextNode;

    public LinkedListIterator(Node<T> head) {
      nextNode = head;
    }

    public boolean hasNext() {
      return nextNode != null;
    }

    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      T object = nextNode.getObject();
      nextNode = nextNode.getNext();
      return object;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private class Node<T> {

    private Node next = null;

    private T object = null;

    public Node(T object) {
      this.object = object;
    }

    public Node<T> getNext() {
      return next;
    }

    public void setNext(Node<T> next) {
      this.next = next;
    }

    public T getObject() {
      return object;
    }
  }
}
