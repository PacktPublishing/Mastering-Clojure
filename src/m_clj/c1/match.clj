(ns m-clj.c1.match
  (:require [clojure.core.match :as m]
            [defun :as f]))

;;; Example 1.11

(defn xor [x y]
  (m/match [x y]
           [true true] false
           [false true] true
           [true false] true
           [false false] false))

;;; Example 1.12

(f/defun xor
  ([true true] false)
  ([false true] true)
  ([true false] true)
  ([false false] false))

;; user> (xor true true)
;; false
;; user> (xor true false)
;; true
;; user> (xor false true)
;; true
;; user> (xor false false)
;; false

;; user> (xor 0 0)
;; IllegalArgumentException No matching clause: [0 0]  m-clj.c1-match/xor (form-init2896148386831218727.clj:1)

;;; Example 1.13

(f/defun fibo
  ([0] 0N)
  ([1] 1N)
  ([n] (+ (fibo (- n 1))
          (fibo (- n 2)))))

;; user> (fibo 0)
;; 0N
;; user> (fibo 1)
;; 1N
;; user> (fibo 10)
;; 55N

;;; Example 1.14

(f/defun fibo-recur
  ([a b 0] a)
  ([a b n] (recur b (+ a b) (dec n)))
  ([n] (recur 0N 1N n)))

;; user> (fibo-recur 0)
;; 0N
;; user> (fibo-recur 1)
;; 1N
;; user> (fibo-recur 10)
;; 55N
;; user> (fibo-recur 9999)
;; 207936...230626N
