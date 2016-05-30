# parse

The purpose of this code is to parse CSV, ensuring that it contains
all required fields. It's derived from a real-world project.

To try it out, run `lein run resources/valid.csv` to see what happens
when the CSV is valid, and `lein run resources/invalid.csv` to see
what happens when the CSV is invalid.

To understand it, start with the
[`-main`](https://github.com/braveclojure/training/blob/master/real/parse/src/parse/core.clj)
function and read each of the functions that `-main` calls:
`read-csv`, `find-invalid`, and `report`. Try working with each
function individually in a REPL if you don't understand what it does;
play with it until it makes sense :)

It relies on the
[`clojure.data.csv`](https://github.com/clojure/data.csv) library.
