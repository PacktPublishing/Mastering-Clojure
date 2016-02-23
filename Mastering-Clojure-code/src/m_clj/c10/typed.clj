(ns m-clj.c10.typed
  (:require [clojure.core.typed :as t]))

;;; Example 10.15

(t/ann add [Number Number -> Number])
(defn add [a b]
  (+ a b))

;; user> (t/check-ns 'my-namespace)
;; Start collecting my-namespace
;; Finished collecting my-namespace
;; Collected 2 namespaces in 200.965982 msecs
;; Start checking my-namespace
;; Checked my-namespace in 447.580402 msecs
;; Checked 2 namespaces  in 650.979682 msecs
;; :ok

;;; Example 10.16

;;; change `add` to this
;; (defn add [a b]
;;   (str (+ a b)))

;; user> (t/check-ns 'my-namespace)
;; Start collecting my-namespace
;; Finished collecting my-namespace
;; Collected 2 namespaces in 215.705251 msecs
;; Start checking my-namespace
;; Checked my-namespace in 493.669488 msecs
;; Checked 2 namespaces  in 711.644548 msecs
;; Type Error (m_clj/c1/typed.clj:23:3) Type mismatch:

;; Expected: 	Number

;; Actual: 	String
;; in: (str (clojure.lang.Numbers/add a b))


;; ExceptionInfo Type Checker: Found 1 error  clojure.core/ex-info (core.clj:4403)


;;; Example 10.17

(t/ann add-abc
       (t/IFn [Number Number -> Number]
              [Number Number Number -> Number]))
(defn add-abc
  ([a b]
   (+ a b))
  ([a b c]
   (+ a b c)))

;;; Example 10.18

(t/ann add-xs [Number * -> Number])
(defn add-xs [& xs]
  (apply + xs))

;; user> (t/cf nil)
;; [nil {:then ff, :else tt}]
;; user> (t/cf true)
;; [true {:then tt, :else ff}]
;; user> (t/cf false)
;; [false {:then ff, :else tt}]

;; user> (t/cf "Hello")
;; [(t/Val "Hello") {:then tt, :else ff}]
;; user> (t/cf 1)
;; [(t/Val 1) {:then tt, :else ff}]
;; user> (t/cf :key)
;; [(t/Val :key) {:then tt, :else ff}]

;; user> (t/cf str)
;; [t/Any * -> String]
;; user> (t/cf +)
;; (t/IFn [Long * -> Long]
;;        [(t/U Double Long) * -> Double]
;;        [t/AnyInteger * -> t/AnyInteger]
;;        [Number * -> Number])

;; user> (t/cf (t/ann-form #(+ 1 %) [Number -> Number]))
;; [[Number -> Number] {:then tt, :else ff}]
;; user> (t/cf (t/ann-form #(str %) [t/Any -> String]))
;; [[t/Any -> String] {:then tt, :else ff}]

;; user> (t/cf (list 0 1 2))
;; (PersistentList (t/U (t/Val 1) (t/Val 0) (t/Val 2)))
;; user> (t/cf [0 1 2])
;; [(t/HVec [(t/Val 0) (t/Val 1) (t/Val 2)]) {:then tt, :else ff}]

;; user> (t/cf (list 0 1 2) (t/List t/Num))
;; (t/List t/Num)
;; user> (t/cf [0 1 2] (t/Vec t/Num))
;; (t/Vec t/Num)
;; user> (t/cf {:a 1 :b 2} (t/Map t/Keyword t/Int))
;; (t/Map t/Keyword t/Int)
;; user> (t/cf #{0 1 2} (t/Set t/Int))
;; (t/Set t/Int)

;;; Example 10.19

;; (defalias AnyInteger
;;   (U Integer Long clojure.lang.BigInt BigInteger Short Byte))

;; user> (t/cf identity)
;; (t/All [x] [x -> x :filters ... ])
;; user> (t/cf iterate)
;; (t/All [x] [[x -> x] x -> (t/ASeq x)])

;;; Example 10.20

(t/ann make-map (t/All [x] [x -> (t/Map t/Keyword x)]))
(defn make-map [a]
  {:x a})
