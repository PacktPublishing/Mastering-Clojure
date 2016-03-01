(ns m-clj.c9.fibers
  (:require [co.paralleluniverse.pulsar.core :as pc]
            [co.paralleluniverse.pulsar.dataflow :as pd]))

;;; Example 9.1

(defn add-with-fiber [a b]
  (let [f (pc/spawn-fiber
               (fn []
                 (pc/sleep 100)
                 (+ a b)))]
    (pc/join f)))

;;; Example 9.2

(defn df-add [a b]
  (let [x (pd/df-val)
        y (pd/df-val)
        sum (pd/df-var #(+ @x @y))]
    (x a)
    (y b)
    @sum))

;;; Example 9.3

(defn df-add-to-range [a r]
  (let [x (pd/df-val)
        y (pd/df-var)
        sum (pd/df-var #(+ @x @y))
        f (pc/fiber
           (for [i r]
             (do
               (y i)
               (pc/sleep 10)
               @sum)))]
    (x a)
    (pc/join f)))

;; user> (df-add-to-range 2 (range 10))
;; (2 3 4 5 6 7 8 9 10 11)
