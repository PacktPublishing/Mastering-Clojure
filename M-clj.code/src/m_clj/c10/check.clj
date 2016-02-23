(ns m-clj.c10.check
  (:require [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]))

;;; Example 10.12

(def commutative-*-prop
  (prop/for-all [a gen/int
                 b gen/int]
    (= (* a b)
       (* b a))))

(def first-is-min-after-sort-prop
  (prop/for-all [v (gen/not-empty (gen/vector gen/int))]
    (= (apply min v)
       (first (sort v)))))

;; user> (tc/quick-check 100 commutative-*-prop)
;; {:result true, :num-tests 100, :seed 1449998010193}
;; user> (tc/quick-check 100 first-is-min-after-sort-prop)
;; {:result true, :num-tests 100, :seed 1449998014634}

;;; Example 10.13

(def commutative-minus-prop
  (prop/for-all [a gen/int
                 b gen/int]
    (= (- a b)
       (- b a))))

;; user> (tc/quick-check 100 commutative-minus-prop)
;; {:result false, :seed 1449998165908,
;;  :failing-size 1, :num-tests 2, :fail [0 -1],
;;  :shrunk {:total-nodes-visited 1, :depth 0, :result false, :smallest [0 -1]}}

;;; Example 10.14

(defspec commutative-* 100
  (prop/for-all [a gen/int
                 b gen/int]
    (= (* a b)
       (* b a))))
