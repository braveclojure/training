(ns training.exercises.ex01-total-word-count
  (:require [clojure.string :as s]))

(defn total-words
  [x]
  (count (s/split x #" ")))
