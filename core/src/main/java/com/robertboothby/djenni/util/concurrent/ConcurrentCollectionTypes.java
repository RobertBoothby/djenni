package com.robertboothby.djenni.util.concurrent;

import com.robertboothby.djenni.util.CollectionType;

import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TransferQueue;

/**
 * This class contains static factory methods for creating CollectionTypes for all collections in the java.util.concurrent
 * package.
 */
public class ConcurrentCollectionTypes {

    /**
     * Create a CollectionTypeInstance for BlockingDeque based on an underlying CollectionType that is an implementation
     * of BlockingDeque.
     * @param underlying The underlying BlockingDeque based CollectionType.
     * @param <T> The type of the BlockingDeque Collection implementation.
     * @param <U> The type of the values to be carried in the BlockingDeque.
     * @return A CollectionType instance for BlockingDeque that will actually be the undertlying CollectionType.
     */
    @SuppressWarnings("unchecked")
    public static <T extends BlockingDeque<U>, U> CollectionType<BlockingDeque<U>, U> asBlockingDeque(CollectionType<T,U> underlying){
        return (CollectionType<BlockingDeque<U>, U>) underlying;
    }

    /**
     * Create a CollectionTypeInstance for BlockingQueue based on an underlying CollectionType that is an implementation
     * of BlockingQueue.
     * @param underlying The underlying BlockingQueue based CollectionType.
     * @param <T> The type of the underlying BlockingQueue implementation.
     * @param <U> The type of the values to be carried in the BlockingQueue.
     * @return A CollectionType instance for BlockingQueue that will actually be the underlying CollectionType.
     */
    @SuppressWarnings("unchecked")
    public static <T extends BlockingQueue<U>, U> CollectionType<BlockingQueue<U>, U> asBlockingQueue(CollectionType<T,U> underlying){
        return (CollectionType<BlockingQueue<U>, U>) underlying;
    }

    /**
     * Create a CollectionTypeInstance for TransferQueue based on an underlying CollectionType that is an implementation
     * of TransferQueue.
     * @param underlying The underlying TransferQueue based CollectionType.
     * @param <T> The type of the underlying TransferQueue implementation.
     * @param <U> The type of the values to be carried in the TransferQueue.
     * @return A CollectionType instance for TransferQueue that will actually be the underlying CollectionType.
     */
    @SuppressWarnings("unchecked")
    public static <T extends TransferQueue<U>, U> CollectionType<TransferQueue<U>, U> asTransferQueue(CollectionType<T,U> underlying){
        return (CollectionType<TransferQueue<U>, U>) underlying;
    }

    /**
     * Create a CollectionType instance for ArrayBlockingQueue. The order of values in an ArrayBlockingQueue created
     * using it will reflect their order in the input value list, the capacity will be defined by the size of the value
     * list..
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ArrayBlockingQueue from the passed in values.
     */
    public static <T> CollectionType<ArrayBlockingQueue<? extends T>, T> arrayBlockingQueue(){
        return values -> {
            ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<>(values.size());
            queue.addAll(values);
            return queue;
        };
    }

    /**
     * Create a CollectionType instance for ArrayBlockingQueue. The order of values in an ArrayBlockingQueue created
     * using it will reflect their order in the input value list, the capacity will be defined by the size of the value
     * list..
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ArrayBlockingQueue from the passed in values.
     */
    public static <T> CollectionType<ArrayBlockingQueue<? extends T>, T> arrayBlockingQueue(Class<T> tClass){
        return arrayBlockingQueue();
    }

    /**
     * Create a CollectionType instance for ArrayBlockingQueue. The order of values in an ArrayBlockingQueue created
     * using it will reflect their order in the input value list.
     * <p>
     * Please note that an IllegalArgumentException will be thrown if the value list size is greater than the capacity
     * being created.
     * @param capacity The capacity to use when creating the ArrayBlockingQueue
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ArrayBlockingQueue from the passed in values.
     */
    public static <T> CollectionType<ArrayBlockingQueue<? extends T>, T> arrayBlockingQueue(int capacity){
        return values -> {
            if(values.size() > capacity){
                throw new IllegalArgumentException("The number of values being used to create the ArrayBlockingQueue was greater than its capacity");
            }
            ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<>(values.size());
            queue.addAll(values);
            return queue;
        };
    }

    /**
     * Create a CollectionType instance for ArrayBlockingQueue. The order of values in an ArrayBlockingQueue created
     * using it will reflect their order in the input value list.
     * <p>
     * Please note that an IllegalArgumentException will be thrown if the value list size is greater than the capacity
     * being created.
     * @param capacity The capacity to use when creating the ArrayBlockingQueue
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ArrayBlockingQueue from the passed in values.
     */
    public static <T> CollectionType<ArrayBlockingQueue<? extends T>, T> arrayBlockingQueue(int capacity, Class<T> tClass){
        return arrayBlockingQueue(capacity);
    }

