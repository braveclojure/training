(ns valeria.core
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [valeria.odds :as odds])
  (:gen-class))

(def cards (-> "cards.edn" io/resource slurp edn/read-string))

;;=====
;; Odds of hitting card
;;=====


(defn hit-odds
  "If the hit is greater than 6, then it can only be hit via the sum
  of both dice and normal sum odds apply.

  If the hit is 6 or less, then it a single die can also hit. The
  probability of that is 11/36."
  [hit]
  (let [base-odds (odds/sum-odds hit 6 6)]
    (if (<= hit 6)
      (+ base-odds 11/36)
      base-odds)))

(defn card-odds
  "Add up the odds for every possible hit. For example, Butcher hits
  on 11 and 12, so add the odds of an 11 with the odds of a 12."
  [{:keys [hit]}]
  (reduce + (map hit-odds hit)))

;;=====
;; Format card details
;;=====
(defn format-payout
  "String representation of a payout"
  [payout]
  (let [type (first payout)]
    (cond (= type :or)
          (str/join " || " (map format-payout (rest payout)))

          (= type :and)
          (str/join " && " (map format-payout (rest payout)))

          (= type :convert)
          (str "convert " (format-payout (second payout)) " to " (format-payout (last payout)))
          
          (= type :*)
          (str (format-payout (second payout)) " per " (name (last payout)))

          :else
          (str (second payout) " " (name type)))))

;; TODO payout odds
(defn format-card
  [card]
  (str "Name:         " (:name card) "\n"
       "Base cost:    " (:base-cost card) "\n"
       "Hits on:      " (str/join ", " (sort (:hit card))) "\n"
       "You payout:   " (format-payout (:you-payout card)) "\n"
       "Other payout: " (format-payout (:other-payout card)) "\n"
       "Odds:         " (card-odds card) "\n"
       "Role:         " (name (:role card))))

;;=====
;; Format list of cards
;;=====
(defn format-card-row
  [bought index card]
  (str index ". " (:name card)
       " " (:base-cost card)
       "\t\t(" (get bought index 0) ")"))

(defn format-card-table
  [bought]
  (->> cards
       (map-indexed (partial format-card-row bought)) 
       (str/join "\n")))

;;=====
;; Total card cost
;;=====
(defn cost
  [{:keys [base-cost]} n]
  (if n
    (reduce + (range base-cost (+ base-cost n)))
    0))

(defn total-cost
  [cards bought]
  (->> cards
       (map-indexed (fn [index card] (cost card (get bought index))))
       (reduce +)))

;;=====
;; Interact with dashboard
;;=====
(defn get-input
  "Waits for user to enter text and hit enter, then cleans the input.
  If the user doesn't enter anything, it prompts again."
  []
  (let [input (str/trim (read-line))]
    (if (empty? input)
      (do (println "Please enter some input")
          (recur))
      (str/lower-case input))))

(defn parse-number
  [s]
  (Integer. (re-find #"\d+" s)))

(defn buy-card
  "Add 1 to the bought card map for this index.
  Update function has to handle the special case of choosing a card
  for the first time."
  [bought index]
  (update bought index (fn [x] (if x (inc x) 1))))

(defn return-card
  "Like buy card, except subtract 1"
  [bought index]
  (update bought index (fn [x] (if (or (not x) (zero? x)) 0 (dec x)))))

(defn view-card
  [index]
  (println "\n========")
  (-> (get cards index)
      format-card
      println)
  (println "========"))

(defn parse-input
  [bought input]
  (let [command (subs input 0 1)]
    (cond (= command "b") (buy-card bought (parse-number input))
          (= command "v") (do (view-card (parse-number input))
                              bought)
          (= command "r") (return-card bought (parse-number input))
          (= command "reset") {}
          :else (do (println "I don't understand that")
                    bought))))

(defn prompt
  [bought]
  (println "(b)uy, (r)eturn, or (v)iew a card, or (reset). Ex: \"b 0\" to buy cleric")
  (parse-input bought (get-input)))

(defn dashboard
  [bought]
  (loop [bought bought]
    (println (format-card-table bought))
    (println "Total cost: " (total-cost cards bought))
    (let [new-bought (prompt bought)]
      (if new-bought
        (recur new-bought)
        (println "Goodbye!")))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (dashboard {}))
