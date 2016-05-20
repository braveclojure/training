(ns training.solutions.sch1-find-the-gap)

;; Find the smallest natural number not in a given finite set of
;; natural numbers. Suppose the numbers are unsorted:

(def numbers [8 23 9 0 12 11 1 10 13 7 41 4 14 21 5 17 3 19 2 6])

;; How can you find the number in linear time? You can't sort because
;; sorting can't be done in linear time.

(defn minfrom
  [a [len xs]]
  (let [b       (+ a 1 (int (/ len 2)))
        [us vs] (partition-by #(< % b) xs)
        m       (count us)]
    (cond (zero? len)   a
          (= m (- b a)) (recur b [(- len m) vs])
          :else         (recur a [m us]))))

(defn minfree
  [xs]
  (minfrom 0 [(count xs) xs]))
