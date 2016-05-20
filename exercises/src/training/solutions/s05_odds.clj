(ns training.solutions.s05-odds)

(defn sums
  [d1 d2]
  (loop [d1' d1
         pairs []]
    (if (= d1' 0)
      pairs
      (recur (dec d1')
             (loop [d2' d2
                    pairs' pairs]
               (if (= d2' 0)
                 pairs'
                 (recur (dec d2')
                        (conj pairs' (+ d1' d2')))))))))

(defn odds
  [sum d1 d2]
  (let [sums (sums d1 d2)]
    (/ (count (filter #(= sum %) sums)) (* d1 d2))))
