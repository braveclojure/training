(ns character-creator.core
  (:gen-class))

(def initial-state
  {:name "Smooches McCutes"
   :attributes {:strength 10
                :intelligence 10
                :charisma 10
                :dexterity 10}
   :attribute-points 5})

(defn format-character
  "Given a map representing a character, return a string that
  describes it in a way that's easy for humans to read"
  [{:keys [name attributes attribute-points]}]
  (str "Name: " name "

Attributes
STR: " (:strength attributes) "
INT: " (:intelligence attributes) "
CHR: " (:charisma attributes) "
DEX: " (:dexterity attributes) "

Remaining attribute points: " attribute-points))

(defn assign-attribute-point
  "Updates the character map, adding one to the chosen attribute and
  deducting one from remaining attribute points"
  [character attr]
  (-> character
      (update-in [:attributes attr] inc)
      (update :attribute-points dec)))

;;;
;; Handle input
;;;

(def input-map
  "Maps the user's input, a string, to the corresponding attribute key"
  {"s" :strength
   "i" :intelligence
   "c" :charisma
   "d" :dexterity})

(defn get-input
  "Waits for user to enter text and hit enter, then cleans the input"
  ([] (get-input ""))
  ([default]
   (let [input (clojure.string/trim (read-line))]
     (if (empty? input)
       default
       (clojure.string/lower-case input)))))

(defn prompt-attribute-point
  "Prints a prompt and adds an attribute point based on input."
  [character]
  (println (format-character character))
  (println "Assign an attribute point to (s)tr, (i)nt, (c)hr, or (d)ex:")
  (let [choice (get-input)
        attr (get input-map choice)]
    (if attr
      (assign-attribute-point character attr)
      (do (println "I don't recognize that choice")
          (recur character)))))

;; Notice that this uses recur to continue prompting for point
;; assignment. We're updating the character, building up a final
;; value, using recursion.
(defn builder-loop
  "Continue building a character until there are no more attribute points"
  [character]
  (if (zero? (:attribute-points character))
    (do (println (str "\n\nYour final character:\n" (format-character character)))
        (System/exit 0))
    (recur (prompt-attribute-point character))))

(defn -main
  [& args]
  (println "Welcome to your character builder!")
  (builder-loop initial-state))
