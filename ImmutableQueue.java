package jp.co.worksap.global;

/**
 * Created by Emperor on 1/11/14.
 */

/**
 * Immutable means that after every operation on the object, a new operated object is returned while the original
 * object is untouched. An initial thought is that for every operation that may change the object, first make a copy
 * of the object, and then operate on the copied one. This scheme could work but with low efficiency both in terms of
 * time and space. One solution is to use shared structure.
 * For every modification operation, only the changed data part are stored in the new object, those unchanged parts
 * remain shared by both the new and the old objects. Take an example of binary tree:
 *   2
 *  / \
 * 1  3
 * If a new node with value 4 is inserted under node 3, then the states of node 3 and 2 are changed, node 1 is untouched.
 * So the new tree need to store new version of node 2 and 3 without touching node 1. This is useful when the subtree
 * root at node 1 is very large.
 * In this immutable queue problem, we can maintain a storage structure composed of two linked list, one is used for
 * enqueue and one dequeue. After each enqueue operation, only the added node and the reference to the old enqueue
 * storage is stored in the new object, the old dequeue storage is unchanged and the new object simply holds its reference.
 * For dequeue operation, the mechanism is the same.
 */
import java.util.NoSuchElementException;

/**
 * The Queue class represents an immutable first-in-first-out (FIFO) queue of objects.
 *
 * @param <E> the item type
 */
public class ImmutableQueue<E> {

    private InternalStorage<E> storageForEnqueue;
    private InternalStorage<E> storageForDequeue;

    /**
     * requires default constructor.
     */
    public ImmutableQueue() {
        // modify this constructor if necessary, but do not remove default constructor
        storageForEnqueue = new InternalStorage<E>();
        storageForDequeue = new InternalStorage<E>();

    }

    public ImmutableQueue(InternalStorage<E> enq, InternalStorage<E> deq) {
        storageForEnqueue = enq;
        storageForDequeue = deq;
    }
    // add other constructors if necessary

    public static void main(String[] args) {
        ImmutableQueue<Integer> queue = new ImmutableQueue<Integer>();
        System.out.println(queue);
        ImmutableQueue<Integer> one = queue.enqueue(1);
        System.out.println(queue);
        System.out.println(one);
        ImmutableQueue<Integer> two = queue.enqueue(2);
        ImmutableQueue<Integer> three = one.enqueue(2);
        System.out.println(queue);
        System.out.println(one);
        System.out.println(two);
        System.out.println(three.enqueue(3).enqueue(4));
        System.out.println(three.peek());
        System.out.println(three.dequeue().dequeue().enqueue(44));
    }

    /**
     * Returns the queue that adds an item into the tail of this queue without modifying this queue.
     * <pre>
     * e.g.
     * When this queue represents the queue (2, 1, 2, 2, 6) and we enqueue the value 4 into this queue,
     * this method returns a new queue (2, 1, 2, 2, 6, 4)
     * and this object still represents the queue (2, 1, 2, 2, 6).
     * </pre>
     * If the element e is null, throws IllegalArgumentException.
     *
     * @param e
     * @return ImmutableQueue<E>
     * @throws IllegalArgumentException
     */
    public ImmutableQueue<E> enqueue(E e) {
        if (e == null)
            throw new IllegalArgumentException();

        return new ImmutableQueue<E>(storageForEnqueue.push(e), storageForDequeue);
    }

    /**
     * Returns the queue that removes the object at the head of this queue without modifying this queue.
     * <pre>
     * e.g.
     * When this queue represents the queue (7, 1, 3, 3, 5, 1) ,
     * this method returns a new queue (1, 3, 3, 5, 1)
     * and this object still represents the queue (7, 1, 3, 3, 5, 1) .
     * </pre>
     * If this queue is empty, throws java.util.NoSuchElementException.
     *
     * @return ImmutableQueue<E>
     * @throws java.util.NoSuchElementException
     */
    public ImmutableQueue<E> dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (!storageForDequeue.isEmpty()) {
            return new ImmutableQueue<E>(storageForEnqueue, storageForDequeue.rest);
        } else {
            return new ImmutableQueue<E>(new InternalStorage<E>(),
                    storageForEnqueue.reversed().rest);
        }
    }

    /**
     * Looks at the object which is the head of this queue without removing it from the queue.
     * <pre>
     * e.g.
     * When this queue represents the queue (7, 1, 3, 3, 5, 1),
     * this method returns 7 and this object still represents the queue (7, 1, 3, 3, 5, 1)
     * </pre>
     * If the queue is empty, throws java.util.NoSuchElementException.
     *
     * @return element E
     * @throws java.util.NoSuchElementException
     */
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (storageForDequeue.isEmpty()) {
            storageForDequeue = storageForEnqueue.reversed();
            storageForEnqueue = new InternalStorage<E>();
        }
        return storageForDequeue.head;
    }

    /**
     * Returns the number of objects in this queue.
     *
     * @return int
     */
    public int size() {
        boolean enqNull = (storageForEnqueue == null);
        boolean deqNull = (storageForDequeue == null);
        if (enqNull && deqNull) {
            return 0;
        } else if (enqNull) {
            return storageForDequeue.size;
        } else if (deqNull) {
            return storageForEnqueue.size;
        } else
            return storageForDequeue.size + storageForEnqueue.size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public String toString() {
        String string = "Queue: ";
        if (isEmpty()) {
            return "Empty Queue";
        }
        ImmutableQueue<E> cur = this;
        while (!cur.isEmpty()) {
            string += cur.peek();
            string += " ";
            cur = cur.dequeue();
        }
        return string;
    }

    /**
     * The InternalStorage class is the backbone of the ImmutableQueue.
     * The storage is also immutable. It provide necessary abstraction of the shared structure implementation.
     * @param <E>
     */
    private static class InternalStorage<E> {
        private E head = null;
        private InternalStorage<E> rest = null;
        private int size = 0;

        public InternalStorage(E head, InternalStorage<E> rest) {
            this.head = head;
            this.rest = rest;
            this.size = rest.size + 1;
        }

        public InternalStorage() {
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public InternalStorage<E> push(E item) {
            return new InternalStorage<E>(item, this);
        }


        /**
         * When a dequeue operation is requested but the storage for dequeue is empty,
         * then we need to move all the items in the storage for enqueue into dequeue's storage reversely.
         * @return reversed version of the storage for enqueue.
         */
        public InternalStorage<E> reversed() {
            InternalStorage<E> reverse = new InternalStorage<E>();
            InternalStorage<E> cur = this;
            while (!cur.isEmpty()) {
                reverse = reverse.push(cur.head);
                cur = cur.rest;
            }
            return reverse;
        }
    }
}
