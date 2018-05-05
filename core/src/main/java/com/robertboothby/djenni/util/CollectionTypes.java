package com.robertboothby.djenni.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

/**
 * @todo Add static methods for the List, Set, SortedSet etc. interfaces.
 */
public class CollectionTypes {

    @SuppressWarnings("unchecked")
    public static <T extends List<U>, U> CollectionType<List<U>, U> asList(CollectionType<T, U> underlying) {
        return (CollectionType<List<U>, U>) underlying;
    }

    /**
     * Create a CollectionType instance for ArrayList. The order of values in an ArrayList created using it will reflect
     * their order in the input list.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the ArrayList from the passed in values.
     */
    public static <T> CollectionType<ArrayList<T>, T> arrayList() {
        return ArrayList::new;
    }

    /**
     * Create a CollectionType instance for ArrayList. The order of values in an ArrayList created using it will reflect
     * their order in the input list.
     * @param clazz The class of the type that will be held in this collection.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the ArrayList from the passed in values.
     */
    public static <T> CollectionType<ArrayList<T>, T> arrayList(Class<T> clazz) {
        return ArrayList::new;
    }

    /**
     * Create a CollectionType instance for ArrayDeque. The order of values in an ArrayList created using it will reflect
     * their order in the input list.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the ArrayDeque from the passed in values.
     */
    public static <T> CollectionType<ArrayDeque<T>, T> arrayDeque() {
        return ArrayDeque::new;
    }

    /**
     * Create a CollectionType instance for ArrayDeque. The order of values in an ArrayList created using it will reflect
     * their order in the input list.
     * @param clazz The class of the type that will be held in this collection.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the ArrayDeque from the passed in values.
     */
    public static <T> CollectionType<ArrayDeque<T>, T> arrayDeque(Class<T> clazz) {
        return ArrayDeque::new;
    }

    /**
     * Create a CollectionType instance for EnumSet. There is no ordering to the values in the resulting
     * EnumSets. There must be at least one value in any passed in list so that the EnumSets can determine their type.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the EnumSet from the passed in values.
     */
    public static <T extends Enum<T>> CollectionType<EnumSet<T>, T> enumSet() {
        return EnumSet::copyOf;
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
    public static <T> CollectionType<HashSet<T>, T> hashSet() {
        return HashSet::new;
    }

    /**
     * Create a CollectionType instance for HashSet. There is no ordering to the values in the resulting
     * HashSet.
     * @param clazz The class of the type that will be held in this collection.
     * @param <T> The type of the values in the collections.
     * @return A CollectionType instance that will create the HashSet from the passed in values.
     */
    public static <T> CollectionType<HashSet<T>, T> hashSet(Class<T> clazz) {
        return HashSet::new;
    }

    public static <T> CollectionType<LinkedHashSet<T>, T> linkedHashSet() {
        return LinkedHashSet::new;
    }

    public static <T> CollectionType<LinkedHashSet<T>, T> linkedHashSet(Class<T> clazz) {
        return LinkedHashSet::new;
    }

    public static <T> CollectionType<LinkedList<T>, T> linkedList() {
        return LinkedList::new;
    }

    public static <T> CollectionType<LinkedList<T>, T> linkedList(Class<T> clazz) {
        return LinkedList::new;
    }

    /**
     * Creates a Priority Queue with the desired elements. This uses the {@link PriorityQueue#PriorityQueue(Collection)}
     * constructor and so will typically use their comparable ordering.
     * @param <T> The type of object in the collection which needs to be comparable.
     * @return a CollectionType which can instantiate a PriorityQueue from a list of values.
     */
    public static <T extends Comparable<T>> CollectionType<PriorityQueue<T>, T> priorityQueue() {
        return PriorityQueue::new;
    }

    /**
     * Creates a Priority Queue with the desired elements. This uses the {@link PriorityQueue#PriorityQueue(Collection)}
     * constructor and so will typically use their comparable ordering.
     * @param clazz The class of the type that will be held in this collection.
     * @param <T> The type of object in the collection which needs to be comparable.
     * @return a CollectionType which can instantiate a PriorityQueue from a list of values.
     */
    public static <T extends Comparable<T>> CollectionType<PriorityQueue<T>, T> priorityQueue(Class<T> clazz) {
        return PriorityQueue::new;
    }

    public static <T, U extends Comparator<T>> CollectionType<PriorityQueue<T>, T> priorityQueue(U comparator) {
        return (values) -> {
           PriorityQueue<T> priorityQueue = new PriorityQueue<>(comparator);
           priorityQueue.addAll(values);
           return priorityQueue;
        };
    }

    public static <T> CollectionType<Stack<T>, T> stack() {
        return values -> {
            Stack<T> stack = new Stack<>();
            values.forEach(stack::push);
            return stack;
        };
    }

    public static <T> CollectionType<Stack<T>, T> stack(Class<T> clazz) {
        return values -> {
            Stack<T> stack = new Stack<>();
            values.forEach(stack::push);
            return stack;
        };
    }

    public static <T extends Comparable<T>> CollectionType<TreeSet<T>, T> treeSet() {
        return TreeSet::new;
    }

    public static <T extends Comparable<T>> CollectionType<TreeSet<T>, T> treeSet(Class<T> clazz) {
        return TreeSet::new;
    }

    public static <T, U extends Comparator<T>> CollectionType<TreeSet<T>, T> treeSet(U comparator) {
        return values -> {
            TreeSet<T> treeSet = new TreeSet<>(comparator);
            treeSet.addAll(values);
            return treeSet;
        };
    }

    public static <T> CollectionType<Vector<T>, T> vector() {
        return Vector::new;
    }

    public static <T> CollectionType<Vector<T>, T> vector(Class<T> clazz) {
        return Vector::new;
    }
}
