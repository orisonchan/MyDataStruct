package my.struct.linkedlist;

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

  public MyConcurrentDoublyLinkedList() {
    head = new DNode(null);
    current = head;
  }

  @Override
  public boolean isEmpty() {
    return head.next == null;
  }

  @Override
  public T get(int index) {
    if (index + 1 > size) {
      return null;
    }
    DNode tmp = head;
    for (int i = 0; i < index; i++) {
      tmp = tmp.next;
    }
    return tmp.val;
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public void append(T t) {
    synchronized (this) {
      DNode node = new DNode(t);
      current.next = node;
      node.prev = current;
      current = node;
      size++;
    }
  }

  @Override
  public void add(int index, T t) {
    if (index >= size) {
      append(t);
    } else {
      synchronized (this) {
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
      }
    }
  }

  @Override
  public void remove(int index) {
    if (index >= size) {
      throw new IllegalArgumentException("Specific index is bigger than size!");
    } else {
      synchronized (this) {
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
      }
    }
  }

  @Override
  public void remove(T t) {
    synchronized (this) {
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
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    DNode node = head;
    builder.append("[");
    for (int i = 0; i < size; i++) {
      node = node.next;
      builder.append("{").append(node.val).append("}");
    }
    builder.append("]");
    return builder.toString();
  }
}
