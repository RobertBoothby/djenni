# Dynamic Supplier Builder Guide

This module houses the infrastructure for building suppliers dynamically from arbitrary Java beans. The
`DynamicSupplierBuilder` inspects constructor parameters and JavaBean setters, associates each property with a
configurable `Parameter`, and uses `StreamableSupplier` instances to generate values at runtime.

## When to Reach for DynamicSupplierBuilder

Use the builder when you want to generate fixtures for simple data classes without writing a bespoke
`SupplierBuilder`. It automatically:

1. Picks the public constructor with the most parameters (or a constructor/function you register explicitly).
2. Records every parameter via `ParameterContext`, preserving discovery order.
3. Adds setter-only properties so write-only attributes can still be populated.
4. Binds each parameter to a supplier sourced from `DefaultSuppliers`, which you can override globally or per property.

This gives you a runnable supplier quickly, while still exposing fluent hooks to customise individual properties.

## Requirements

- Compile target classes with `-parameters` so constructor argument names are available at runtime.
- Ensure setters follow standard JavaBean conventions so the builder can locate them via `Introspector`.

## Configuring Properties

```java
DynamicSupplierBuilder<Person> builder = DynamicSupplierBuilder.supplierFor(Person.class)
        .property(Person::getGivenNames, arrays(givenNames(), intsBetween(1, 6), String[]::new))
        .property(Person::getFamilyName, familyNames())
        .property(Person::setValueThree, fix("override"));

StreamableSupplier<Person> supplier = builder.build();
```

Key APIs:

- `.property(getter, Supplier)` or `.property(setter, Supplier)` binds a supplier directly.
- `.property(getter, SupplierBuilder)` provides deferred construction via another builder.
- `.useConstructor(Function<ParameterContext,C>)` or `.useFunction(Function<ParameterContext,C>)` lets you choose a
  different instantiation path while still recording parameters.

All property calls ultimately manipulate `Parameter` instances captured during introspection. At build time those
parameters are copied so each supplier has its own snapshot of the configuration.

## Default Suppliers & ParameterContext

Every `Parameter` starts with a supplier retrieved from `DefaultSuppliers`. There are three tiers of lookup:

1. Class + property name
2. Class level fallback
3. A null-supplying sentinel (`UseDefaultValueSupplier`) indicating "leave the bean's default" for setter-only fields.

The `ParameterContext` used during introspection captures name overrides via `p(String, Class)` and lets you supply sample
values or `StreamableSupplier`s when constructor arguments cannot be null.

At runtime, `RuntimeParameterContext` replays those parameters in order, delegating to each supplier in turn to obtain the
value passed to the constructor or setter.

## Resetting to Bean Defaults

If you previously overrode a property but later want to revert to the bean's inherent default value, call one of:

- `clearProperty(setter)` – reinstalls the default supplier that the builder chose during introspection.
- `useDefaultValue(setter)` – explicitly configures the property to use the sentinel `UseDefaultValueSupplier`, which
  causes setter invocation to be skipped unless you supply a concrete value later.

Both APIs avoid writing `null` into fields that already have meaningful defaults inside the class.

## Customising Global Defaults

`DefaultSuppliers` is a singleton registry. You can register new defaults globally:

```java
defaultSuppliers().setClassSupplier(String.class, fix("STRING"));
defaultSuppliers().setClassAndPropertySupplier(Integer.class, "valueTwo", fix(42));
```

These defaults feed directly into new `DynamicSupplierBuilder` instances so teams can centralise repeatable fixtures.

## Putting It Together

```java
DynamicSupplierBuilder<TestClass> builder = DynamicSupplierBuilder.supplierFor(TestClass.class)
        .property(TestClass::getValueOne, fix("one"))
        .property(TestClass::setValueThree, SupplierHelper.nullSupplier()); // allow null override

StreamableSupplier<TestClass> supplier = builder.build();

TestClass first = supplier.get();      // valueThree == null
TestClass second = supplier.get();     // valueThree == null

// Revert to bean default for subsequent instances without re-introspecting
builder.useDefaultValue(TestClass::setValueThree);
StreamableSupplier<TestClass> defaultingSupplier = builder.build();

TestClass third = defaultingSupplier.get();      // valueThree == bean default ("" in the tests)
```

Suppliers produced by the builder are immutable snapshots of the current configuration. You can safely keep using an
existing supplier while reconfiguring the same builder to produce a new one.

## Related Types

- `DefaultSuppliers` / `DefaultSuppliersImpl` – registry for fallback suppliers per class/property.
- `UseDefaultValueSupplier` – sentinel supplier representing "leave bean default".
- `Parameter`, `ParameterContext`, `IntrospectionParameterContext`, `RuntimeParameterContext` – capture and replay
  parameter metadata and suppliers.

For further background, see the Dynamic Supplier section in the top-level `core/readme.md` or continue exploring the
package-level documentation below.
