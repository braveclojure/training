(ns training.exercises.ex11-read-eval-macros)

;; ========================================
;; Read and Eval
;; ========================================

;; The reader reads text to produce a Clojure data structure. When you
;; write Clojure, you're writing text that represents data structures.
(read-string "(+ 1 2)")
(= '(+ 1 2) (read-string "(+ 1 2)"))
(= 3 (read-string "(+ 1 2)"))

;; The evaluator evaluates those data structures
(eval (read-string "(+ 1 2)"))
(eval '(+ 1 2))

;; What's with the '?
map
'map
(quote map)

(map inc [1 2 3])
'(map inc [1 2 3])

;; You can manipulate data structures before they get evald
(eval (list '+ 1 2))

(defn infix
  [expr]
  (let [x  (first expr)
        op (second expr)
        y  (last expr)]
    (list op x y)))

(eval (infix '(1 + 2)))

;; aside: you can use destructuring
(defn infix'
  [[x op y]]
  (list op x y))

;; Macros let you manipulate the data structures emitted by the reader,
;; sending the result to the evaluator
(defmacro infix-macro
  [x op y]
  (list op x y))

(infix-macro 1 + 2)

;; You try:
;; * Write some code to handle postfix evaluation, like:
;;   (eval (postfix 1 2 +))


;; ========================================
;; Eval rules
;; ========================================

;; Data that's not a list or symbol evals to itself:
(eval true)
(eval false)
(eval {})
(eval 1)
(eval #{1 2})
;; empty lists also eval to themselves
(eval ())


;;;;
;; Let's get to know symbols!
;;;;
;; In general, Clojure resolves a symbol by:
;; 
;; 1. Looking up whether the symbol names a special form. If it doesn’t . . .
;; 2. Looking up whether the symbol corresponds to a local binding. If it doesn’t . . .
;; 3. Trying to find a namespace mapping introduced by def. If it doesn’t . . .
;; 4. Throwing an exception

;; if is a special form
(if true :a :b)

(let [x 5]
  (+ x 3))

(def x 15)
x

(let [x 5]
  (let [x 6]
    (+ x 3)))

(defn exclaim
  [exclamation]
  (str exclamation "!"))

(read-string "+")
(type (read-string "+"))
(list (read-string "+") 1 2)
(eval (list (read-string "+") 1 2))

;; Evaling lists
;; function calls
(+ 1 2)

;; special forms
(if true 1 2)

;; Evaling macros
(read-string "(1 + 1)")

;; Why will this fail?
(comment (eval (read-string "(1 + 1)")))

;; You can manipulate the data before evaling it
(let [infix (read-string "(1 + 1)")]
  (list (second infix) (first infix) (last infix)))


;; ========================================
;; Writing macros
;; ========================================

;; Macro anatomy
;; 1. defmacro
;; 2. macro name
;; 3. macro arguments. When the macro is called,
;;   these arguments are unevaluated data.
;; 4. macro body - works exactly like a function body
(defmacro infix-m
  [[x op y]]
  (list op x y))


;; Macros have to return a list. Why doesn't this work?
(defmacro broken-infix
  [[x op y]]
  (op x y))

(broken-infix (1 + 2))
;; This doesn't work because, in the macro body, you're applying `op`
;; to the `x` and `y`, not returning the list '(op x y). The return
;; value of the macro is ('+ 1 2), which attempts to apply the plus
;; _symbol_ to the arguments 1 and 2, and the return value of that is
;; 2. 2 is then passed to the evaluator.
;;
;; Instead, you want the macro to return the list '(+ 1 2) so that the
;; evaluator will handle it correctly.


;; Check macros with macroexpand and macroexpand-1:
(macroexpand-1 '(broken-infix (1 + 2)))
(macroexpand-1 '(when true (pr "when") (pr "true")))

;; simple quoting
(defmacro when'
  "Evaluates test. If logical true, evaluates body in an implicit do."
  {:added "1.0"}
  [test & body]
  (list 'if test (cons 'do body)))



;; ========================================
;; Syntax Quoting
;; ========================================
;; * Uses fully-qualified symbols
;; * Allows unquoting and unquote splicing

;; Fully-qualified symbols
'+
`+

;; recursively syntax quotes all elements
`(+ 1 (- 2 3))

;; You can unquote values
;; When you unqoute something, it's evaluated, and the result is
;; placed in the resulting data structure returned by syntax quote
(def flibbity :a)
`(get {:a 1} flibbity)
`(get {:a 1} ~flibbity)

`(+ 1 (inc 1))
`(+ 1 ~(inc 1))

;; Unquote splicing evaluates a form which should return a sequence,
;; then "unwraps" it
(defmacro wait
  [timeout & body]
  `(do (Thread/sleep ~timeout) ~@body))
(macroexpand-1 '(wait 500
                      (println "waited!")
                      (reduce + [1 2 3])))

;; Without unquote splicing, ~body is a list
(defmacro bad-wait
  [timeout & body]
  `(do (Thread/sleep ~timeout) ~body))
(macroexpand-1 '(bad-wait 500
                          (println "waited!")
                          (reduce + [1 2 3])))
;; expands to:
(comment
  (do (java.lang.Thread/sleep 500)
      ((println "waited!") (reduce + [1 2 3]))))



;; ========================================
;; Macro pitfalls
;; ========================================

;; Variable capture: macro introduces a binding that shadows an
;; existing binding

(def message "Good job!")
;; This macro will shadow `message`
(defmacro with-mischief
  [& stuff-to-do]
  (concat (list 'let ['message "Oh, big deal!"])
          stuff-to-do))

(with-mischief
  (println "Here's how I feel about that thing you did: " message))

(macroexpand-1 '(with-mischief
                  (println "Here's how I feel about that thing you did: " message)))

;; get around this with gensyms
(gensym)
(gensym 'message)

(defmacro without-mischief
  [& stuff-to-do]
  (let [macro-message (gensym 'message)]
    `(let [~macro-message "Oh, big deal!"]
       ~@stuff-to-do
       (println "I still need to say: " ~macro-message))))

(without-mischief
  (println "Here's how I feel about that thing you did: " message))

;; autogensyms are a convenience.
;; All instances of the same autogensym
;; in a syntax quote evaluate to the same symbol
`(blarg# blarg#)

(defmacro without-mischief'
  [& stuff-to-do]
  `(let [message# "Oh, big deal!"]
     ~@stuff-to-do
     (println "I still need to say: " message#)))

(macroexpand-1 '(without-mischief'
                  (println "Here's how I feel about that thing you did: " message)))
;; expands to:
(comment
  (clojure.core/let
      [message__21129__auto__ "Oh, big deal!"]
    (println "Here's how I feel about that thing you did: " message)
    (clojure.core/println "I still need to say: " message__21129__auto__)))

;; ========================================
;; When to use macros?
;; ========================================

;; * When you're beginning, use them whenever you feel like it. Then
;;   try to do the same thing with functions.
;; * Use them when you need new syntax - new evaluation rules not
;;   provided out of the box. Examples:

(-> "Catface Meowmers"
    (clojure.string/lower-case)
    (clojure.string/split #" "))

(comment
  (if-valid
   rest-params validation-map errors
   false
   [true (errors-map errors)]))

;; You try:
;; Remember this?

(comment
  (def character
    {:name "Smooches McCutes"
     :attributes {:intelligence 10
                  :strength 4
                  :dexterity 5}})
  (def c-int (comp :intelligence :attributes))
  (def c-str (comp :strength :attributes))
  (def c-dex (comp :dexterity :attributes)))

;; write a macro, defattrs, which lets you define c-int, c-str, c-dex
;; more succinctly, like this:

(comment
  (defattrs
    c-int :intelligence
    c-str :strength
    c-dex :dexterity)

  (c-int character) ;=> 10
  )


;; Bonus
;; `and` is a macro:

(comment
  (defmacro and
    "Evaluates exprs one at a time, from left to right. If a form
  returns logical false (nil or false), and returns that value and
  doesn't evaluate any of the other expressions, otherwise it returns
  the value of the last expr. (and) returns true."
    {:added "1.0"}
    ([] true)
    ([x] x)
    ([x & next]
     `(let [and# ~x]
        (if and# (and ~@next) and#)))))

;; study it till you understand it, and optionally implement or as a macro
