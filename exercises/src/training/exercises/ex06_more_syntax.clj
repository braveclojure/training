(ns training.exercises.ex06-more-syntax)

;; ========================================
;; Exceptions
;; ========================================

(try (/ 1 0)
     (catch Exception e
       (str "caught exception: " (.getMessage e))))


;; ========================================
;; Destructuring
;; ========================================
;; Destructuring is incredibly useful, but it's not absolutely
;; necessary so I'm not going to spend much time on it. There's an
;; excellent guide at http://clojure.org/guides/destructuring

;; positional destructuring
(def numbers [1 2 3 4 5])
(let [[a b] numbers]
  a)

(let [[a b] numbers]
  b)

(let [[a b] numbers]
  (+ a b))

(let [[a b & c] [1 2 3 4 5]]
  c)

;; map destructuring
(def image {:width 10 :height 30})
(let [{:keys [width height]} image]
  (* width height))


(defn format-coords
  [{:keys [lat lng] :as point}]
  (str "lat " lat ", lng " lng " in " point))

;; Try this: destructure the following

;; Use destructuring to reference the first and second elements of
;; this vector and addthem
[0 1 2 3 4]

;; Use destructuring to create the string "Jebediah is a butter churner"
{:name "Jebediah"
 :occupation "butter churner"}
