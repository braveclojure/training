# mic-check

Check that you're comfortable with the basic of building and running a
Clojure app, and using the REPL. Try these:

* Run `lein run` in the command line. This should print `Testing... testing...`
* Run `lein uberjar`, then `java -jar target/uberjar/mic-check-0.1.0-SNAPSHOT-standalone.jar`. This should print `Testing... testing...`
* In your editor, navigate to `core.clj` and start your REPL. Make sure the REPL is in the `mic-check.core` namespace. Change `core.clj` so that the `-main` function will print  `Is this thing on?`. Then compile the file. In the REPL, type `(-main)` and hit enter. This should print `Is this thing on?`.
