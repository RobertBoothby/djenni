# Djenni Core - User Guide
Assuming that you have followed the guidance in the Quickstart then this document will walk you through how to use many 
of the features of Djenni.

## Core Constructs
Djenni's core constructs are the SupplierBuilder interface:
```
package com.robertboothby.djenni;

import com.robertboothby.djenni.core.StreamableSupplier;

/**
 * A builder of object Suppliers, implementing the 'Builder' pattern. Instances of this interface will usually
 * be not thread safe and highly mutable.
 * @author robertboothby
 * @param <T> The type of the value that will be supplied.
 */
public interface SupplierBuilder<T> {

    /**
     * Build an instance of the generator based on the current configuration state.
     * @return an instance of the generator.
     */
    StreamableSupplier<T> build();
}
```
And the StreamableSupplier:
```
package com.robertboothby.djenni.core;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Extension interface to the basic {@link Supplier} interface that adds a stream method.
 * @param <T> The type of values supplied by the Supplier and any streams derived from it.
 */
public interface StreamableSupplier<T> extends Supplier<T> {

    /**
     * Return a limited Stream using this supplier as a source.
     * @param numberOfValues The number of values from this supplier to stream.
     * @return A Stream instance derived from this supplier.
     */
    default Stream<T> stream(long numberOfValues){
        return SupplierHelper.stream(this, numberOfValues);
    }

    /**
     * Return an infinite Stream using this supplier as a source.
     * @return A Stream instance derived from this supplier.
     */
    default Stream<T> stream() {
        return SupplierHelper.stream(this);
    }

    /**
     * Return a new Supplier derived from this supplier using the function passed in.
     * @param derivation The derivation function.
     * @param <R> The type returned from the derivation function.
     * @return A new Supplier derived from this one.
     */
    default <R> StreamableSupplier<R> derive(Function<T, R> derivation){
        return SupplierHelper.derived(derivation, this);
    }

    /**
     * Return a new Supplier derived from this supplier using the function passed in and a second supplier.
     * @param derivation The derivation function.
     * @param otherSupplier The other supplier.
     * @param <R> The type returned from the derivation function.
     * @return A new Supplier derived from this one and the other.
     */
    default <U,R> StreamableSupplier<R> derive(BiFunction<T, U ,R> derivation, Supplier<U> otherSupplier){
        return SupplierHelper.derived(derivation, this, otherSupplier);
    }
}
```
These interfaces crystallise the concept of configuring and building a Supplier of data and then allowing that Supplier
of data to provide large quantities of data.

An extension to the SupplierBuilder is the ConfigurableSupplierBuilder that uses the Configurable interface that makes it easy to provide pre-defined
configurations that can be customised later:
```
package com.robertboothby.djenni.util;

import java.util.function.Consumer;

/**
 * <p>
 *     Useful interface that allows us to trivially add a method that allows a Consumer to configure the class in question.
 * </p>
 * <p>
 *     This is primarily intended to allow us to compose multiple pre-canned configurations together in a fluent style.
 * </p>
 * @param <T> The concrete implementation of the Configurable interface.
 */
public interface Configurable<T extends Configurable<T>> {

    /**
     * Supply a Consumer that will configure this class.
     * @param config The configuration to use.
     * @return This class for further actions.
     */
    @SuppressWarnings("unchecked")
    default T configure(Consumer<T> config){
        config.accept((T)this);
        return (T)this;
    }
}
```
```
package com.robertboothby.djenni;

import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.util.Configurable;

import java.util.function.Consumer;

/**
 * Multiple Inheritance interface that combines the SupplierBuilder and Configurable interfaces.
 * @param <T> The type of object that will eventually be supplied.
 * @param <U> A self reference to the type of the concrete implementation.
 */
public interface ConfigurableSupplierBuilder <T, U extends ConfigurableSupplierBuilder<T,U>>
        extends SupplierBuilder<T>, Configurable<U>{

    /**
     * Convenience method for when you want to get a supplier immediately from a configuration with no later customisation.
     * @param configuration The configuration to use.
     * @return A Supplier build from the configuration.
     */
    default StreamableSupplier<T> build(Consumer<U> configuration){
        return this.configure(configuration).build();
    }
}
```
This extension is the one that you will normally want to use as it will give you most flexibility.
## My First SupplierBuilder
Let's put this together and create a simple SupplierBuilder for people and show some of the potential usage patterns.

