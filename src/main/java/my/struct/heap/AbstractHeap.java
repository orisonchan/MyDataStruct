package my.struct.heap;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Orison Chan
 */
public abstract class AbstractHeap<T extends Comparable<T>> {

  private ArrayList<T> data;

  protected AbstractHeap(int capacity) {
    data = new ArrayList<>(capacity);
  }

  protected AbstractHeap() {
    data = new ArrayList<>();
  }

  protected AbstractHeap(T[] array) {
    data = new ArrayList<>(Arrays.asList(array));
    for (int i = parentIndex(data.size() - 1); i >= 0; i--) {
      shiftDown(i);
    }
  }

  public void add(T e) {
    data.add(e);
    shiftUp(data.size() - 1);
  }

  public T pop() {
    if (data.size() == 0) {
      throw new IllegalArgumentException("Can not pop when heap is empty");
    }
    T e = data.get(0);
    swap(0, data.size() - 1);
    data.remove(data.size() - 1);
    shiftDown(0);
    return e;
  }

  public boolean isEmpty() {
    return data.isEmpty();
  }

  /**
   * return size in heap
   *
   * @return size
   */
  public int getSize() {
    return data.size();
  }

  protected int parentIndex(int index) {
    if (index == 0) {
      throw new IllegalArgumentException("index-0 doesn't have parent");
    }
    return (index - 1) / 2;
  }

  protected int leftChildIndex(int index) {
    return index * 2 + 1;
  }

  protected int rightChildIndex(int index) {
    return index * 2 + 2;
  }

  protected void swap(int i, int j) {
    T t = data.get(i);
    data.set(i, data.get(j));
    data.set(j, t);
  }

  protected T get(int index) {
    return data.get(index);
  }

  abstract void shiftUp(int index);

  abstract void shiftDown(int index);

  @Override
  public String toString() {
    return "Heap{" +
            "data=" + data +
            '}';
  }
}
