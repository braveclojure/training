(ns valeria.cards
  (:require [valeria.core :as core]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def citizens (-> "citizens.edn" io/resource slurp edn/read-string))
(def monsters (-> "monsters.edn" io/resource slurp edn/read-string))

(defprotocol Card
  (format [x] "Produce a string meant for human eyes describing the card"))

(defrecord Citizen [name base-cost hit you-payout other-payout role]
  Card
  (format [this]
    (str "Name:         " name "\n"
         "Base cost:    " base-cost "\n"
         "Hits on:      " (str/join ", " (sort hit)) "\n"
         "You payout:   " (core/format-payout you-payout) "\n"
         "Other payout: " (core/format-payout other-payout) "\n"
         "Odds:         " (core/card-odds this) "\n"
         "Role:         " (name role))))
