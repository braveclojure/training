(ns training.solutions.s08-movie-time
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

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

(defn max-by
  [f x y]
  (if (> (f x) (f y)) x y))

(defn highest-rated
  []
  (reduce (fn [x y]
            (let [rx (:rating x)
                  ry (:rating y)]
              (if (= rx ry)
                (max-by :votes x y)
                (max-by :rating x y))))
          (filter #(> (:votes %) 1000) movies)))

(defn select-movies
  [min-rating max-rating min-votes]
  (sort-by :rating
           (filter (fn [{:keys [rating votes]}]
                     (and (> rating min-rating)
                          (< rating max-rating)
                          (> votes min-votes)))
                   movies)))

(defn vote-average
  []
  (let [totals (reduce (fn [{:keys [total-votes ratings]} {:keys [votes rating]}]
                         {:total-votes (+ total-votes votes)
                          :ratings (+ ratings (* votes rating))})
                       {:total-votes 0
                        :ratings 0}
                       movies)]
    (/ (:ratings totals) (:total-votes totals))))
