package com.robertboothby.djenni.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

/**
 * This class contains static factory methods for creating CollectionTypes for all collections in the java.util.concurrent
 * package.
 */
public class CollectionTypes {

    @SuppressWarnings("unchecked")
    public static <T extends Collection<? extends U>, U> CollectionType<Collection<? extends U>, U> asCollection(CollectionType<T, U> underlying) {
        return (CollectionType<Collection<? extends U>, U>) underlying;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Deque<? extends U>, U> CollectionType<Deque<? extends U>, U> asDeque(CollectionType<T, U> underlying) {
        return (CollectionType<Deque<? extends U>, U>) underlying;
    }

    @SuppressWarnings("unchecked")
    public static <T extends List<? extends U>, U> CollectionType<List<? extends U>, U> asList(CollectionType<T, U> underlying) {
        return (CollectionType<List<? extends U>, U>) underlying;
    }

    @SuppressWarnings("unchecked")
    public static <T extends NavigableSet<? extends U>, U> CollectionType<NavigableSet<? extends U>, U> asNavigableSet(CollectionType<T, U> underlying) {
        return (CollectionType<NavigableSet<? extends U>, U>) underlying;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Queue<? extends U>, U> CollectionType<Queue<? extends U>, U> asQueue(CollectionType<T, U> underlying) {
        return (CollectionType<Queue<? extends U>, U>) underlying;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Set<? extends U>, U> CollectionType<Set<? extends U>, U> asSet(CollectionType<T, U> underlying) {
        return (CollectionType<Set<? extends U>, U>) underlying;
    }

    @SuppressWarnings("unchecked")
    public static <T extends SortedSet<? extends U>, U> CollectionType<SortedSet<? extends U>, U> asSortedSet(CollectionType<T, U> underlying) {
        return (CollectionType<SortedSet<? extends U>, U>) underlying;
    }

    /**
     * Create a CollectionType instance for ArrayList. The order of values in an ArrayList created using it will reflect
     * their order in the input list.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the ArrayList from the passed in values.
     */
    public static <T> CollectionType<ArrayList<? extends T>, T> arrayList() {
        return ArrayList::new;
    }

    /**
     * Create a CollectionType instance for ArrayList. The order of values in an ArrayList created using it will reflect
     * their order in the input list.
     * @param clazz The class of the type that will be held in this collection.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the ArrayList from the passed in values.
     */
    public static <T> CollectionType<ArrayList<? extends T>, T> arrayList(Class<? extends T> clazz) {
        return ArrayList::new;
    }

    /**
     * Create a CollectionType instance for ArrayDeque. The order of values in an ArrayList created using it will reflect
     * their order in the input list.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the ArrayDeque from the passed in values.
     */
    public static <T> CollectionType<ArrayDeque<? extends T>, T> arrayDeque() {
        return ArrayDeque::new;
    }

    /**
     * Create a CollectionType instance for ArrayDeque. The order of values in an ArrayList created using it will reflect
     * their order in the input list.
     * @param clazz The class of the type that will be held in this collection.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the ArrayDeque from the passed in values.
     */
    public static <T> CollectionType<ArrayDeque<? extends T>, T> arrayDeque(Class<? extends T> clazz) {
        return ArrayDeque::new;
    }

    /**
     * Create a CollectionType instance for EnumSet. There is no ordering to the values in the resulting
     * EnumSets. There must be at least one value in any passed in list so that the EnumSets can determine their type.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the EnumSet from the passed in values.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> CollectionType<EnumSet<T>, T> enumSet() {
        return values -> EnumSet.copyOf((Collection<T>)values);
    }

    /**
     * Create a CollectionType instance for EnumSet. There is no ordering to the values in the resulting
     * EnumSets.
     * @param tClass the enum class that the EnumSet will contain.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the EnumSet from the passed in values.
     */
    public static <T extends Enum<T>> CollectionType<EnumSet<T>, T> enumSet(Class<T> tClass) {
        return values -> {
          EnumSet<T> enumSet = EnumSet.noneOf(tClass);
          enumSet.addAll(values);
          return enumSet;
        };
    }

    /**
     * Create a CollectionType instance for HashSet. There is no ordering to the values in the resulting
     * HashSet.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the HashSet from the passed in values.
     */
    public static <T> CollectionType<HashSet<? extends T>, T> hashSet() {
        return HashSet::new;
    }

    /**
     * Create a CollectionType instance for HashSet. There is no ordering to the values in the resulting
     * HashSet.
     * @param clazz The class of the type that will be held in this collection.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the HashSet from the passed in values.
     */
    public static <T> CollectionType<HashSet<? extends T>, T> hashSet(Class<? extends T> clazz) {
        return HashSet::new;
    }

    public static <T> CollectionType<LinkedHashSet<? extends T>, T> linkedHashSet() {
        return LinkedHashSet::new;
    }

    public static <T> CollectionType<LinkedHashSet<? extends T>, T> linkedHashSet(Class<? extends T> clazz) {
        return LinkedHashSet::new;
    }

    public static <T> CollectionType<LinkedList<? extends T>, T> linkedList() {
        return LinkedList::new;
    }

    public static <T> CollectionType<LinkedList<? extends T>, T> linkedList(Class<? extends T> clazz) {
        return LinkedList::new;
    }

    /**
     * Creates a Priority Queue with the desired elements. This uses the {@link PriorityQueue#PriorityQueue(Collection)}
     * constructor and so will typically use their comparable ordering.
     * @param <T> The type of object in the collection which needs to be comparable.
     * @return a CollectionType which can instantiate a PriorityQueue from a list of values.
     */
    public static <T extends Comparable<T>> CollectionType<PriorityQueue<? extends T>, T> priorityQueue() {
        return PriorityQueue::new;
    }

    /**
     * Creates a Priority Queue with the desired elements. This uses the {@link PriorityQueue#PriorityQueue(Collection)}
     * constructor and so will typically use their comparable ordering.
     * @param clazz The class of the type that will be held in this collection.
     * @param <T> The type of object in the collection which needs to be comparable.
     * @return a CollectionType which can instantiate a PriorityQueue from a list of values.
     */
    public static <T extends Comparable<T>> CollectionType<PriorityQueue<? extends T>, T> priorityQueue(Class<? extends T> clazz) {
        return PriorityQueue::new;
    }

    public static <T, U extends Comparator<T>> CollectionType<PriorityQueue<? extends T>, T> priorityQueue(U comparator) {
        return (values) -> {
           PriorityQueue<T> priorityQueue = new PriorityQueue<>(comparator);
           priorityQueue.addAll(values);
           return priorityQueue;
        };
    }

    public static <T> CollectionType<Stack<? extends T>, T> stack() {
        return values -> {
            Stack<T> stack = new Stack<>();
            values.forEach(stack::push);
            return stack;
        };
    }

    public static <T> CollectionType<Stack<? extends T>, T> stack(Class<? extends T> clazz) {
        return values -> {
            Stack<T> stack = new Stack<>();
            values.forEach(stack::push);
            return stack;
        };
    }

    public static <T extends Comparable<T>> CollectionType<TreeSet<? extends T>, T> treeSet() {
        return TreeSet::new;
    }

    public static <T extends Comparable<T>> CollectionType<TreeSet<? extends T>, T> treeSet(Class<? extends T> clazz) {
        return TreeSet::new;
    }

    public static <T, U extends Comparator<T>> CollectionType<TreeSet<? extends T>, T> treeSet(U comparator) {
        return values -> {
            TreeSet<T> treeSet = new TreeSet<>(comparator);
            treeSet.addAll(values);
            return treeSet;
        };
    }

    public static <T> CollectionType<Vector<? extends T>, T> vector() {
        return Vector::new;
    }

    public static <T> CollectionType<Vector<? extends T>, T> vector(Class<? extends T> clazz) {
        return Vector::new;
    }
}
