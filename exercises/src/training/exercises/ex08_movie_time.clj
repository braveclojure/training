(ns training.exercises.ex08-movie-time
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

;; This function includes some stuff we haven't larned yet; don't
;; worry about that for now. We only need to use this to load the
;; movie data.
(defn get-movies
  []
  (->> (io/resource "ex08-movie-time/ratings.list")
       slurp
       str/split-lines
       (map #(str/split % #"\s{2,}"))
       (map (fn [[_ _ votes rating title]]
              {:votes  (Integer. votes)
               :rating (Double. rating)
               :title  (str/replace title #"\"" "")}))))

(def movies (get-movies))
;; Each movie is a map like:
{:votes  4130005
 :rating 8.3
 :title  "Tears of a Clown"}


;; Exercises:
;; * Get the highest ratest movie with more than 1000 votes.
;;   Extra credit: to break a tie, select the title with more votes.
;; * Get all movies rated above x with at least y votes, sorted by rating.
;    WARNNIG: make sure to use a high minimum vote count or your REPL will freeze
;;   while it tries to  print all the results
;; * Get a count of all movies rated above x and below y with at least z votes
;; * Calculate the average movie rating across all movies

;; Tips:
;; * Don't just type `movies` in your REPL, it will attempt to
;    print all of them and there are thousands
;; * However, you can interact with the movies data with functions
;;   like `(first movies)`, `(count movies)` 
