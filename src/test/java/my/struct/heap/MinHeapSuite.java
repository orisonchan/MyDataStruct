package my.struct.heap;

import org.junit.Test;

public class MinHeapSuite {

  private MinHeap<Integer> minHeap;

  @Test
  public void testBasic() {
    minHeap = new MinHeap<>(new Integer[]{3, 5, 2, 1, 4});
    assert minHeap.pop() == 1;
    assert minHeap.pop() == 2;
    assert minHeap.pop() == 3;
    assert minHeap.pop() == 4;
    assert minHeap.pop() == 5;
  }

  @Test
  public void testAdd() {
    minHeap = new MinHeap<>(new Integer[]{5, 8, 7, 2, 3, 4});
    assert minHeap.pop() == 2;
    minHeap.add(1);
    minHeap.add(6);
    assert minHeap.pop() == 1;
    assert minHeap.pop() == 3;
    assert minHeap.pop() == 4;
  }

}
