(ns m-clj.c6.monads
  (:require [cats.core :as cc]
            [cats.builtin :as cb]
            [cats.monad.maybe :as cmm]
            [cats.monad.identity :as cmi]
            [cats.monad.exception :as cme]))

;; user> (cc/bind (cmm/just 1) inc)
;; 2
;; user> (cc/bind (cmm/nothing) inc)
;; #<Nothing@24e44b: nil>

;; user> (cc/bind (cmm/just 1) #(-> % inc cc/return))
;; #<Just@208e3: 1>
;; user> (cc/bind (cmm/nothing) #(-> % inc cc/return))
;; #<Nothing@1e7075b: nil>

;; user> ((cc/lift-m inc) (cmm/just 1))
;; #<Just@1eaaab: 2>

;; user> (cc/>>= (cc/>>= (cmm/just 1)
;;                       #(-> % inc cmm/just))
;;               #(-> % dec cmm/just))
;; #<Just@91ea3c: 1>

;; user> (cc/>>= (cc/>>= (cmm/just 1)
;;                       #(-> % inc cmm/just))
;;               #(-> % dec cmi/identity))
;; #<Identity@dd6793: 1>

;;; Example 6.2

(defn process-with-maybe [x]
  (cc/mlet [a (if (even? x)
                (cmm/just x)
                (cmm/nothing))
            b (do
                (println (str "Incrementing " a))
                (-> a inc cmm/just))]
    b))

;; user> (process-with-maybe 2)
;; Incrementing 2
;; 3
;; user> (process-with-maybe 3)
;; #<Nothing@1ebd3fe: nil>

;; user> (cme/success 1)
;; #<Success@441a312 [1]>
;; user> (cme/failure {:error (Exception.)})
;; #<Failure@4812b43 [#<java.lang.Exception>]>
;; user> (cme/try-on 1)
;; #<Success@5141a5 [1]>
;; user> @(cme/try-on 1)
;; 1

;; user> (cme/try-on (/ 1 0))
;; #<Failure@bc1115 [#<java.lang.ArithmeticException>]>
;; user> (cme/try-on (-> 1 (/ 0) inc))
;; #<Failure@f2d11a [#<java.lang.ArithmeticException>]>

;; user> (cc/bind (cme/try-on (/ 1 1)) #(-> % inc cc/return))
;; #<Success@116ea43 [2]>
;; user> (cc/bind (cme/try-on (/ 1 0)) #(-> % inc cc/return))
;; #<Failure@0x1c90acb [#<java.lang.ArithmeticException>]>

;; user> (cme/try-or-else (/ 1 0) 0)
;; #<Success@bd15e6 [0]>
;; user> (cme/try-or-recover (/ 1 0)
;;                           (fn [e]
;;                             (if (instance? ArithmeticException e)
;;                               0
;;                               :error)))
;; 0
