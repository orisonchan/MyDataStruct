package my.struct.linkedlist;

public interface MyConcurrentLinkedList<T> {

  int size = 0;

  boolean isEmpty();

  T get(int index);

  int getSize();

  /**
   * append to last
   */
  void append(T t);

  /**
   * add into a specific index
   */
  void add(int index, T t);

  void remove(int index);

  void remove(T t);

}
