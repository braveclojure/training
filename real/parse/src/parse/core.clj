(ns parse.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io])
  (:gen-class))

(defn report
  [errs]
  (if (empty? errs)
    "No errors!"
    (str "Errors at these rows: " (clojure.string/join ", " errs))))

(def data-indices
  "These columns must not be empty"
  [0 1 3 4])

(defn row-valid?
  "Check that each column in data-indices is not empty"
  [row]
  (every? #(not-empty (get row %))
          data-indices))

(defn find-invalid
  [rows]
  (->> rows
       (map-indexed vector)
       (reduce (fn [errs [index row]]
                 (if (row-valid? row)
                   errs
                   (conj errs index)))
               [])))

(defn read-csv
  [path]
  (with-open [file (io/reader path)]
    (doall (csv/read-csv file))))

(defn -main
  "I don't do a whole lot ... yet."
  [file-path]
  (-> file-path
      read-csv
      find-invalid
      report
      println))
