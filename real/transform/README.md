# transform

`core.clj` includes some example real-world code that shows how you
might transform data after pulling it from the database.

The code maps over "flights", records pulled from a database, using
`process-flight` as the mapping function. `process-flight` just
bundles together functions that process a specific aspect of the the
flight, `format-flight-times` and `add-isfreqcap`.

As it is, you can't run this code from the command line, but you can
interact with it in a REPL.

To understand the code, start with
[line 46](https://github.com/braveclojure/training/blob/master/real/transform/src/transform/core.clj#L46). This
actually applies the `map` function to `example-flights`, transforming
them using the `process-flight` function.

From there, read the
[definition of the `process-flight`](https://github.com/braveclojure/training/blob/master/real/transform/src/transform/core.clj#L40)
function. It takes one argument, a `flight`, and passes it to the
`format-flight-times` function. The result of that is passed to
`add-isfreqcap`

`format-flight-times` modifies two fields in the map, transforming
them from one textual representation of a date to
another. `add-isfreqcap` checks whether the `:freqcap` key of a flight
is truthy, and if it is, associates the flight with the `:isfreqcap
true` key/value pair; otherwise it returns the original flight.


This code relies on the `clj-time` library, which is the most-used
library for dealing with dates and times.
