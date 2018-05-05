package com.robertboothby.djenni.util.concurrent;

import com.robertboothby.djenni.util.CollectionType;

import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

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
     * Create a CollectionType instance for ArrayBlockingQueue. The order of values in an ArrayBlockingQueue created
     * using it will reflect their order in the input value list, the capacity will be defined by the size of the value
     * list..
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ArrayBlockingQueue from the passed in values.
     */
    public static <T> CollectionType<ArrayBlockingQueue<T>, T> arrayBlockingQueue(){
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
    public static <T> CollectionType<ArrayBlockingQueue<T>, T> arrayBlockingQueue(Class<T> tClass){
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
    public static <T> CollectionType<ArrayBlockingQueue<T>, T> arrayBlockingQueue(int capacity){
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
    public static <T> CollectionType<ArrayBlockingQueue<T>, T> arrayBlockingQueue(int capacity, Class<T> tClass){
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
    public static <T> CollectionType<ArrayBlockingQueue<T>, T> arrayBlockingQueue(int capacity, boolean fair){
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
    public static <T> CollectionType<ArrayBlockingQueue<T>, T> arrayBlockingQueue(int capacity, boolean fair, Class<T> tClass){
        return arrayBlockingQueue(capacity, fair);
    }

    /**
     * Create a CollectionType instance for ConcurrentLinkedDeque. The order of values in an ConcurrentLinkedDeque created
     * using it will reflect their order in the input value list.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentLinkedDeque from the passed in values.
     */
    public static <T> CollectionType<ConcurrentLinkedDeque<T>, T> concurrentLinkedDeque(){
        return ConcurrentLinkedDeque::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentLinkedDeque. The order of values in an ConcurrentLinkedDeque created
     * using it will reflect their order in the input value list.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentLinkedDeque from the passed in values.
     */
    public static <T> CollectionType<ConcurrentLinkedDeque<T>, T> concurrentLinkedDeque(Class<T> tClass){
        return ConcurrentLinkedDeque::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentLinkedQueue. The order of values in an ConcurrentLinkedQueue created
     * using it will reflect their order in the input value list.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentLinkedQueue from the passed in values.
     */
    public static <T> CollectionType<ConcurrentLinkedQueue<T>, T> concurrentLinkedQueue(){
        return ConcurrentLinkedQueue::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentLinkedQueue. The order of values in an ConcurrentLinkedQueue created
     * using it will reflect their order in the input value list.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentLinkedQueue from the passed in values.
     */
    public static <T> CollectionType<ConcurrentLinkedQueue<T>, T> concurrentLinkedQueue(Class<T> tClass){
        return ConcurrentLinkedQueue::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentSkipListSet.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentSkipListSet from the passed in values.
     */
    public static <T extends Comparable> CollectionType<ConcurrentSkipListSet<T>, T> concurrentSkipListSet(){
        return ConcurrentSkipListSet::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentSkipListSet.
     * @param tClass The class of the values in the collections created.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentSkipListSet from the passed in values.
     */
    public static <T extends Comparable> CollectionType<ConcurrentSkipListSet<T>, T> concurrentSkipListSet(Class<T> tClass){
        return ConcurrentSkipListSet::new;
    }

    /**
     * Create a CollectionType instance for ConcurrentSkipListSet.
     * @param comparator the comparator used in the ConcurrentSkipListSet.
     * @param <T> The type of the values in the collections created.
     * @return A CollectionType instance that will create an ConcurrentSkipListSet from the passed in values.
     * @todo think about comparators of super types.
     */
    public static <T extends Comparable> CollectionType<ConcurrentSkipListSet<T>, T> concurrentSkipListSet(Comparator<T> comparator){
        return values -> new ConcurrentSkipListSet<>(comparator);
    }



}