For this example we're going to create this SupplierBuilder longhand, but we can also use the DynamicSupplierBuilder to
reduce the boilerplate.

First, let's define the Person data class that we're going to be generating.
```
package com.robertboothby.djenni.examples;

import java.time.Instant;
import java.util.List;

public class Person {
    private final String[] givenNames;
    private String familyName;
    private final Instant timeOfBirth;
    private final List<String> titlesByPrecedence;

    public Person(String[] givenNames, String familyName, Instant timeOfBirth, List<String> titlesByPrecedence) {
        this.givenNames = givenNames;
        this.familyName = familyName;
        this.timeOfBirth = timeOfBirth;
        this.titlesByPrecedence = titlesByPrecedence;
    }

    public String[] getGivenNames() {
        return givenNames;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Instant getTimeOfBirth() {
        return timeOfBirth;
    }

    public List<String> getTitlesByPrecedence() {
        return titlesByPrecedence;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("givenNames=").append(Arrays.toString(givenNames));
        sb.append(", familyName='").append(familyName).append('\'');
        sb.append(", timeOfBirth=").append(timeOfBirth);
        sb.append(", titlesByPrecedence=").append(titlesByPrecedence);
        sb.append('}');
        return sb.toString();
    }
}
```
This has been designed to show how you can generate a number of common constructs with Djenni.

Now let's build a custom SupplierBuilder:

First step is to create the class extending `ConfigurableSupplierBuilder` or `SupplierBuilder`. The first one gives us access to the configure method which can make for a more fluent approach but it is a little more complex to declare.
```
package com.robertboothby.djenni.examples;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;
import com.robertboothby.djenni.core.SupplierHelper;

public class PersonSupplierBuilder implements ConfigurableSupplierBuilder<Person, PersonSupplierBuilder> {
    @Override
    public StreamableSupplier<Person> build() {
        return null;
    }
}
```
We have let the IDE implement the build() with a simple null return. Please note that the build method returns a `StreamableSupplier` which makes it trivial to derive bounded and unbounded streams from a Supplier. I'm surprised that this isn't part of the default Java `Supplier` interface.

Now let us start fleshing out the builder with the ability to build a concrete instance of the person interface.

```
package com.robertboothby.djenni.examples;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

public class PersonSupplierBuilder implements ConfigurableSupplierBuilder<Person, PersonSupplierBuilder> {
    private Supplier<String[]> givenNamesSupplier = SupplierHelper.nullSupplier();
    private Supplier<String> familyNameSupplier = SupplierHelper.nullSupplier();
    private Supplier<Instant> timeOfBirthSupplier = SupplierHelper.nullSupplier();
    private Supplier<List<String>> titlesByPrecedenceSupplier = SupplierHelper.nullSupplier();

    @Override
    public StreamableSupplier<Person> build() {
        return () -> new Person(
                givenNamesSupplier.get(), 
                familyNameSupplier.get(), 
                timeOfBirthSupplier.get(), 
                titlesByPrecedenceSupplier.get()
        );
    }
}
```
So, now we're generating instances of `Person` but all its attributes are `null`. Notice that we are using the instance variables (for now).

Let's add the builder setter methods (Many IDEs have mechanisms to make this really easy!).
```
package com.robertboothby.djenni.examples;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

public class PersonSupplierBuilder implements ConfigurableSupplierBuilder<Person, PersonSupplierBuilder> {
    private Supplier<String[]> givenNamesSupplier = SupplierHelper.nullSupplier();
    private Supplier<String> familyNameSupplier = SupplierHelper.nullSupplier();
    private Supplier<Instant> timeOfBirthSupplier = SupplierHelper.nullSupplier();
    private Supplier<List<String>> titlesByPrecedenceSupplier = SupplierHelper.nullSupplier();

    @Override
    public StreamableSupplier<Person> build() {
        return () -> new Person(
                givenNamesSupplier.get(), 
                familyNameSupplier.get(), 
                timeOfBirthSupplier.get(), 
                titlesByPrecedenceSupplier.get()
        );
    }

    public PersonSupplierBuilder setGivenNamesSupplier(Supplier<String[]> givenNamesSupplier) {
        this.givenNamesSupplier = givenNamesSupplier;
        return this;
    }

    public PersonSupplierBuilder setFamilyNameSupplier(Supplier<String> familyNameSupplier) {
        this.familyNameSupplier = familyNameSupplier;
        return this;
    }

    public PersonSupplierBuilder setTimeOfBirthSupplier(Supplier<Instant> timeOfBirthSupplier) {
        this.timeOfBirthSupplier = timeOfBirthSupplier;
        return this;
    }

    public PersonSupplierBuilder setTitlesByPrecedenceSupplier(Supplier<List<String>> titlesByPrecedenceSupplier) {
        this.titlesByPrecedenceSupplier = titlesByPrecedenceSupplier;
        return this;
    }
}
```
This still compiles even though we have added mutators for the instance variables? This may not be surprising for you but it was for me. I had to read up to learn that 'effectively final' only applies to local variables and not to instance variables.

