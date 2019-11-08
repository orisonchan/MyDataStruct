package my.struct.linkedlist;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Orison Chan
 */
public class MyConcurrentDoublyLinkedList<T> implements MyConcurrentLinkedList<T> {

  private class DNode {
    T val;
    DNode prev;
    DNode next;

    private DNode(T t) {
      this.val = t;
    }

    @Override
    public String toString() {
      return val.toString();
    }
  }

  private DNode head;

  private volatile DNode current;

  private volatile int size;

  private ReentrantReadWriteLock lock;

  public MyConcurrentDoublyLinkedList() {
    head = new DNode(null);
    current = head;
    lock = new ReentrantReadWriteLock();
  }

  @Override
  public boolean isEmpty() {
    lock.readLock().lock();
    boolean b = head.next == null;
    lock.readLock().unlock();
    return b;
  }

  @Override
  public T get(int index) {
    lock.readLock().lock();
    if (index + 1 > size) {
      lock.readLock().unlock();
      return null;
    }
    DNode tmp = head;
    for (int i = 0; i < index; i++) {
      tmp = tmp.next;
    }
    lock.readLock().unlock();
    return tmp.val;
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public void append(T t) {
    lock.writeLock().lock();
    DNode node = new DNode(t);
    current.next = node;
    node.prev = current;
    current = node;
    size++;
    lock.writeLock().unlock();
  }

  @Override
  public void add(int index, T t) {
    if (index >= size) {
      append(t);
    } else {
      lock.writeLock().lock();
      if (index < size / 2) {
        // index close to left side
        DNode pointer = head;
        for (int i = 0; i < index; i++) {
          pointer = pointer.next;
        }
        DNode node = new DNode(t);
        node.next = pointer.next;
        pointer.next.prev = node;
        pointer.next = node;
        node.prev = pointer;
      } else {
        // index close to right side
        DNode pointer = current;
        for (int i = 0; i < size - index; i++) {
          pointer = pointer.prev;
        }
        DNode node = new DNode(t);
        node.next = pointer.next;
        pointer.next.prev = node;
        pointer.next = node;
        node.prev = pointer;
      }
      size++;
      lock.writeLock().unlock();
    }
  }

  @Override
  public void remove(int index) {
    if (index >= size) {
      throw new IllegalArgumentException("Specific index is bigger than size!");
    } else {
      lock.writeLock().lock();
      DNode pointer;
      if (index < size / 2) {
        // index close to left side
        pointer = head;
        for (int i = 0; i < index; i++) {
          pointer = pointer.next;
        }
      } else {
        // index close to right side
        pointer = current;
        for (int i = 0; i < size - index; i++) {
          pointer = pointer.prev;
        }
      }
      if (pointer.next.next == null) {
        // means remove `current`
        // current will back to prev
        current = pointer;
      } else {
        pointer.next.next.prev = pointer;
      }
      pointer.next = pointer.next.next;
      size--;
      lock.writeLock().unlock();
    }
  }

  @Override
  public void remove(T t) {
    lock.writeLock().lock();
    DNode pointer = head;
    for (int i = 0; i < size; i++) {
      if (pointer.next.val.equals(t)) {
        if (pointer.next.next == null) {
          // means remove `current`
          // current will back to prev
          current = pointer;
        } else {
          pointer.next.next.prev = pointer;
        }
        pointer.next = pointer.next.next;
        size--;
        i--;
      } else {
        pointer = pointer.next;
      }
    }
    lock.writeLock().unlock();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    lock.readLock().lock();
    DNode node = head;
    builder.append("[");
    for (int i = 0; i < size; i++) {
      node = node.next;
      builder.append("{").append(node.val).append("}");
    }
    lock.readLock().unlock();
    builder.append("]");
    return builder.toString();
  }
}
