(ns m-clj.c10.midje
  (:require [midje.sweet :refer :all]
            [midje.repl :as mr]))

;;; Example 10.3

(defn first-element [sequence default]
  (if (empty? sequence)
    default
    (first sequence)))

;;; Example 10.4

(facts "about first-element"
  (fact "it returns the first element of a collection"
        (first-element [1 2 3] :default) => 1
        (first-element '(1 2 3) :default) => 1)

  (fact "it returns the default value for empty collections"
        (first-element [] :default) => :default
        (first-element '() :default) => :default
        (first-element nil :default) => :default
        (first-element (filter even? [1 3 5]) :default) => :default))

;;; Example 10.5

(fact "first-element returns the first element of a collection"
      (first-element ..seq.. :default) => :default
      (provided
       (empty? ..seq..) => true))

;;; Example 10.6

(defn is-diesel? [car])

(defn cost-of-car [car])

(defn overall-cost-of-car [car]
  (if (is-diesel? car)
    (* (cost-of-car car) 1.4)
    (cost-of-car car)))

;;; Example 10.7

(fact
  (overall-cost-of-car ..car..) => (* 5000 1.4)
  (provided
    (cost-of-car ..car..) => 5000
    (is-diesel? ..car..) => true))

;; user> (mr/autotest :files "test")

;; ======================================================================
;; Loading ( ... )
;; >>> Output from clojure.test tests:

;; 0 failures, 0 errors.
;; >>> Midje summary:
;; All checks (8) succeeded.
;; [Completed at ... ]
