(ns transform.core
  (:require [clj-time.format :as f]))

(def example-flights
  [{:partingstarttime "08:20:00.00000"
    :partingendtime   "22:00:00.00000"
    :name             "Flight 1"}
   {:partingstarttime "06:00:00.00000"
    :partingendtime   "20:15:00.00000"
    :freqcap          true
    :name             "Flight 2"}])

(def input-date-format
 (f/formatter "HH:mm:ss"))

(def output-date-format
  (f/formatter "hh:mm a"))

(defn format-parting
  "turns something like 23:00:00.00000 into 11:00 PM"
  [time-string]
  (if time-string
    (->> (clojure.string/replace time-string #"\.0+" "")
         (f/parse input-date-format)
         (f/unparse output-date-format))))

(defn format-flight-times
  [flight]
  (-> flight
      (update-in [:partingstarttime] format-parting)
      (update-in [:partingendtime] format-parting)))

(defn add-isfreqcap
  "This appears redundant but unfortunately we needed to do it"
  [flight]
  (if (:freqcap flight)
    (assoc flight :isfreqcap true)
    flight))

(defn process-flight
  [flight]
  (-> flight
      format-flight-times
      add-isfreqcap))

(map process-flight example-flights)
