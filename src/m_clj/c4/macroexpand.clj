(ns m-clj.c4.macroexpand)

;;; macroexpand, macroexpand-1 and macroexpand-all

;; user> (-> [0 1 2] first inc)
;; 1
;; user> (-> [0 1 2] (-> first inc))
;; 1
;; user> (-> (-> [0 1 2] first) inc)
;; 1

;; user> (macroexpand '(-> [0 1 2] first inc))
;; (inc (first [0 1 2]))

;; user> (macroexpand-1 '(-> [0 1 2] (-> first inc)))
;; (-> [0 1 2] first inc)
;; user> (macroexpand '(-> [0 1 2] (-> first inc)))
;; (inc (first [0 1 2]))

;; user> (macroexpand-1 '(-> (-> [0 1 2] first) inc))
;; (inc (-> [0 1 2] first))
;; user> (macroexpand '(-> (-> [0 1 2] first) inc))
;; (inc (-> [0 1 2] first))

;; user> (clojure.walk/macroexpand-all '(-> (-> [0 1 2] first) inc))
;; (inc (first [0 1 2]))
