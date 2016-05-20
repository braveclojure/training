(ns training.solutions.s07-core-functions)

(filter even? (map inc [0 1 2 3]))

(reduce (fn [x y] (if (> x y) x y))
        [0 1 2 3 4 5])

(reduce max [0 1 2 3 4 5])

(defn map'
  [f xs]
  (if (empty? xs)
    xs
    (cons (f (first xs))
          (map' f (rest xs)))))

(defn complement'
  [f]
  (fn [& args] (not (apply f args))))

(defn partial'
  [f & partial-args]
  (fn [& args]
    (apply f (concat partial-args args))))
