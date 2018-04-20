<h1>Djenni</h1>
<h2>Test Data Generation Framework</h3>
Djenni is the distillation of many years building diverse systems that all needed testing and as a result needed test data.
<h3>Why Djenni?</h3>
I and a number of my past colleagues came to the realisation that in most testing the exact values in the data were irrelevant and needed only to be relevant and coherent.

In fact my realisation went further and I realised that there is a major unstated assertion in most testing. 

<em>"The test outcome is unaffected by the exact values of most of the data used in the test"</em>

What does this mean?

We should be able to put random values into most of the data elememts in a test and there should be no
change in outcome.

I actually came across a number of occasions where static data helpers were used by tests that produced the same objects
with the same data and when this data was changed many of the tests broke in unexpected ways. Worse I came across code
that passed all the tests fine using the fixed data set but broke completely when the code was used in the real world.

Djenni is designed to make it easy to set up test data for all levels of testing and effectively assert which test data
is irrelevant to the outome and which data matters.

Coincidentally, Djenni makes it easy to generate large volumes of test data for rich domains and can scale well to support
everything from unit testing to large scale non-functional testing.
<h3>The Core Pattern</h3>
Djenni is built around the concept of a &quot;Generator&quot; and a &quot;GeneratorBuilder&quot.

The Generator is the ultimate source of test data. Generators produce the objects required by the tests. These objects
composed of other objects and in the same way most generators are composed of other generators. When a Generator is used
to generate an object, it used generators to generate the object's fields which themselves may compose other genrators and so on.

Generators should typically be immutable so that they are thread safe.

The GeneratorBuilder makes it easy to configure and build instances of its associated Generator. GeneratorBuilders are mutable and are not thread safe.

Once you have used the GeneratorBuilder to configure and build your Generator, you can generate as many instances of
your test object as you want, usually on as many threads as you want.

By implementing your own Genrators and GeneratorBuilders you can rapidly build up a data generation suite for your entire
domain that will make testing a lot easier.

In addition Djenn contains a Maven plugin that can generate a lot of the Generators and GeneratorBuilders for your domain. 