    /**
     * Create a CollectionType instance for ArrayBlockingQueue. The order of values in an ArrayBlockingQueue created
     * using it will reflect their order in the input value list.
     * <p>
     * Please note that an IllegalArgumentException will be thrown if the value list size is greater than the capacity
     * being created.
     * @param capacity The capacity to use when creating the ArrayBlockingQueue
     * @param fair Whether the ArrayBlockingQueue is fair.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ArrayBlockingQueue from the passed in values.
     */
    public static <T> CollectionType<ArrayBlockingQueue<? extends T>, T> arrayBlockingQueue(int capacity, boolean fair){
        return values -> {
            if(values.size() > capacity){
                throw new IllegalArgumentException("The number of values being used to create the ArrayBlockingQueue was greater than its capacity");
            }
            return new ArrayBlockingQueue<>(values.size(), fair, values);
        };
    }

    /**
     * Create a CollectionType instance for ArrayBlockingQueue. The order of values in an ArrayBlockingQueue created
     * using it will reflect their order in the input value list.
     * <p>
     * Please note that an IllegalArgumentException will be thrown if the value list size is greater than the capacity
     * being created.
     * @param capacity The capacity to use when creating the ArrayBlockingQueue
     * @param fair Whether the ArrayBlockingQueue is fair.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ArrayBlockingQueue from the passed in values.
     */
    public static <T> CollectionType<ArrayBlockingQueue<? extends T>, T> arrayBlockingQueue(int capacity, boolean fair, Class<T> tClass){
        return arrayBlockingQueue(capacity, fair);
    }

