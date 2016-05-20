(ns training.exercises.ex01-total-word-count
  (:require [clojure.string :as s]))

;; Given a string, count the total number of words it has
;; Example:

;; (total-words "this has four words")
;; ;=> 4

;; hint: use s/split with the regex #" " to split the string on
;; spaces, like this:
(s/split "test string" #" ")

(defn total-words
  [x]
  )
