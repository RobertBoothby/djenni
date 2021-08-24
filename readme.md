<h1>Djenni</h1>
<h2>Test Data Generation Framework</h3>
Djenni is the distillation of many years building diverse systems that all needed testing and as a result needed test data.
<h3>Why Djenni?</h3>

<em>"The test outcome is often unaffected by the exact values of many of the inputs"</em>

This is an unstated assumption in most testing that the values of the uninteresting fields will not affect the result of
the test. In my experience this is usually a dangerous assumption.

<h3>What does this mean?</h3>

In the majority of tests the point of the test is usually affected by one or two values. For example if we are testing
a validator that says verifies a vehicle is a bicycle only if it has two wheels then the color, the power-train and the
style of the frame should be irrelevant to validation. How do we prove that they are?

We should be able to put random values into the irrelevant data elements in a test and there should be no
change in outcome.

<h3>What does Djenni do?</h3>
Djenni is designed to make it easy to set up test data for all levels of testing and effectively assert which test data
is irrelevant to the outome and which data matters.

Djenni also makes it easy to generate large volumes of test data for rich domains and can scale well to support
everything from unit testing to large scale non-functional testing.

Djenni contains a Maven plugin that can generate a lot of the Suppliers and SupplierBuilders for your domain.

Djenni now contains an experimental dynamically introspecting Supplier Builder that works best with classes that implement 
the JavaBeans getter and setter patterns.
```java
        StreamableSupplier<TestClass> testClassSupplier = supplierFor(TestClass.class)
            .byGet($ -> $::getValueTwo, integerSupplier().between(1).and(10))
            .byGet($ -> $::getValueOne, fix("One"))
            .build();
        TestClass testClass = testClassSupplier.get();
        //OR
        Stream<TestClass> testClassStream = testClassSupplier.stream();
        //OR
        Stream<TestClass> testClassStreamOfTen = testClassSupplier.stream(10);
```

<h3>The Core Pattern</h3>

Djenni is built around the concept of a &quot;Supplier&quot; and a &quot;SupplierBuilder&quot;.

The Supplier is the ultimate source of test data. Suppliers produce the objects required by the tests. These objects
composed of other objects and in the same way most generators are composed of other generators. When a Supplier is used
to generate an object, it used other Suppliers to generate the object's fields which themselves may be composed of other 
Suppliers and so on.

Suppliers should typically be immutable so that they are thread safe.

The SupplierBuilder makes it easy to configure and build instances of its associated Supplier. SupplierBuilders are 
mutable and are not thread safe.

Once you have used the SupplierBuilder to configure and build your Supplier, you can generate as many instances of
your test object as you want, usually on as many threads as you want.

By implementing your own Suppliers and SupplierBuilders you can rapidly build up a data generation suite for your entire
domain that will make testing a lot easier.

