package my.struct.heap;

public class MinHeap<T extends Comparable<T>> extends AbstractHeap<T> {

  public MinHeap(int capacity) {
    super(capacity);
  }

  public MinHeap() {
  }

  public MinHeap(T[] array) {
    super(array);
  }
  
  @Override
  void shiftUp(int index) {
    while (index > 0 && get(index).compareTo(get(parentIndex(index))) < 0) {
      swap(index, parentIndex(index));
      index = parentIndex(index);
    }
  }
  
  @Override
  void shiftDown(int index) {
    while (leftChildIndex(index) < getSize()) {
      int leftChildIndex = leftChildIndex(index);
      T leftChild = get(leftChildIndex);
      int minIndex = leftChildIndex;
      if (leftChildIndex + 1 < getSize() && get(leftChildIndex + 1).compareTo(leftChild) < 0) {
        minIndex = leftChildIndex + 1;
      }
      if (get(index).compareTo(get(minIndex)) <= 0) {
        break;
      }
      swap(index, minIndex);
      index = minIndex;
    }
  }
}