(ns m-clj.c6.functors
  (:require [cats.core :as cc]
            [cats.builtin :as cb]
            [cats.monad.maybe :as cmm]))

;; user> (map inc [0 1 2])
;; (1 2 3)
;; user> (cc/fmap inc [0 1 2])
;; [1 2 3]

;; user> (cc/<$> inc (lazy-seq '(1)))
;; (2)
;; user> (cc/<$> inc #{1})
;; #{2}

;; user> (cc/fmap inc (cmm/just 1))
;; #<Just@ff5df0: 2>
;; user> (cc/fmap inc (cmm/nothing))
;; #<Nothing@d4fb58: nil>

;;; Functor laws

;; user> (cc/<$> identity [0 1 2])
;; [0 1 2]

;; user> (->> [0 1 2]
;;            (cc/<$> inc)
;;            (cc/<$> (partial + 2)))
;; [3 4 5]
;; user> (cc/<$> (comp (partial + 2) inc) [0 1 2])
;; [3 4 5]