    /**
     * Create a CollectionType instance for ConcurrentLinkedDeque. The order of values in a ConcurrentLinkedDeque created
     * using it will reflect their order in the input value list.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentLinkedDeque from the passed in values.
     */
    public static <T> CollectionType<ConcurrentLinkedDeque<? extends T>, T> concurrentLinkedDeque(){
        return ConcurrentLinkedDeque::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentLinkedDeque. The order of values in a ConcurrentLinkedDeque created
     * using it will reflect their order in the input value list.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentLinkedDeque from the passed in values.
     */
    public static <T> CollectionType<ConcurrentLinkedDeque<? extends T>, T> concurrentLinkedDeque(Class<T> tClass){
        return ConcurrentLinkedDeque::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentLinkedQueue. The order of values in a ConcurrentLinkedQueue created
     * using it will reflect their order in the input value list.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentLinkedQueue from the passed in values.
     */
    public static <T> CollectionType<ConcurrentLinkedQueue<? extends T>, T> concurrentLinkedQueue(){
        return ConcurrentLinkedQueue::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentLinkedQueue. The order of values in a ConcurrentLinkedQueue created
     * using it will reflect their order in the input value list.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentLinkedQueue from the passed in values.
     */
    public static <T> CollectionType<ConcurrentLinkedQueue<? extends T>, T> concurrentLinkedQueue(Class<T> tClass){
        return ConcurrentLinkedQueue::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentSkipListSet.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentSkipListSet from the passed in values.
     */
    public static <T extends Comparable> CollectionType<ConcurrentSkipListSet<? extends T>, T> concurrentSkipListSet(){
        return ConcurrentSkipListSet::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentSkipListSet.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentSkipListSet from the passed in values.
     */
    public static <T extends Comparable> CollectionType<ConcurrentSkipListSet<? extends T>, T> concurrentSkipListSet(Class<T> tClass){
        return ConcurrentSkipListSet::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentSkipListSet.
     * @param comparator the comparator used in the ConcurrentSkipListSet.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentSkipListSet from the passed in values.
     * @todo think about comparators of super types.
     */
    public static <T> CollectionType<ConcurrentSkipListSet<? extends T>, T> concurrentSkipListSet(Comparator<T> comparator){
        return values -> new ConcurrentSkipListSet<>(comparator);
    }

    /**
     * Create a CollectionType instance for CopyOnWriteArrayList. The order of values in a CopyOnWriteArrayList created
     *      * using it will reflect their order in the input value list.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an CopyOnWriteArrayList from the passed in values.
     */
    public static <T> CollectionType<CopyOnWriteArrayList<? extends T>, T> copyOnWriteArrayList(){
        return CopyOnWriteArrayList::new;
    }

    /**
     * Create a CollectionType instance for CopyOnWriteArrayList. The order of values in a CopyOnWriteArrayList created
     * using it will reflect their order in the input value list.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an CopyOnWriteArrayList from the passed in values.
     */
    public static <T> CollectionType<CopyOnWriteArrayList<? extends T>, T> copyOnWriteArrayList(Class<T> tClass){
        return CopyOnWriteArrayList::new;
    }

    /**
     * Create a CollectionType instance for CopyOnWriteArraySet.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an CopyOnWriteArraySet from the passed in values.
     */
    public static <T> CollectionType<CopyOnWriteArraySet<? extends T>, T> copyOnWriteArraySet(){
        return CopyOnWriteArraySet::new;
    }

    /**
     * Create a CollectionType instance for CopyOnWriteArraySet.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an CopyOnWriteArraySet from the passed in values.
     */
    public static <T> CollectionType<CopyOnWriteArraySet<? extends T>, T> copyOnWriteArraySet(Class<T> tClass){
        return CopyOnWriteArraySet::new;
    }

    /**
     * Create a CollectionType instance for DelayQueue. The order of values in a DelayQueue created
     * using it will reflect their order in the input value list.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an DelayQueue from the passed in values.
     */
    public static <T extends Delayed> CollectionType<DelayQueue<? extends T>, T> delayQueue(){
        return DelayQueue::new;
    }

    /**
     * Create a CollectionType instance for DelayQueue. The order of values in a DelayQueue created
     * using it will reflect their order in the input value list.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an DelayQueue from the passed in values.
     */
    public static <T extends Delayed> CollectionType<DelayQueue<? extends T>, T> delayQueue(Class<T> tClass){
        return DelayQueue::new;
    }

    /**
     * Create a CollectionType instance for LinkedBlockingDeque. The order of values in a LinkedBlockingDeque created
     * using it will reflect their order in the input value list.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an LinkedBlockingDeque from the passed in values.
     */
    public static <T> CollectionType<LinkedBlockingDeque<? extends T>, T> linkedBlockingDeque(){
        return LinkedBlockingDeque::new;
    }

    /**
     * Create a CollectionType instance for LinkedBlockingDeque. The order of values in a LinkedBlockingDeque created
     * using it will reflect their order in the input value list.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an LinkedBlockingDeque from the passed in values.
     */
    public static <T> CollectionType<LinkedBlockingDeque<? extends T>, T> linkedBlockingDeque(Class<T> tClass){
        return LinkedBlockingDeque::new;
    }

    /**
     * Create a CollectionType instance for LinkedBlockingDeque. The order of values in a LinkedBlockingDeque created
     * using it will reflect their order in the input value list.
     * @param capacity The capacity of the LinkedBlockingDeque to create.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an LinkedBlockingDeque from the passed in values.
     */
    public static <T> CollectionType<LinkedBlockingDeque<? extends T>, T> linkedBlockingDeque(int capacity){
        return values -> {
            LinkedBlockingDeque<T> blockingDeque = new LinkedBlockingDeque<>(capacity);
            blockingDeque.addAll(values);
            return blockingDeque;
        };
    }

    /**
     * Create a CollectionType instance for LinkedBlockingDeque. The order of values in a LinkedBlockingDeque created
     * using it will reflect their order in the input value list.
     * @param capacity The capacity of the LinkedBlockingDeque to create.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an LinkedBlockingDeque from the passed in values.
     */
    public static <T> CollectionType<LinkedBlockingDeque<? extends T>, T> linkedBlockingDeque(int capacity, Class<T> tClass){
        return linkedBlockingDeque(capacity);
    }

    /**
     * Create a CollectionType instance for LinkedBlockingQueue. The order of values in a LinkedBlockingQueue created
     * using it will reflect their order in the input value list.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create a LinkedBlockingQueue from the passed in values.
     */
    public static <T> CollectionType<LinkedBlockingQueue<? extends T>, T> linkedBlockingQueue(){
        return LinkedBlockingQueue::new;
    }

    /**
     * Create a CollectionType instance for LinkedBlockingQueue. The order of values in a LinkedBlockingQueue created
     * using it will reflect their order in the input value list.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create a LinkedBlockingQueue from the passed in values.
     */
    public static <T> CollectionType<LinkedBlockingQueue<? extends T>, T> linkedBlockingQueue(Class<T> tClass){
        return LinkedBlockingQueue::new;
    }

    /**
     * Create a CollectionType instance for LinkedBlockingQueue. The order of values in a LinkedBlockingQueue created
     * using it will reflect their order in the input value list.
     * @param capacity The capacity of the LinkedBlockingQueue to create.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an LinkedBlockingQueue from the passed in values.
     */
    public static <T> CollectionType<LinkedBlockingQueue<? extends T>, T> linkedBlockingQueue(int capacity){
        return values -> {
            LinkedBlockingQueue<T> blockingQueue = new LinkedBlockingQueue<>(capacity);
            blockingQueue.addAll(values);
            return blockingQueue;
        };
    }

    /**
     * Create a CollectionType instance for LinkedBlockingQueue. The order of values in a LinkedBlockingQueue created
     * using it will reflect their order in the input value list.
     * @param capacity The capacity of the LinkedBlockingQueue to create.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an LinkedBlockingQueue from the passed in values.
     */
    public static <T> CollectionType<LinkedBlockingQueue<? extends T>, T> linkedBlockingQueue(int capacity, Class<T> tClass){
        return linkedBlockingQueue(capacity);
    }

    /**
     * Create a CollectionType instance for LinkedTransferQueue. The order of values in a LinkedTransferQueue created
     * using it will reflect their order in the input value list.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create a LinkedTransferQueue from the passed in values.
     */
    public static <T> CollectionType<LinkedTransferQueue<? extends T>, T> linkedTransferQueue(){
        return LinkedTransferQueue::new;
    }

    /**
     * Create a CollectionType instance for LinkedTransferQueue. The order of values in a LinkedTransferQueue created
     * using it will reflect their order in the input value list.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create a LinkedTransferQueue from the passed in values.
     */
    public static <T> CollectionType<LinkedTransferQueue<? extends T>, T> linkedTransferQueue(Class<T> tClass){
        return LinkedTransferQueue::new;
    }

    /**
     * Create a CollectionType instance for PriorityBlockingQueue. The order of values in a PriorityBlockingQueue created
     *      * using it will reflect their order in the input value list and how their comparison further orders them.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create a PriorityBlockingQueue from the passed in values.
     */
    public static <T extends Comparable> CollectionType<PriorityBlockingQueue<? extends T>, T> priorityBlockingQueue(){
        return PriorityBlockingQueue::new;
    }

    /**
     * Create a CollectionType instance for PriorityBlockingQueue. The order of values in a PriorityBlockingQueue created
     *      * using it will reflect their order in the input value list and how their comparison further orders them.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create a PriorityBlockingQueue from the passed in values.
     */
    public static <T extends Comparable> CollectionType<PriorityBlockingQueue<? extends T>, T> priorityBlockingQueue(Class<T> tClass){
        return PriorityBlockingQueue::new;
    }

    /**
     * Create a CollectionType instance for PriorityBlockingQueue. The order of values in a PriorityBlockingQueue created
     *      * using it will reflect their order in the input value list and how their comparison further orders them.
     * @param comparator the comparator used in the PriorityBlockingQueue.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an PriorityBlockingQueue from the passed in values.
     * @todo think about comparators of super types.
     */
    public static <T> CollectionType<PriorityBlockingQueue<? extends T>, T> priorityBlockingQueue(Comparator<T> comparator){
        return values -> {
            PriorityBlockingQueue<T> blockingQueue = new PriorityBlockingQueue<>(values.size(), comparator);
            blockingQueue.addAll(values);
            return blockingQueue;
        };
    }

    /**
     * Create a CollectionType instance for SynchronousQueue. The order of values in a SynchronousQueue created
     * using it will reflect their order in the input value list.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create a SynchronousQueue from the passed in values.
     */
    public static <T> CollectionType<SynchronousQueue<? extends T>, T> synchronousQueue(){
        return values -> {
            SynchronousQueue<T> queue = new SynchronousQueue<>();
            queue.addAll(values);
            return queue;
        };
    }

    /**
     * Create a CollectionType instance for SynchronousQueue. The order of values in a SynchronousQueue created
     * using it will reflect their order in the input value list.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create a SynchronousQueue from the passed in values.
     */
    public static <T> CollectionType<SynchronousQueue<? extends T>, T> synchronousQueue(Class<T> tClass){
        return synchronousQueue();
    }

    /**
     * Create a CollectionType instance for SynchronousQueue. The order of values in a SynchronousQueue created
     * using it will reflect their order in the input value list.
     * @param fair Whether the queue will be fair or not.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create a SynchronousQueue from the passed in values.
     */
    public static <T> CollectionType<SynchronousQueue<? extends T>, T> synchronousQueue(boolean fair){
        return values -> {
            SynchronousQueue<T> queue = new SynchronousQueue<>(fair);
            queue.addAll(values);
            return queue;
        };
    }

    /**
     * Create a CollectionType instance for SynchronousQueue. The order of values in a SynchronousQueue created
     * using it will reflect their order in the input value list.
     * @param fair Whether the queue will be fair or not.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create a SynchronousQueue from the passed in values.
     */
    public static <T> CollectionType<SynchronousQueue<? extends T>, T> synchronousQueue(boolean fair, Class<T> tClass){
        return synchronousQueue(fair);
    }
}
