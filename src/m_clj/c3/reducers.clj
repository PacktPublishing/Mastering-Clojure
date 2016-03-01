(ns m-clj.c3.reducers
  (:require [clojure.core.reducers :as r]))

;;; Example 3.1

(defn square-with-side-effect [x]
  (do
    (println (str "Side-effect: " x))
    (* x x)))

;; user> (def mapped (map square-with-side-effect [0 1 2 3 4 5]))
;; #'user/mapped
;; user> (reduce + (take 3 mapped))
;; Side-effect: 0
;; Side-effect: 1
;; Side-effect: 2
;; Side-effect: 3
;; Side-effect: 4
;; Side-effect: 5
;; 5

;;; Example 3.2

;; (defn map [f coll]
;;   (cons (f (first coll))
;;         (lazy-seq (map f (rest coll)))))

;; user> (first (map #(do (print \!) %) (range 70)))
;; !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
;; 0
;; user> (nth (map #(do (print \!) %) (range 70)) 32)
;; !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
;; 32

;;; Example 3.3

(defn mapping [f]
  (fn [rf]
    (fn [result input]
      (rf result (f input)))))

(defn filtering [p?]
  (fn [rf]
    (fn [result input]
      (if (p? input)
        (rf result input)
        result))))

(defn mapcatting [f]
  (fn [rf]
    (fn [result input]
      (reduce rf result (f input)))))

;;; Example 3.4

(defprotocol CollReduce
  (coll-reduce [coll rf init]))

;;; Example 3.5

(defn reducer
  ([coll xf]
   (reify
     CollReduce
     (coll-reduce [_ rf init]
       (coll-reduce coll (xf rf) init)))))

;;; Example 3.6

;; (defn reduce
;;   ([rf coll]
;;    (reduce rf (rf) coll))
;;   ([rf init coll]
;;    (coll-reduce coll rf init)))

;; user> (r/reduce + 0 (r/reducer [1 2 3 4] (mapping inc)))
;; 14
;; user> (reduce + 0 (r/reducer [1 2 3 4] (mapping inc)))
;; 14

;;; Example 3.7

;; (defn map [f coll]
;;   (reducer coll (mapping f)))

;; (defn filter [p? coll]
;;   (reducer coll (filtering p?)))

;; (defn mapcat [f coll]
;;   (reducer coll (mapcatting f)))

;; user> (r/reduce + (r/map inc [1 2 3 4]))
;; 14
;; user> (r/reduce + (r/filter even? [1 2 3 4]))
;; 6
;; user> (r/reduce + (r/mapcat range [1 2 3 4]))
;; 10

;; user> (def mapped (r/map square-with-side-effect [0 1 2 3 4 5]))
;; #'user/mapped
;; user> (reduce + (r/take 3 mapped))
;; Side-effect: 0
;; Side-effect: 1
;; Side-effect: 2
;; Side-effect: 3
;; 5

;;; Example 3.8

(defn process [nums]
  (reduce + (map inc (map inc (map inc nums)))))

(defn process-with-reducer [nums]
  (reduce + (r/map inc (r/map inc (r/map inc nums)))))

;; user> (def nums (vec (range 1000000)))
;; #'user/nums
;; user> (time (process nums))
;; "Elapsed time: 471.217086 msecs"
;; 500002500000
;; user> (time (process-with-reducer nums))
;; "Elapsed time: 356.767024 msecs"
;; 500002500000

;;; Example 3.9

(defprotocol CollFold
  (coll-fold [coll n cf rf]))

;;; Example 3.10

(defn folder
  ([coll xf]
   (reify
     CollReduce
     (coll-reduce [_ rf init]
       (coll-reduce coll (xf rf) init))
     CollFold
     (coll-fold [_ n cf rf]
       (coll-fold coll n cf (xf rf))))))

;;; Example 3.11

;; (defn fold
;;   ([rf coll]
;;    (fold rf rf coll))
;;   ([cf rf coll]
;;    (fold 512 cf rf coll))
;;   ([n cf rf coll]
;;    (coll-fold coll n cf rf)))

;;; Example 3.12

;; (defn monoid
;;   [op ctor]
;;   (fn
;;     ([] (ctor))
;;     ([a b] (op a b))))

;;; Example 3.13

;; (defn map [f coll]
;;   (folder coll (mapping f)))

;; (defn filter [p? coll]
;;   (folder coll (filtering p?)))

;; (defn mapcat [f coll]
;;   (folder coll (mapcatting f)))

;;; Example 3.14

(defn process-with-folder [nums]
  (r/fold + (r/map inc (r/map inc (r/map inc nums)))))

;; user> (def nums (vec (range 1000000)))
;; #'user/nums
;; user> (time (process nums))
;; "Elapsed time: 474.240782 msecs"
;; 500002500000
;; user> (time (process-with-reducer nums))
;; "Elapsed time: 364.945748 msecs"
;; 500002500000
;; user> (time (process-with-folder nums))
;; "Elapsed time: 241.057025 msecs"
;; 500002500000