This can become an issue when you want thread safe suppliers or to derive multiple suppliers from the same supplier builder with different configurations. I'm going to fix this by creating effectively final local variables in the build() method.

```
package com.robertboothby.djenni.examples;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.core.SupplierHelper;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

public class PersonSupplierBuilder implements ConfigurableSupplierBuilder<Person, PersonSupplierBuilder> {
    private Supplier<String[]> givenNamesSupplier = SupplierHelper.nullSupplier();
    private Supplier<String> familyNameSupplier = SupplierHelper.nullSupplier();
    private Supplier<Instant> timeOfBirthSupplier = SupplierHelper.nullSupplier();
    private Supplier<List<String>> titlesByPrecedenceSupplier = SupplierHelper.nullSupplier();

    @Override
    public StreamableSupplier<Person> build() {
        //Effectively final local variables to isolate the generated supplier from any changes to the builder.
        Supplier<String[]> givenNamesSupplier = this.givenNamesSupplier;
        Supplier<String> familyNameSupplier = this.familyNameSupplier;
        Supplier<Instant> timeOfBirthSupplier = this.timeOfBirthSupplier;
        Supplier<List<String>> titlesByPrecedenceSupplier = this.titlesByPrecedenceSupplier;
        return () -> new Person(
                givenNamesSupplier.get(),
                familyNameSupplier.get(),
                timeOfBirthSupplier.get(),
                titlesByPrecedenceSupplier.get()
        );
    }

    public PersonSupplierBuilder setGivenNamesSupplier(Supplier<String[]> givenNamesSupplier) {
        this.givenNamesSupplier = givenNamesSupplier;
        return this;
    }

    public PersonSupplierBuilder setFamilyNameSupplier(Supplier<String> familyNameSupplier) {
        this.familyNameSupplier = familyNameSupplier;
        return this;
    }

    public PersonSupplierBuilder setTimeOfBirthSupplier(Supplier<Instant> timeOfBirthSupplier) {
        this.timeOfBirthSupplier = timeOfBirthSupplier;
        return this;
    }

    public PersonSupplierBuilder setTitlesByPrecedenceSupplier(Supplier<List<String>> titlesByPrecedenceSupplier) {
        this.titlesByPrecedenceSupplier = titlesByPrecedenceSupplier;
        return this;
    }
}
```
Another completely reasonable way to handle this issue is to create a concrete `PersonSupplier` class extending StreamableSupplier and use that in the `public StreamableSupplier<Person> build(){...}` method instead.

Now that we have a PersonSupplierBuilder class we can supply some reasonable defaults for the attributes instead of nulls.

