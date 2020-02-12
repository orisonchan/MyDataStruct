package my.struct.heap;

public class MaxHeap<T extends Comparable<T>> extends AbstractHeap<T> {

  public MaxHeap(int capacity) {
    super(capacity);
  }

  public MaxHeap() {
  }

  public MaxHeap(T[] array) {
    super(array);
  }

  @Override
  void shiftUp(int index) {
    while (index > 0 && get(parentIndex(index)).compareTo(get(index)) < 0) {
      swap(index, parentIndex(index));
      index = parentIndex(index);
    }
  }

  @Override
  void shiftDown(int index) {
    // leftChildIndex(index) < getSize() means left child exists
    while (leftChildIndex(index) < getSize()) {
      int leftChildIndex = leftChildIndex(index);
      int maxIndex = leftChildIndex;
      T leftChild = get(leftChildIndex);
      if (leftChildIndex + 1 < getSize() && leftChild.compareTo(get(leftChildIndex + 1)) < 0) {
        maxIndex = leftChildIndex + 1;
      }
      if (get(index).compareTo(get(maxIndex)) >= 0) {
        break;
      }
      swap(index, maxIndex);
      index = maxIndex;
    }
  }
}
