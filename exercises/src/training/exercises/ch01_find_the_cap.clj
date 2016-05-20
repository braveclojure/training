(ns training.exercises.ch01-find-the-cap)

;; Find the smallest natural number not in a given finite set of
;; natural numbers. Suppose the numbers are unsorted:

(def numbers [8 23 9 0 12 11 1 10 13 7 41 4 14 21 5 17 3 19 2 6])

;; How can you find the number in linear time? You can't sort because
;; sorting can't be done in linear time.