This way we can have 'valid' persons being generated with no additional configuration if the exact values of the attributes are irrelevant to the context in which the `Person` class is being used.
```
package com.robertboothby.djenni.examples;

import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.lang.StringSupplierBuilder;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.robertboothby.djenni.lang.LongSupplierBuilder.generateALong;
import static com.robertboothby.djenni.lang.StringSupplierBuilder.arbitraryString;
import static com.robertboothby.djenni.util.SimpleListSupplierBuilder.simpleList;

public class PersonSupplierBuilder implements ConfigurableSupplierBuilder<Person, PersonSupplierBuilder> {
    private Supplier<String[]> givenNamesSupplier;
    private Supplier<String> familyNameSupplier;
    private Supplier<Instant> timeOfBirthSupplier;
    private Supplier<List<String>> titlesByPrecedenceSupplier;

    public PersonSupplierBuilder() {
        StreamableSupplier<String> arbitraryStrings = arbitraryString().build();
        StreamableSupplier<Long> arbitraryLongs = generateALong().build();
        givenNamesSupplier = () -> arbitraryStrings.stream(1).toArray(String[]::new);
        familyNameSupplier = arbitraryStrings;
        timeOfBirthSupplier = () -> Instant.ofEpochMilli(arbitraryLongs.get());
        titlesByPrecedenceSupplier = simpleList($ -> $.entries(arbitraryStrings).withSizeBetween(1).and(3));
    }

    @Override
    public StreamableSupplier<Person> build() {
        //Effectively final local variables to isolate the generated supplier from any changes to the builder.
        Supplier<String[]> givenNamesSupplier = this.givenNamesSupplier;
        Supplier<String> familyNameSupplier = this.familyNameSupplier;
        Supplier<Instant> timeOfBirthSupplier = this.timeOfBirthSupplier;
        Supplier<List<String>> titlesByPrecedenceSupplier = this.titlesByPrecedenceSupplier;
        return () -> new Person(
                givenNamesSupplier.get(),
                familyNameSupplier.get(),
                timeOfBirthSupplier.get(),
                titlesByPrecedenceSupplier.get()
        );
    }

    public PersonSupplierBuilder setGivenNamesSupplier(Supplier<String[]> givenNamesSupplier) {
        this.givenNamesSupplier = givenNamesSupplier;
        return this;
    }

    public PersonSupplierBuilder setFamilyNameSupplier(Supplier<String> familyNameSupplier) {
        this.familyNameSupplier = familyNameSupplier;
        return this;
    }

    public PersonSupplierBuilder setTimeOfBirthSupplier(Supplier<Instant> timeOfBirthSupplier) {
        this.timeOfBirthSupplier = timeOfBirthSupplier;
        return this;
    }

    public PersonSupplierBuilder setTitlesByPrecedenceSupplier(Supplier<List<String>> titlesByPrecedenceSupplier) {
        this.titlesByPrecedenceSupplier = titlesByPrecedenceSupplier;
        return this;
    }

    public static PersonSupplierBuilder aPerson(){
        return new PersonSupplierBuilder();
    }

    public static PersonSupplierBuilder aPerson(Consumer<PersonSupplierBuilder> initialConfig){
        return new PersonSupplierBuilder().configure(initialConfig);
    }
}
```
Now we're actually generating some default values by using some inbuilt `SupplierBuilder`s. The `arbitraryString()` method simply generates a short, fully randomised string of upper case characters. The `simpleList()` method allows you to generate a simple list of randomised values when composed with another Supplier.

I have slightly optimised these by defining the `arbitraryStrings` and `arbitraryLongs` variables so that these underlying suppliers are not regenerated every time the `get()` method is called on `StreamableSupplier<Person>` instance that is created.

I have also added a couple of static methods at the end to make the actual usage a bit tidier and more expressive.

## Customising and configuring
Now let's look at customising and configuring this new `PersonSupplierBuilder`.

```
    public StreamableSupplier<Person> inlineConfig() {
        //Provide a default configuration immediately.
        PersonSupplierBuilder personSupplierBuilder = aPerson($ ->
                //Let's now constrain the date of birth to between the start of the epoch and now.
                $.setTimeOfBirthSupplier(
                        generateALong()
                                .between(0L)
                                .and(System.currentTimeMillis())
                                .build()
                                .derive(Instant::ofEpochMilli)
                )
        );
        return personSupplierBuilder.build();
    }

    public StreamableSupplier<Person> postFactConfig() {
        PersonSupplierBuilder personSupplierBuilder = aPerson();
        //...
        //Provide a configuration after the fact.
        personSupplierBuilder.configure($ -> $.setTimeOfBirthSupplier(
                generateALong()
                        .between(0L)
                        .and(System.currentTimeMillis())
                        .build()
                        .derive(Instant::ofEpochMilli)
        ));
        return personSupplierBuilder.build();
    }

    public StreamableSupplier<Person> postFactSet() {
        PersonSupplierBuilder personSupplierBuilder = aPerson();
        //...
        //Configure the builder directly.
        personSupplierBuilder
                .setTimeOfBirthSupplier(
                        generateALong()
                                .between(0L)
                                .and(System.currentTimeMillis())
                                .build()
                                .derive(Instant::ofEpochMilli)
                );
        return personSupplierBuilder.build();
    }


```
This code fragment shows three equivalent ways of customising / configuring the `PersonSupplierBuilder`. The two approaches that take lambda functions are a touch more flexible from a fluent API perspective but it all comes down to personal preference.

