package my.struct.heap;

import org.junit.Test;

public class MaxHeapSuite {

  private MaxHeap<Integer> maxHeap;

  @Test
  public void testBasic() {
    maxHeap = new MaxHeap<>(new Integer[]{3, 5, 2, 1, 4});
    assert maxHeap.pop() == 5;
    assert maxHeap.pop() == 4;
    assert maxHeap.pop() == 3;
    assert maxHeap.pop() == 2;
    assert maxHeap.pop() == 1;
  }

  @Test
  public void testAdd() {
    maxHeap = new MaxHeap<>(new Integer[]{3, 8, 6, 2, 1, 4});
    assert maxHeap.pop() == 8;
    maxHeap.add(5);
    maxHeap.add(7);
    assert maxHeap.pop() == 7;
    assert maxHeap.pop() == 6;
    assert maxHeap.pop() == 5;
  }

}
