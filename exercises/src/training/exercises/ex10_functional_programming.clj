(ns training.exercises.ex10-functional-programming
  (:require [training.exercises.ex08-movie-time :as mt]
            [clojure.string :as str]
            [clojure.string :as s]))

;; ========================================
;; Pure functions
;; ========================================

;; no side effects
(+ 1 3)

;; associng a map doesn't change the original map
(let [x {:name "Dr. Jekyll"}]
  (println (assoc x :name "Mr. Hyde"))
  (println x))


;; referential transparency
(clojure.string/upper-case "bob loblaw's law blog")

;; not referentially transparent
(rand-int 10)



;; ========================================
;; Immutability
;; ========================================
;; * Main data types are immutable (vectors, maps, lists, sets)

;; adding an element to a vector doesn't change it
(let [x [1 2 3]]
  (println (conj x 4))
  (println x))



;; ========================================
;; Recursion instead of loop/for
;; ========================================

(defn sum
  ([vals]
     (sum vals 0))
  ([vals accumulating-total]
     (if (empty? vals)
       accumulating-total
       (recur (rest vals) (+ (first vals) accumulating-total)))))

;; Recursive call, step-by-step
(sum [39 5 1]) ; single-arity body calls two-arity body
(sum [39 5 1] 0)
(sum [5 1] 39)
(sum [1] 44)
(sum [] 45) ; base case is reached, so return accumulating-total
; => 45

;; map, reduce, etc all recursive
(reduce + [39 5 1])

;; multiple seq operations to control result
;; total number of votes for all movies of n rating
(reduce +
        (map :votes
             (filter #(= 8.0 (:rating %)) mt/movies)))



;; ========================================
;; Function composition instead of attribute mutation
;; ========================================

;; Pass a value through a chain of functions
;; reusable: can be used on any string
(defn clean
  [text]
  (str/replace (str/trim text) #"lol" "LOL"))

(clean "My boa consitrctor is so sassy lol!")

;; Function composition also used to traverse a structure
(:title (first mt/movies))

(def first-title (comp :title first))

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})
(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

(c-int character)
(c-str character)
(c-dex character)



;; ========================================
;; Make it more readable with threading
;; ========================================
;; Also known as the stabby macro

;; First-position threading; equivalent to `clean` above
(-> "My boa consitrctor is so sassy lol!"
    s/trim
    (str/replace #"lol" "LOL"))

;; Last-position threading; notice two `>`
(filter even? (map inc [1 2 3]))
;; threaded:
(->> [1 2 3]
     (map inc)
     (filter even?))


;; You try: re-write this using threading
(reduce +
        (map :votes
             (filter #(= 8.0 (:rating %)) mt/movies)))



;; ========================================
;; More functional programming
;; ========================================

;; Let's update Smooches McCutes, increment strength
(assoc-in character
          [:attributes :strength]
          (inc (get-in character [:attributes :strength])))

(update-in character [:attributes :strength] inc)

(assoc-in character
          [:attributes :strength]
          (+ (get-in character [:attributes :strength]) 2))

(update-in character [:attributes :strength] + 2)

;; Also works on vectors
(update-in [5 6] [0] inc)

;; Here's how you could implement update-in.
(defn update-in'
  [m path f & args]
  (let [current (get-in m path)
        new     (apply f current args)]
    (assoc-in m path new)))

;; Because Clojure focuses on using a small set of data abstractions,
;; you end up with a lot of small, widely-apaplicable utility
;; functions like this.

;; Returns sub-collections, splitting each time odd? returns a new
;; value
(partition-by odd? [1 1 1 2 2 3 3])

(partition-by count ["a" "b" "ab" "ac" "c"])



;; ========================================
;; Programming to abstractions
;; ========================================

;; The seq abstraction
(first [1 2 3])
(first '(1 2 3))
(first #{1 2 3})
(first {:a 1 :b 2 :c 3})

(rest [1 2 3])
(rest '(1 2 3))

(filter odd? [1 2 3])
(filter odd? #{1 2 3})
;; sometimes you have to convert back
(set (filter odd? #{1 2 3}))

;; The associative abstraction
(get {:a 1} :a)
(get [:a :b] 1)

(assoc {:a 1} :a 2)
(assoc [:a :b] 1 :c)

;; The collection abstraction
(count [1 2 3])
(count #{1 2 3})

(empty? {})

(some odd? [1 2 3])
(some odd? [2 2 2])

(every? odd? [1 2 3])
(every? odd? #{1 3})

(distinct? [1 2 3])
(distinct? {:a 1 :b 2})
(distinct [1 2 2])