It gets a little clearer when we define a method that can then be used as a lambda.
```
    public StreamableSupplier<Person> inlineConfig2() {
        //Provide a default configuration immediately.
        return aPerson(this::epochBirthdate).build();
    }

    public StreamableSupplier<Person> postFactConfig2() {
        PersonSupplierBuilder personSupplierBuilder = aPerson();
        //...
        //Provide a configuration after the fact.
        personSupplierBuilder.configure(this::epochBirthdate);
        return personSupplierBuilder.build();
    }

    public StreamableSupplier<Person> postFactSet2() {
        PersonSupplierBuilder personSupplierBuilder = aPerson();
        //...
        //Configure the builder directly.
        epochBirthdate(aPerson());
        return personSupplierBuilder.build();
    }

    private PersonSupplierBuilder epochBirthdate(PersonSupplierBuilder $) {
        return $.setTimeOfBirthSupplier(
                generateALong()
                        .between(0L)
                        .and(System.currentTimeMillis())
                        .build()
                        .derive(Instant::ofEpochMilli)
        );
    }
```
The use of the `derive(Function)` method is worth highlighting as this provides a neat method for deriving new suppliers from previous suppliers. For example you may have a domain type that you want to get the serialised JSON version of and you can derive a new supplier from the supplier of the base domain type. In addition, there is a `derive(BiFunction)` method that allows for the derivation of new suppliers by composing multiple suppliers.
## Dynamic Supplier Builder

The `DynamicSupplierBuilder` is a utility class that allows you to create a Supplier without needing to manually create
a SupplierBuilder. It accomplishes this using a combination of JavaBeans reflection and some Lambda Introspection.

To use this builder the class that you want to generate instances of, *must* have been compiled using the `-parameters` flag
so that it can introspect properly the parameter names of any constructor used.

Any suppliers it creates will perform a bit worse than those created by a hand-crafted or code generated SupplierBuilder but for many use
cases the difference will be negligible.

The syntax for configuring it is more clunky than a bespoke SupplierBuilder but is, hopefully, not that much more clunky.

Here is an example of how to use the `DynamicSupplierBuilder` to create a `Person` supplier.

First the Person class which has been compiled with the `-parameters` flag.
```
package com.robertboothby.djenni.examples.examples;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class Person {
    private final String[] givenNames;
    private String familyName;
    private final Instant timeOfBirth;
    private final List<String> titlesByPrecedence;

    public Person(String[] givenNames, String familyName, Instant timeOfBirth, List<String> titlesByPrecedence) {
        this.givenNames = givenNames;
        this.familyName = familyName;
        this.timeOfBirth = timeOfBirth;
        this.titlesByPrecedence = titlesByPrecedence;
    }

    public String[] getGivenNames() {
        return givenNames;
    }

    public String getFamilyName() {
        return familyName;
    }

    public Instant getTimeOfBirth() {
        return timeOfBirth;
    }

    public List<String> getTitlesByPrecedence() {
        return titlesByPrecedence;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("givenNames=").append(Arrays.toString(givenNames));
        sb.append(", familyName='").append(familyName).append('\'');
        sb.append(", timeOfBirth=").append(timeOfBirth);
        sb.append(", titlesByPrecedence=").append(titlesByPrecedence);
        sb.append('}');
        return sb.toString();
    }
}
```
Next the code that uses the `DynamicSupplierBuilder` to create a `Person` supplier.
```
package com.robertboothby.djenni.examples.examples;

import com.robertboothby.djenni.dynamic.DynamicSupplierBuilder;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.robertboothby.djenni.common.NameSupplierBuilder.familyNames;
import static com.robertboothby.djenni.common.NameSupplierBuilder.givenNames;
import static com.robertboothby.djenni.core.SupplierHelper.arrays;
import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static com.robertboothby.djenni.lang.IntegerSupplierBuilder.integerSupplier;
import static com.robertboothby.djenni.time.InstantSupplierBuilder.anInstant;

public class PersonDynamicSupplierBuilderExample {

    /**
     * Provides a pre-configured DynamicSupplierBuilder for a Person with between 1 and 5 random given names, a random family name, and a random date of birth.
     * @return a pre-configured DynamicSupplierBuilder for a Person.
     */
    public DynamicSupplierBuilder<Person> defaultPersonDynamicSupplierBuilder(){
            return DynamicSupplierBuilder.supplierFor(Person.class)
                    .property(Person::getGivenNames,
                            arrays(
                                    givenNames(),
                                    integerSupplier()
                                            .between(1)
                                            .and(6)
                                            .build(),
                                    String[]::new
                            )
                    )
                    .property(Person::getFamilyName, familyNames())
                    .property(Person::getTimeOfBirth, anInstant().build())
                    .property(Person::getTitlesByPrecedence, fix(Collections.EMPTY_LIST));
    }

    public static void main(String[] args) {
        PersonDynamicSupplierBuilderExample personDynamicSupplierBuilderExample = new PersonDynamicSupplierBuilderExample();
        DynamicSupplierBuilder<Person> personDynamicSupplierBuilder = personDynamicSupplierBuilderExample.defaultPersonDynamicSupplierBuilder();
        System.out.println(personDynamicSupplierBuilder.build().stream(10).map(Objects::toString).collect(Collectors.joining("\n")));
    }
}
```
The above example uses one of the ways of defining the suppliers for the properties using lambda function introspection
to identify them. It will first attempt to set the property by matching constructor parameter names before reverting to
using the corresponding setter if available.

