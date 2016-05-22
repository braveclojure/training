(ns training.exercises.ex07-core-functions)

;; ========================================
;; Data functions
;; ========================================
;; You use these all the time

(map inc [1 2 3 4])
(map :height [{:width 10 :height 20} {:width 30 :height 40}])

(filter even? [1 2 3 4])

(def aria-stark  {:faceless true :name false})
(def sansa-stark {:name "Sansa"})
(filter :name [aria-stark sansa-stark])

(def images [{:dims {:w 10 :h 30}}
             {:dims {:w 23 :h 45}}])
(filter #(> (get-in % [:dims :w])
            20)
        images)

(sort [0 3 5 1])
(reverse (sort [0 3 5 1]))

(sort-by #(get-in % [:dims :w]) images)

(reduce + [1 23 4])
(reduce merge
        {}
        [{:height 10} {:width 20} {:depth 30}])

(first [1 2 3])
(rest [1 2 3])
(conj [1 2 3] 4)
(cons 4 [1 2 3])

(first '(1 2 3))
(rest '(1 2 3))
(conj '(1 2 3) 4)
(cons 4 '(1 2 3))

;; slide time!

;; These functions can all be implemented in terms of first, rest, and
;; cons
(defn filter'
  [pred xs]
  (if (empty? xs)
    xs
    (if (pred (first xs))
      (cons (first xs)
            (filter' pred (rest xs)))
      (filter' pred (rest xs)))))

;; You try:
;; * Use map and filter together: first map a vector, then filter it
;; * Use reduce to select the largest number from a vector
;; * Implement map in terms of first, rest, and cons



;; ========================================
;; Function functions
;; ========================================

;; complement
(def odd?' (complement even?))

(odd?' 1)
(odd?' 2)

;; apply
(max 1 2 3)
(max [1 2 3])
(apply max [1 2 3])
(apply merge [{:width 10} {:height 20}])

;;  partial
(def inc3 (partial + 3))
(inc3 5)

(def default-attrs (partial merge {:width 10 :height 20}))
(default-attrs {:width 15 :depth 30} {:name "Farfarf"})

;; comp
(def strinc (comp str inc))
(strinc 3)

(def first-sample (comp first :samples))
(first-sample {:samples [10.3 5.2]})

;; naive way to implement comp
(defn lousy-comp
  [f2 f1]
  (fn [x] (f2 (f1 x))))
(def occupied? (comp not empty?))
(occupied? [3])
(occupied? [])

;; You try:
;; * Implement complement
;; * Implement partial
;;  Hint: use apply



;; ========================================
;; Laziness
;; ========================================
;; Map & filter are lazy
;; Some functions like reduce are greedy

;; range produces a lazy seq of numbers
(range 10)

(map inc (range 1000000))               ; doesn't actually map yet

(take 10 (map inc (range 1000000)))     ; performs map on first 32

;; infinite sequences

(range) ; infinite sequence of integers starting at 0


;; You try:
;; * Create a lazy sequence of all even numbers
