# **RingVector**
Extends Scala2 [`immutable.Vector`](https://www.scala-lang.org/api/current/scala/collection/immutable/Vector.html) with ring (circular) methods.

```scala
"RING".toVector.rotateRight(1) // Vector(G,R,I,N)
```

## How to use
Use the [`RingVector`](/src/main/scala/RingVector.scala) trait to extend classes or objects
where a `Vector` has to be considered circular.

## Need
Whenever data are structured in a circular sequence,
chances are you don't want to locally reinvent the wheel (pun intended).

## Solution
**RingVector** is a small, purely functional, self-contained library,
where most of the circular use cases are already solved
and building blocks provided for the others.

One early approach was to create a dedicated collection.
But differences with `Vector` are few,
so a Scala2 [`implicit class`](https://docs.scala-lang.org/overviews/core/implicit-classes.html) seems a better fit.

## Methods

### Circular version of existing ones
Named as their standard non-circular `Vector` alternatives,
but with an `O` suffix (meaning _ring_).

They are (see [test cases](/src/test/scala/OMethodsSpec.scala)):
* `applyO`
* `segmentLengthO`
* `sliceO`
* `containsSliceO`
* `indexOfSliceO`
* `lastIndexOfSliceO`
* `slidingO`

### Rotation and reflection
Rotate and reflect a circular `Vector`
(see [test cases](/src/test/scala/RotationsReflectionsSpec.scala))

### Symmetry
Calculate rotational and reflectional symmetries of a circular `Vector`
(see [test cases](/src/test/scala/SymmetriesSpec.scala))