The constructor chosen by default will always be the one with the most parameters. If you want to use a different constructor
then you can use the `useConstructor` method to specify the constructor you want to use or you can use the `useFunction` method to give you
even more control where you can supply your own Function to supply the instances. Both of them use an instance of a class
called the `ParameterContext` that captures the parameter names and types of the constructor or function. 

## Advanced Suppliers
Here we will take a look at some of the more advanced capabilities of Djenni suppliers.
### Streamable Supplier
The `StreamableSupplier` is a Djenni specific extension to the basic Java `Supplier` functional interface and provides for the derivation of both streams and other suppliers.
```
package com.robertboothby.djenni.core;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Extension interface to the basic {@link Supplier} interface that adds stream and derive methods.
 * @param <T> The type of values supplied by the Supplier and any streams derived from it.
 */
public interface StreamableSupplier<T> extends Supplier<T> {

    /**
     * Return a limited Stream using this supplier as a source.
     * @param numberOfValues The number of values from this supplier to stream.
     * @return A Stream instance derived from this supplier.
     */
    default Stream<T> stream(long numberOfValues){
        return SupplierHelper.stream(this, numberOfValues);
    }

    /**
     * Return an infinite Stream using this supplier as a source.
     * @return A Stream instance derived from this supplier.
     */
    default Stream<T> stream() {
        return SupplierHelper.stream(this);
    }

    /**
     * Return a new Supplier derived from this supplier using the function passed in.
     * @param derivation The derivation function.
     * @param <R> The type returned from the derivation function.
     * @return A new Supplier derived from this one.
     */
    default <R> StreamableSupplier<R> derive(Function<T, R> derivation){
        return SupplierHelper.derived(derivation, this);
    }

    /**
     * Return a new Supplier derived from this supplier using the function passed in and a second supplier.
     * @param derivation The derivation function.
     * @param otherSupplier The other supplier.
     * @param <R> The type returned from the derivation function.
     * @return A new Supplier derived from this one and the other.
     */
    default <U,R> StreamableSupplier<R> derive(BiFunction<T, U ,R> derivation, Supplier<U> otherSupplier){
        return SupplierHelper.derived(derivation, this, otherSupplier);
    }
}
```
### Thread Local Supplier
Unfortunately not all suppliers will be thread safe (for example consider a supplier interacting with a database connection) so the `ThreadLocalSupplier` class provides a mechanism to provide different instances of the thread unsafe supplier on each thread. 

### International Calendar Builders
Djenni now ships chrono-aware builders that work with any `java.time.chrono.Chronology` visible on the classpath, including the JVM defaults (Thai Buddhist, Japanese, Hijrah, Minguo, etc.) and the extra chronologies contributed by ThreeTen-Extra (Coptic, Ethiopic, Julian, International Fixed…). Use:

