(ns training.solutions.s03-banned-words
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [clojure.string :as s]))

(defn contains-banned-words?
  [banned s]
  (not-empty (set/intersection banned (set (str/split s #" ")))))
