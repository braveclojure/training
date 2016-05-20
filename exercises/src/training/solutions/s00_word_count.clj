(ns training.solutions.s00-word-count
  (:require [clojure.string :as s]))

(def test-string "Owwwww! My head feels like it's... like it's gonna have a baby.")

;; contains error because inc 
(defn word-count
  [s]
  (reduce (fn [counts word] (update counts (s/lower-case word) inc))
          {}
          (s/split s #"[\. !]+")))

(defn word-count
  [s]
  (reduce (fn [counts word] (update counts (s/lower-case word) (fnil inc 0)))
          {}
          (s/split s #"[\. !]+")))
