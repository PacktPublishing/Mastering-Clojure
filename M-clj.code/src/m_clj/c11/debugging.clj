(ns m-clj.c11.debugging
  (:require [clojure.tools.trace :as tr]
            [spyscope.repl :as sr]))

;;; Example 11.1

(defn make-vector [x]
  [x])

;; user> (tr/trace (make-vector 0))
;; TRACE: [0]
;; [0]
;; user> (tr/trace "my-tag" (make-vector 0))
;; TRACE my-tag: [0]
;; [0]

;; user> (tr/trace-forms (+ 10 20) (* 2 3) (/ 10 0))
;; ArithmeticException Divide by zero
;;   Form failed: (/ 10 0)
;; clojure.lang.Numbers.divide (Numbers.java:158)

;;; Example 11.2

(tr/deftrace add-into-vector [& xs]
  (let [sum (apply + xs)]
    [sum]))

;; user> (add-into-vector 10 20)
;; TRACE t9083: (add-into-vector 10 20)
;; TRACE t9083: => [30]
;; [30]

;; user> (take 5 (repeatedly
;;                #(let [r (rand-int 100)]
;;                   #spy/p r)))
;; 95
;; 36
;; 61
;; 99
;; 73
;; (95 36 61 99 73)

;; user> (take 5 (repeatedly
;;                #(let [r (rand-int 100)]
;;                   #spy/d (/ r 10.0))))
;; user$eval9408$fn__9409.invoke(form-init1..0.clj:2) (/ r 10.0) => 4.6
;; user$eval9408$fn__9409.invoke(form-init1..0.clj:2) (/ r 10.0) => 4.4
;; user$eval9408$fn__9409.invoke(form-init1..0.clj:2) (/ r 10.0) => 5.0
;; user$eval9408$fn__9409.invoke(form-init1..0.clj:2) (/ r 10.0) => 7.8
;; user$eval9408$fn__9409.invoke(form-init1..0.clj:2) (/ r 10.0) => 3.1
;; (4.6 4.4 5.0 7.8 3.1)

;; user> (take 5 (repeat #spy/d ^{:fs 3 :marker "add"} (+ 0 1 2)))
;; ----------------------------------------
;; clojure.lang.Compiler.eval(Compiler.java:6745)
;; clojure.lang.Compiler.eval(Compiler.java:6782)
;; user$eval9476.invoke(form-init1..0.clj:1) add (+ 0 1 2) => 3
;; (3 3 3 3 3)

;; user> (take 5 (repeat #spy/d ^{:fs 3 :nses #"core|user"} (+ 0 1 2)))
;; ----------------------------------------
;; clojure.core$apply.invoke(core.clj:630)
;; clojure.core$eval.invoke(core.clj:3081)
;; user$eval9509.invoke(form-init1..0.clj:1) (+ 0 1 2) => 3
;; (3 3 3 3 3)

;;; Example 11.3

(defn add-in-future [& xs]
  (future
    #spy/t (apply + xs)))

;; user> (sr/trace-next)
;; nil
;; user> (def f1 (add-in-future 10 20))
;; #'user/f1
;; user> (def f2 (add-in-future 20 30))
;; #'user/f2
;; user> (sr/trace-query)
;; user$add_in_future$fn_..7.invoke(debugging.clj:66) (apply + xs) => 30
;; ----------------------------------------
;; user$add_in_future$fn_..7.invoke(debugging.clj:66) (apply + xs) => 50
;; nil
