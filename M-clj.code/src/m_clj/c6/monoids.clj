(ns m-clj.c6.monoids
  (:require [cats.core :as cc]
            [cats.builtin :as cb]
            [cats.monad.maybe :as cmm]))

;; user> (cc/mappend "12" "34" "56")
;; "123456"
;; user> (cc/mappend [1 2] [3 4] [5 6])
;; [1 2 3 4 5 6]

;; user> @(cc/mappend (cmm/just "123")
;;                    (cmm/just "456"))
;; "123456"
;; user> @(cc/mappend (cmm/just "123")
;;                    (cmm/nothing)
;;                    (cmm/just "456"))
;; "123456"
