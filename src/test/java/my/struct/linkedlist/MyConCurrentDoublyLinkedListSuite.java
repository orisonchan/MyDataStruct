package my.struct.linkedlist;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class MyConCurrentDoublyLinkedListSuite {

  private class Person {
    private int id;
    private String name;

    private Person(int id, String name) {
      this.id = id;
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Person person = (Person) o;
      return id == person.id &&
              Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, name);
    }

    @Override
    public String toString() {
      return id + ", " + name;
    }
  }

  @Test
  public void testCreateDLinkedList() {
    MyConcurrentLinkedList<Person> list = new MyConcurrentDoublyLinkedList<>();
    list.append(new Person(1, "zhangsan"));
    assert (list.getSize() == 1);
  }

  @Test
  public void testAddSpecificIndex() {
    MyConcurrentLinkedList<Person> list = new MyConcurrentDoublyLinkedList<>();
    list.append(new Person(1, "zhangsan"));
    list.append(new Person(2, "lisi"));
    list.append(new Person(3, "wangwu"));
    assert (list.toString().equals("[{1, zhangsan}{2, lisi}{3, wangwu}]"));
    list.add(0, new Person(4, "lily"));
    assert (list.toString().equals("[{4, lily}{1, zhangsan}{2, lisi}{3, wangwu}]"));
    list.add(2, new Person(5, "alice"));
    assert (list.toString().equals("[{4, lily}{1, zhangsan}{5, alice}{2, lisi}{3, wangwu}]"));
    list.add(2, new Person(6, "emily"));
    assert (list.toString().equals("[{4, lily}{1, zhangsan}{6, emily}{5, alice}{2, lisi}{3, wangwu}]"));
    list.add(4, new Person(7, "coco"));
    assert (list.toString().equals("[{4, lily}{1, zhangsan}{6, emily}{5, alice}{7, coco}{2, lisi}{3, wangwu}]"));
  }

  @Test
  public void testRemoveSpecificIndex() {
    MyConcurrentLinkedList<Person> list = new MyConcurrentDoublyLinkedList<>();
    list.append(new Person(1, "zhangsan"));
    list.append(new Person(2, "lisi"));
    list.append(new Person(3, "wangwu"));
    list.append(new Person(4, "lily"));
    list.append(new Person(5, "alice"));
    list.append(new Person(6, "emily"));
    list.append(new Person(7, "coco"));

    list.remove(0);
    assert (list.toString().equals("[{2, lisi}{3, wangwu}{4, lily}{5, alice}{6, emily}{7, coco}]"));
    list.remove(3);
    assert (list.toString().equals("[{2, lisi}{3, wangwu}{4, lily}{6, emily}{7, coco}]"));
    list.remove(4);
    assert (list.toString().equals("[{2, lisi}{3, wangwu}{4, lily}{6, emily}]"));
    list.append(new Person(8, "abcd"));
    assert (list.toString().equals("[{2, lisi}{3, wangwu}{4, lily}{6, emily}{8, abcd}]"));
  }

  @Test
  public void testRemoveObject() {
    MyConcurrentLinkedList<Person> list = new MyConcurrentDoublyLinkedList<>();
    list.append(new Person(1, "zhangsan"));
    list.append(new Person(2, "lisi"));
    list.append(new Person(3, "wangwu"));
    list.append(new Person(3, "wangwu"));
    list.append(new Person(3, "wangwu"));
    list.append(new Person(4, "lily"));
    list.append(new Person(5, "alice"));
    list.append(new Person(6, "emily"));
    list.append(new Person(7, "coco"));
    list.append(new Person(7, "coco"));

    list.remove(new Person(1, "zhangsan"));
    list.remove(new Person(3, "wangwu"));
    assert (list.toString().equals("[{2, lisi}{4, lily}{5, alice}{6, emily}{7, coco}{7, coco}]"));
    list.remove(new Person(7, "coco"));
    assert (list.toString().equals("[{2, lisi}{4, lily}{5, alice}{6, emily}]"));
    list.append(new Person(8, "abcd"));
    assert (list.toString().equals("[{2, lisi}{4, lily}{5, alice}{6, emily}{8, abcd}]"));
  }

  @Test
  public void testConcurrentAppend() throws InterruptedException, ExecutionException {
    MyConcurrentLinkedList<Person> list = new MyConcurrentDoublyLinkedList<>();
    ExecutorService pool = Executors.newFixedThreadPool(5);
    List<Future<Integer>> futureList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      final int finalI = i;
      futureList.add(pool.submit(() -> {
        list.append(new Person(finalI, finalI + ""));
        return finalI;
      }));
    }
    for (Future<Integer> future : futureList) {
      future.get();
    }
    assert (list.getSize() == 5);
  }

  @Test
  public void testConcurrentRemove() throws ExecutionException, InterruptedException {
    MyConcurrentLinkedList<Person> list = new MyConcurrentDoublyLinkedList<>();
    list.append(new Person(1, "zhangsan"));
    list.append(new Person(2, "lisi"));
    list.append(new Person(4, "lily"));
    list.append(new Person(3, "wangwu"));
    list.append(new Person(3, "wangwu"));
    list.append(new Person(3, "wangwu"));
    list.append(new Person(4, "lily"));
    list.append(new Person(5, "alice"));
    list.append(new Person(6, "emily"));
    list.append(new Person(7, "coco"));
    list.append(new Person(7, "coco"));

    Callable<Integer> callable1 = () -> {
      list.remove(new Person(3, "wangwu"));
      return 3;
    };
    Callable<Integer> callable2 = () -> {
      list.remove(new Person(7, "coco"));
      return 7;
    };
    Callable<Integer> callable3 = () -> {
      list.remove(new Person(4, "lily"));
      return 4;
    };
    ExecutorService pool = Executors.newFixedThreadPool(3);
    List<Future<Integer>> futureList = new ArrayList<>();
    futureList.add(pool.submit(callable1));
    futureList.add(pool.submit(callable2));
    futureList.add(pool.submit(callable3));
    for (Future<Integer> future : futureList) {
      future.get();
    }
    assert (list.getSize() == 4);
  }

}
