(ns blackjack.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.set :as set]))

;; Arbitrary amount of starting money
(def starting-money 1000)

;; Use nested loops to iterate over suits and ranks to generate a deck
(def deck
  "A deck of cards"
  (loop [suits ["♠" "♥" "♦" "♣"]
         deck  #{}]
    (if (empty? suits)
      deck
      (let [[suit & remaining-suits] suits]
        (recur remaining-suits
               (loop [ranks ["A" 2 3 4 5 6 7 8 9 10 "J" "Q" "K"]
                      deck' deck]
                 (if (empty? ranks)
                   deck'
                   (let [[rank & remaining-ranks] ranks]
                     (recur remaining-ranks
                            (conj deck' [suit rank]))))))))))

(def initial-game
  "The starting state of the game"
  {:player {:money starting-money
            :bet 0
            :hand []}
   :dealer {:hand []}
   :deck deck
   :outcome nil})

;; This is used when calculating the value of a hand
(def ten-cards
  "A card value in this set is worth 10 points"
  #{10 "J" "Q" "K"})


(defn get-input
  "Waits for user to enter text and hit enter, then cleans the input.
  If the user doesn't enter anything, it prompts again."
  []
  (let [input (str/trim (read-line))]
    (if (empty? input)
      (do (println "Please enter some input")
          (recur))
      (str/lower-case input))))

;;=====
;; Bets
;;=====
;; Notice that we separate the impure function, prompt-bet, from the
;; pure function, place-bet. In functional programming languages, we
;; try to write pure functions as much as possible, and reduce the
;; footprint of impure functions.

(defn place-bet
  "Transfer from player's money pile to bet pile"
  [game bet]
  (-> game
      (update-in [:player :money] - bet)
      (update-in [:player :bet] + bet)))

(defn prompt-bet
  "Prompts user for a bet, then updates game state"
  [game]
  (println "Please place a bet by entering a number:")
  (place-bet game (Integer. (get-input))))

;;=====
;; Dealing
;;=====
(defn random-card
  "select a random card from the deck"
  [deck]
  (get (vec deck) (rand-int (count deck))))

(defn deal-to
  "Remove a card from the deck and give it to someone"
  [card-receiver-path {:keys [deck] :as game}]
  (let [card (random-card deck)]
    (-> game
        (update-in card-receiver-path conj card)
        (update :deck disj card))))

(def deal-player (partial deal-to [:player :hand]))
(def deal-dealer (partial deal-to [:dealer :hand]))

(defn deal
  [game]
  (-> game
      deal-player
      deal-dealer
      deal-player
      deal-dealer))

(defn player-hand
  [game]
  (get-in game [:player :hand]))

(defn dealer-hand
  [game]
  (get-in game [:dealer :hand]))


;;=====
;; Hand outcomes
;;=====
(defn outcome
  "Assign an outcome"
  [game x]
  (assoc game :outcome x))

;;=====
;; Handling naturals
;;=====
;; A natural is when the player or dealer has an ace and a 10-card

(defn natural-hand?
  "Does a hand contain an ace and a 10. Example hand:
  [[\"♠\" 3] [\"♠\" 10]]"
  [hand]
  ;; Remember that you can use sets as functions. When you apply a set
  ;; to a value, it returns that value if it contains that
  ;; value. Otherwise it returns nil.
  (let [ranks (set (map second hand))]
    (and (ranks "A")
         (not-empty (set/intersection ranks ten-cards)))))

(defn handle-naturals
  "Assign outcome based on whether both have naturals, just one, or
  neither"
  [game]
  (let [player-natural? (natural-hand? (player-hand game))
        dealer-natural? (natural-hand? (dealer-hand game))]
    (cond (and player-natural? dealer-natural?) (outcome game :tie)
          player-natural? (outcome game :player-won-natural)
          dealer-natural? (outcome game :player-lost)
          :else game)))

;;=====
;; Check for busts
;;=====
(defn numberify-card-rank
  "Convert non-numeric cards to a number"
  [rank]
  (cond (ten-cards rank) 10
        (= rank "A") 11
        :else rank))

(defn best-hand-score
  "Gets as close as possible to 21 without going over"
  [hand]
  (let [ranks (map (comp numberify-card-rank second) hand)
        initial-sum (reduce + ranks)]
    (loop [sum initial-sum
           ranks ranks]
      ;; This bit checks if you've busted because you're treating an
      ;; ace as 11. If so, get the score with one ace converted to a 1.
      (if (and (> sum 21) (contains? (set ranks) 11))
        (recur (- sum 10) (conj (->> ranks sort reverse rest) 1))
        sum))))

(defn busted?
  [hand]
  (> (best-hand-score hand) 21))

(defn check-player-bust
  [game]
  (if (busted? (player-hand game))
    (outcome game :player-busted)
    game))

(defn check-dealer-bust
  [game]
  (if (busted? (dealer-hand game))
    (outcome game :dealer-busted)
    game))

;;=====
;; Displaying game
;;=====
;; Notice that all the format functions return strings and don't do
;; any printing. This is part of the general approach of separting out
;; impure functions as much as possible.

(defn format-hand
  [hand]
  (->> hand
       (map #(str/join "" %))
       (str/join ", ")))

(defn format-dealer-hand
  "Special rules for the dealer hand - you don't want to reveal his
  hidden card or his score until after the player plays"
  [hand reveal-full]
  (if reveal-full
    (str (format-hand hand) " (" (best-hand-score hand) ")")
    (format-hand (rest hand))))

(defn format-game
  [game reveal-dealer]
  (let [ph (player-hand game)]
    (str "-----\n"
         "Your hand: " (format-hand ph) " (" (best-hand-score ph) ")\n"
         "Dealer hand: " (format-dealer-hand (dealer-hand game) reveal-dealer) "\n"
         "Bet: " (get-in game [:player :bet]) "\n"
         "-----\n")))

(defn print-game
  [game & [reveal-dealer]]
  (-> game
      (format-game reveal-dealer)
      println))


;;=====
;; Play additional cards
;;=====
(defn player-play
  "Prompt player to hit or stand, check for player bust"
  [game]
  ;; Check that there's no outcome from the natural check
  (when (not (:outcome game))
    (loop [game game]
      (print-game game)
      (if (:outcome game)
        game
        (do (println "(h)it or (s)tand?")
            (let [response (get-input)]
              (if (= "h" response)
                (recur (check-player-bust (deal-player game)))
                (check-player-bust game))))))))

(defn dealer-play
  "Keep giving the dealer cards until his hand exceeds 17"
  [game]
  (if (not (:outcome game))
    (loop [game game]
      (if (>= (best-hand-score (dealer-hand game)) 17)
        (check-dealer-bust game)
        (recur (deal-dealer game))))
    game))

;;=====
;; Final outcome
;;=====
(defn determine-outcome
  "If the outcome wasn't already settled with naturals, compare scores"
  [game]
  (let [dealer-score (best-hand-score (dealer-hand game))
        player-score (best-hand-score (player-hand game))]
    (cond (:outcome game) game
          (> dealer-score player-score) (outcome game :player-lost)
          (> player-score dealer-score) (outcome game :player-won)
          :else (outcome game :tie))))

(def outcomes
  {:player-busted "You busted! You lost!"
   :dealer-busted "The dealer busted! You won!"
   :tie           "It was a tie!"
   :player-won-natural "You won a natural!"
   :player-lost   "You lost!"
   :player-won    "You won!"})

(defn print-outcome
  [game]
  (print-game game true)
  (println ((:outcome game) outcomes))
  game)

(defn print-money
  [game]
  (println "You have " (get-in game [:player :money]) " money")
  game)

(defn pay-player
  [game multiplier]
  (update-in game [:player :money] + (* multiplier (get-in game [:player :bet]))))

(defn tie
  "Return the player's bet"
  [game]
  (pay-player game 1))

(defn player-won-natural
  "Return the player's bet, plus 1.5x the bet"
  [game]
  (pay-player game 2.5))

(defn player-won
  "Return the player's 2x the player's bet"
  [game]
  (pay-player game 2))

(defn settle-outcome
  [{:keys [outcome] :as game}]
  (cond (#{:player-lost :player-busted} outcome) game
        (#{:player-won :dealer-busted} outcome) (player-won game)
        (= outcome :player-won-natural) (player-won-natural game)
        (= outcome :tie) (tie game)))

(defn round
  [game]
  (-> game
      prompt-bet
      deal
      handle-naturals
      player-play
      dealer-play
      determine-outcome
      print-outcome
      settle-outcome
      print-money))

(defn game-loop
  "Wraps game rounds, prompting to continue after each round and
  resetting state such that players have no cards, the deck is full,
  and the player's money is retained"
  [game]
  (print-money game)
  (loop [game game]
    (let [game-result (round game)]
      (println "Continue? y/n")
      (if (= (get-input) "y")
        (recur (assoc-in game [:player :money] (get-in game-result [:player :money])))
        (do (println "Bye!")
            (System/exit 0))))))

(defn -main
  []
  (game-loop initial-game))
