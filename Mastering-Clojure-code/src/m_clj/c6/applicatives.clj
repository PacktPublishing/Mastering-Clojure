(ns m-clj.c6.applicatives
  (:require [cats.core :as cc]
            [cats.builtin :as cb]
            [cats.monad.maybe :as cmm]
            [cats.applicative.validation :as cav]))

;; user> @(cc/fapply (cmm/just inc)
;;                   (cmm/just 1))
;; 2

;; user> (cc/pure cmm/context 1)
;; #<Just@cefb4d: 1>

;; user> @(cc/alet [a (cmm/just [1 2 3])
;;                  b (cmm/just [4 5 6])]
;;          (cc/mappend a b))
;; [1 2 3 4 5 6]

;;; Example 6.1

(defn validate-page-author [page]
  (if (nil? (:author page))
    (cav/fail {:author "No author"})
    (cav/ok page)))

(defn validate-page-number [page]
  (if (nil? (:number page))
    (cav/fail {:number "No page number"})
    (cav/ok page)))

(defn validate-page [page]
  (cc/alet [a (validate-page-author page)
            b (validate-page-number page)]
    (cc/<*> (cc/pure cav/context page)
            a b)))

;; user> (validate-page {:text "Some text"})
;; #<Fail@1203b6a: {:author "No author", :number "No page number"}>
;; user> (validate-page {:text "Some text" :author "John" :number 1})
;; #<Ok@161b2f8: {:text "Some text", :author "John", :number 1}>
