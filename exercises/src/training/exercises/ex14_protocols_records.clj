(ns training.exercises.ex14-protocols-records
  (:require [cheshire.core :as json]))

;; ========================================
;; Protocols
;; ========================================

;; Protocols define abstractions, or collections of related
;; behavior. Clojure lets you extend existing types to implement these
;; abstractions.


;; We want to handle different data types, e.g. strings and maps,
;; differently. Protocols to the rescue!
(defprotocol Serializable
  (serialize [this] "Converts this into a string, ready to send as an SQS message."))

(extend-protocol Serializable
  String (serialize [s] s)
  clojure.lang.PersistentHashMap (serialize [m] (json/generate-string m)))

;; Clojure's `ring` library lets you easily specify responses for HTTP
;; requests. Responses can be strings, files, and even other data
;; types. Another place where protocols are appropriate.
;;
;; The example below calculates an etag for use in HTTP response
;; headers. Browsers use etags to determine whether or note an asset
;; has expired.
(defprotocol ExpirableContent
  (etag [x] "calculate etag"))

(defn sha1
  [s]
  "actually do sha1")

(extend-protocol ExpirableContent
  String
  (etag [s] (sha1 s))

  java.io.File
  (etag [x] (str (.lastModified x) "-" (.length x)))

  ;; Default case
  java.lang.Object
  (etag [x] nil))



;; ========================================
;; Records
;; ========================================
;; Records are custom, performant, map-like data types

(defrecord WereWolf [name title])

;; After defining a record type, there are three different ways to
;; create instances of that record
(def lucian (WereWolf. "David" "London Tourism"))
(def jacob  (->WereWolf "Jacob" "Lead Shirt Discarder"))
(def lucian (map->WereWolf {:name "Lucian" :title "CEO of Melodrama"}))

;; You can look up record values the same way you look up map values,
;; and you can also use Java field access interop:
(.name lucian)
(:name lucian)
(get lucian :name)


;; When testing for equality, Clojure will check that all fields are
;; equal and that the two comparands have the same type:
(= jacob (->WereWolf "Jacob" "Lead Shirt Discarder"))
; => true

(= jacob (WereWolf. "David" "London Tourist"))
; => false

(= jacob {:name "Jacob" :title "Lead Shirt Discarder"})
; => false

;; Any function you can use on a map, you can also use on a record:
(assoc jacob :title "Lead Third Wheel")

;; However, if you dissoc a field, the result’s type will be a plain
;; ol’ Clojure map; it won’t have the same data type as the original
;; record:
(dissoc jacob :title)


;; You can extend records to implement protocols
(defprotocol WereCreature
  (full-moon-behavior [this] "What the creature does on a full moon"))

;; extend-type is another way to implement protocols
(extend-type WereWolf
  WereCreature
  (full-moon-behavior [this] (str name " will howl and murder")))
(full-moon-behavior jacob)
;; where did `name` come from?


;; You can also extend records when you define them
(defrecord WereSimmons [name fabulosity]
  WereCreature
  (full-moon-behavior [this]
    (str name " will sweat to the oldies with a fabulosity rating of " fabulosity)))

(def richard (WereSimmons. "Richard" 11))
(full-moon-behavior richard)


;; You try:
;; * create a protocol for formatting a Valeria card
;; * create two records, Citizen and Monster that implement that protocol
;; * read the files under projects/valeria/resources/citizens.edn and
;;   monsters.edn. Convert those maps to records and format one citizen
;;   and one monster.

;; The following code should work:
(comment
  (format-card (map->Citizen {:name "Cleric"
                              :base-cost 3
                              :hit #{1}
                              :you-payout   [:magic 3]
                              :other-payout [:magic 1]
                              :role :holy})))

;; Notes:
;; * You can reuse code from your previous Valeria project
;; * Theres an implementation under projects/valeria/src/valeria/cards.clj
;;   but there's definitely room for improvement :)
