(ns training.exercises.ex15-schema
  (:require [schema.core :as s]))

;; ========================================
;; Schema
;; ========================================
;; Clojaure is a dynamically typed languaged with limited support for
;; type checking by default. Schema helps fill this role. It doesn't
;; do type checking per se, but it helps you describe the shape of
;; data that functions expect and it validates that shape.
;; 
;; What's "the shape of data"? Simple example:

(def Employee
  {:name s/Str
   :department s/Str
   :id s/Int
   :sanity s/Int})


(s/validate Employee {:name "Eagle Jones"
                      :department "Fraud Prevention"
                      :id 3
                      :sanity 5})

;; This throws an exception because it's missing a key
(comment
  (s/validate Employee {:name "Henry Sock, Jr."
                        :department "Fraud Prevention"
                        :sanity 10}))

;; use s/check to get data describing the problem
(s/check Employee {:name "Henry Sock, Jr."
                   :department "Fraud Prevention"
                   :sanity 10})

;; You can nest validations
(def Employee2
  {:name s/Str
   :department s/Str
   :address {:street1 s/Str
             (s/optional-key :street2) s/Str
             :city s/Str
             :zip s/Str
             :state (s/enum :nc :sc :va)}
   :sanity s/Int
   :id s/Int})

(def valid-employee
  {:name "Eagle Jones"
   :address {:street1 "123 Sycamore"
             :city "McLean"
             :zip "12345"
             :state :va}
   :sanity 5
   :department "Fraud Prevention"
   :id 3})

(def employee-invalid-address
  {:name "Eagle Jones"
   :department "Fraud Prevention"
   :address {:street1 "123 Sycamore"}
   :sanity 5
   :id 3})

(s/check Employee2 employee-invalid-address)

(comment
  (s/validate Employee2 {:name "Eagle Jones"
                         :department "Fraud Prevention"
                         :sanity 5
                         :id 3})
  (s/validate Employee2 employee-invalid-address))
(s/validate Employee2 valid-employee)

;; You can compose validations because they're just data
(def Address
  {:street1 s/Str
   (s/optional-key :street2) s/Str
   :city s/Str
   :zip s/Str
   :state (s/enum :nc :sc :va)})

(def Employee3
  {:name s/Str
   :department s/Str
   :address Address
   :sanity s/Int
   :id s/Int})

;; Schema has macros that let you define functions and records with
;; schema validation
;; Notice the `s/` namespace prefix
(s/defn give-raise
  [employee :- Employee3]
  :ok)

(s/with-fn-validation
  (give-raise valid-employee))

(comment
  (s/with-fn-validation
    (give-raise employee-invalid-address)))

;; You try: 
;; Create schemas for the Citizen and Monster Valeria cards.
;; The tricky bit will probably be the :you-payout and :other-payout.
;; Schema has great documentation: https://github.com/plumatic/schema
