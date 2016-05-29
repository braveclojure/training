# transform

`core.clj` includes some example real-world code that shows how you
might transform data after pulling it from the database.

The code maps over "flights", records pulled from a database, using
`process-flight` as the mapping function. `process-flight` just
bundles together functions that process a specific aspect of the the
flight, `format-flight-times` and `add-isfreqcap`.

As it is, you can't run this code from the command line, but you can
interact with it in a REPL.
