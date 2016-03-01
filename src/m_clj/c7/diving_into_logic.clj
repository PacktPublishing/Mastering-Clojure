(ns m-clj.c7.diving-into-logic
  (:require [clojure.core.logic :as l]
            [clojure.core.logic.fd :as fd]))

;; user> (l/run* [x]
;;         (l/== x 1))
;; (1)
;; user> (l/run* [x]
;;         (l/== 1 0))
;; ()

;; user> (l/run* [x]
;;         (l/!= 1 1))
;; ()
;; user> (l/run* [x]
;;         (l/== 1 1))
;; (_0)

;; user> (l/run* [x]
;;         (l/conso 1 [2 x]
;;                  [1 2 3]))
;; (3)

;; user> (l/run* [x y]
;;         (l/== x y)
;;         (l/== y 1))
;; ([1 1])
;; user> (l/run* [x]
;;         (l/fresh [y]
;;           (l/== x y)
;;           (l/== y 1)))
;; (1)

;; user> (l/run 5 [x]
;;         (fd/in x (fd/interval 0 100))
;;         (fd/> x 10))
;; (11 12 13 14 15)

;; user> (l/run 1 [v a b x]
;;         (l/== v [a b])
;;         (fd/in a b x (fd/domain 0 1 2))
;;         (fd/< a b)
;;         (l/firsto v x))
;; ([[0 1] 0 1 0])

;; user> (l/run* [x]
;;         (l/conde
;;           ((l/== 'A x) l/succeed)
;;           ((l/== 'B x) l/succeed)
;;           ((l/== 'C x) l/fail)))
;; (A B)

;; user> (l/run* [x]
;;         (l/conde
;;           ((l/== 'A x) l/succeed)
;;           ((l/== 'B x) l/succeed)))
;; (A B)
;; user> (l/run* [x]
;;         (l/matche [x]
;;           (['A] l/succeed)
;;           (['B] l/succeed)))
;; (A B)

;; user> (l/run* [x]
;;         (l/matche [x]
;;           (['A])
;;           ([_] l/fail)))
;; (A)

;; user> (l/run* [x]
;;         (l/fresh [y]
;;           (l/== y [1 2 3])
;;           (l/matche [y]
;;             ([[1 . x]]))))
;; ((2 3))

;;; Example 7.1

(l/defne membero [x xs]
  ([_ [x . ys]])
  ([_ [y . ys]]
   (membero x ys)))

;; user> (l/run* [x]
;;         (membero x (range 5))
;;         (membero x (range 3 10)))
;; (3 4)

;; user> (l/run 2 [x y]
;;         (l/membero x (range 1 10))
;;         (l/membero y (range 1 10))
;;         (l/project [x y]
;;           (l/== (+ x y) 5)))
;; ([1 4] [2 3])
