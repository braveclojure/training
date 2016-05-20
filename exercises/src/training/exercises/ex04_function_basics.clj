(ns training.exercises.ex04-function-basics)

;; ========================================
;; Function expressions
;; ========================================

(+ 1 2)
((or + -) 1 2)
((or +) 1 2)


;; ========================================
;; Defining functions
;; ========================================

(defn your-fn
  "docstring"
  [arg1 arg2 & args])

(defn no-params
  []
  "This function doesn't want your input")

(defn one-param
  [x]
  (str x " is the best thing in the world!"))

(defn rest-param
  [& args]
  args)
(rest-param 1 2 3)

;; multi-arity function

(defn resize-image
  "With 2-artiy, resize an image so that the larger dimension is equal
  to second argument. With 3-arity, specify width and height"
  ([image box]
   (let [scale  (/ box (max (:width image) (:height image)))
         width  (* scale (:width image))
         height (* scale (:height image))]
     (resize-image image width height)))
  ([image width height]
   {:width width :height height}))

;; Exercise: write a function number-max that compares two numbers and returns
;; the larger.



;; ========================================
;; Functions are values
;; ========================================

(map inc [1 2 3])
((complement empty?) [1 2 3])

;; Bind functions to symbols just like any other value
(def occupied? (complement empty?))
(occupied? [1 2 3])

;; Take a function as an argument, just like data
(defn mathochist
  [x op y]
  (op x y))
(mathochist 1 / 3)



;; ========================================
;; Anonymous functions
;; ========================================
;; Subtitle: built-in functions aren't special!

(fn [x] (+ x 1))
#(+ % 1)

;; note to self: mention closures
(defn inc-maker
  [n]
  #(+ % n))
((inc-maker 3) 5)

;; Bind anonymous functions
(def inc-5 (inc-maker 5))
(inc-5 10)
(def inc-10 #(+ % 10))

;; Pass anonymous functions as arguments
(filter even?
        [0 1 2 3 4])

(filter #(= 0 (mod % 3))
        [0 1 2 3 4])



;; ========================================
;; Recursion
;; ========================================

;; functions can call themselves
(defn factorial
  [n]
  (if (= n 1) 1
      (* n (factorial (dec n)))))

;; recur will call the function recursively without consuming stack
(defn tail-recursive-factorial
  ([n] (tail-recursive-factorial n 1))
  ([n acc]
   (if (= n 1) acc
       (recur (dec n) (* n acc)))))

;; You can also do recursive calls with loop/recur
(loop [n   1
       sum 0]
  (if (= n 6)
    sum
    (recur (inc n) (+ sum n))))


