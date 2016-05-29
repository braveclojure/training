# parse

The purpose of this code is to parse CSV, ensuring that it contains
all required fields. It's derived from a real-world project.

To try it out, run `lein run resources/valid.csv` to see what happens
when the CSV is valid, and `lein run resources/invalid.csv` to see
what happens when the CSV is invalid.

It relies on the
[`clojure.data.csv`](https://github.com/clojure/data.csv) library.