- `ChronologyLocalDateSupplierBuilder` for `ChronoLocalDate` values.
- `ChronologyLocalDateTimeSupplierBuilder` for `ChronoLocalDateTime` values.
- `ChronologyZonedDateTimeSupplierBuilder` for `ChronoZonedDateTime` values.
- `ChronologyAppointmentSupplierBuilder` to pair chrono dates with ISO `LocalTime`/`Duration` and emit appointments or intervals.

Each builder offers convenience factories (for example `thaiBuddhistDate()`, `copticDateTime()`, `julianZonedDateTime()`) plus `chronologyById(String)`/`chronologyDateTime(String)` helpers that look up chronologies through `ChronologyCatalog`. That catalog surfaces every discovered calendar and resolves identifiers case-insensitively, so downstream tools can present a complete list without hard coding.

```java
ChronoLocalDateTime<ThaiBuddhistDate> dob =
        ChronologyLocalDateTimeSupplierBuilder.thaiBuddhistDateTime()
                .withZone(ZoneId.of("Asia/Bangkok"))
                .between(ThaiBuddhistDate.of(2500, 1, 1).atTime(12, 0))
                .and(ThaiBuddhistDate.of(2567, 1, 1).atTime(12, 0))
                .build()
                .get();
```

When you need to bias or exclude specific values (for example, suppressing leap day across calendars), compose these builders with existing utilities such as `ExplicitlyBiassedSupplierBuilder`, `SupplierHelper.derived(...)`, or `MonthDaySupplierBuilder#preventLeapDay(boolean)`.

### Caching Suppliers
# TODO Update this section to reflect the new CachingSupplier mechanism.

In some domains there will be scenarios where there are values that need to be consistent within a given set of objects. For example Primary / Foreign Keys or maybe currency in a group of transactions.

The `CachingSupplier` provides a mechanism for handling these scenarios.

A `CachingSupplier` wraps an original supplier and when it is constructed or `next()` is called then it stores the value that has been generated and returns it whenever its own `get()` method is called.

If you use the original supplier directly then the `CachingSupplier` and any linked suppliers derived from it will not see any updates. This is intentional.
```
    public StreamableSupplier<Person> CachingSupplierConfig() {
        //Provide a default configuration immediately.
        CachingSupplier<Name> cachingNameSupplier = CachingSupplier.cacheSuppliedValues(NameSupplierBuilder.names());
        StreamableSupplier<Person> personStreamableSupplier = aPerson(this::epochBirthdate)
                .setGivenNamesSupplier(cachingNameSupplier
                        .derive(Name::getGivenName)
                        .derive(givenName -> new String[]{givenName}))
                .setFamilyNameSupplier(cachingNameSupplier
                        .derive(Name::getFamilyName))
                .build();
        //Ensure that the caching name supplier is updated after every call to get a person.
        return afterGetCalled(personStreamableSupplier, ignored -> cachingNameSupplier.next());
    }
```
The mechanism is based on the `public static <T> StreamableSupplier<T> afterGetCalled(Supplier<? extends T> supplier, Consumer<T> peeker)` that allows you to create a supplier that shares any values it generates with a `Consumer` which can be very useful for logging or capturing the data generated for investigation or replay.

In the example above I have used `whenGetCalled()` to trigger the generation of a new name after each time the `get()` method is called.

You do need to take care to use the `CachingSupplier.next()` only once in a particular context that needs this type of consistency - if you don't then a new value may be generated unexpectedly.

To make it easier to deal with multiple CachingSuppliers concurrently there is a `CachingSupplierRegistry` that allows you to register and coordinate them more easily.

### Collection Suppliers
The `CollectionSupplierBuilder` provides mechanisms over and above the `SimpleListSupplier` to create suppliers that supply specific collection types.
### Map Suppliers
The `MapSupplierBuilder`
### Supplier Helper
Not really a supplier but a useful class when creating your own suppliers as it contains a number of utilities and methods that have proved useful time and again.

## Future Enhancements

Things that I am thinking of adding:
- Logging mechanisms using `peek()` to capture the generated values easily.
- Data Storage & replay mechnisms to allow for scenarios to be re-run using the same data for investigation and bug-fixing purposes.
- SQL Supplier - a mini-framework to access data from a SQL database to feed into the generation other data.
