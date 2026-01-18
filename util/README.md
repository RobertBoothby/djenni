# util module

The util module contains shared helper types used across the framework, including interfaces and lambda helpers.

## Key utilities

- `Configurable` and `CopyOnWriteConfigurable` for safe configuration patterns.
- `Nullable` for explicit nullability signaling.
- Introspectable lambda interfaces under `com.robertboothby.djenni.util.lambda.introspectable`.
- `RecordCopyHelper` for convenient record copying.

## RecordCopyHelper

Create modified copies of Java records without manual constructor calls. This targets the canonical constructor and
uses record accessors to identify the component to change.

```java
import static com.robertboothby.djenni.util.RecordCopyHelper.copyOf;

PersonRecord updated = copyOf(original)
    .with(PersonRecord::name, "Grace")
    .with(PersonRecord::age, 33)
    .build();
```

Null handling:
- Null `original` or accessor is rejected.
- Null values are allowed for reference-typed components.
- Null values are rejected for primitive components.

## Introspectable interfaces

These interfaces provide shared, serialization-based introspection helpers for method references:

- `getImplementingClass()` resolves the class containing the referenced method.
- `getMethodName()` returns the referenced method name.
- `serializeToIntrospectableForm()` exposes the underlying `SerializedLambda` (for in-JVM introspection only).

### IntrospectableSupplier

Used for static no-arg or bound instance method references.

- `getReturnType()` returns the referenced method's return type.
- `isStaticMethod()` identifies static no-arg references (for example, `MyType::staticValue`).
- `isBoundInstance()` identifies bound instance references (for example, `instance::value`).
- `getBoundInstance()` returns the captured instance for bound references, which is only meaningful for immediate,
  in-JVM introspection.

### IntrospectableConsumer

Used for static one-arg or bound instance method references.

- `getParameterType()` returns the referenced method's parameter type.
- `isStaticMethod()` identifies static one-arg references (for example, `MyType::acceptValue`).
- `isBoundInstance()` identifies bound instance references (for example, `instance::acceptValue`).
- `getBoundInstance()` returns the captured instance for bound references, which is only meaningful for immediate,
  in-JVM introspection.

### IntrospectableFunction

Used for unbound instance method references that take a target object and return a value (for example, `Person::getName`).

- `getReturnType()` returns the referenced method's return type.

### IntrospectableBiConsumer

Used for unbound instance method references that take a target object plus one argument (for example, `Person::setName`).

- `getParameterType()` returns the referenced method's parameter type.
