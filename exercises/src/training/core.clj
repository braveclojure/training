(ns training.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn multiply
  [x y]
  (if (= x 1)
    y
    (+ (multiply (dec x) y) y)))

(defn triangular-number
  [x])

(defn factorial
  [n]
  (if (= n 1)
    1
    (* n (factorial (dec n)))))

(defn factorial
  ([n] (factorial n n))
  ([n acc]
   (if (= n 1)
     acc
     (let [n-1 (dec n)]
       (recur n-1 (* acc n-1))))))
