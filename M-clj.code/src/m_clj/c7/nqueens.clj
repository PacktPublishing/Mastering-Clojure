(ns m-clj.c7.nqueens
  (:require [clojure.core.logic :as l]))

;;; Example 7.2

(l/defne safeo [q qs]
  ([_ ()])
  ([[x1 y1] [[x2 y2] . t]]
     (l/!= x1 x2)
     (l/!= y1 y2)
     (l/project [x1 x2 y1 y2]
       (l/!= (- x2 x1) (- y2 y1))
       (l/!= (- x1 x2) (- y2 y1)))
     (safeo [x1 y1] t)))

(l/defne nqueenso [n qs]
  ([_ ()])
  ([n [[x y] . t]]
     (nqueenso n t)
     (l/membero x (range n))
     (safeo [x y] t)))

(defn solve-nqueens [n]
  (l/run* [qs]
    (l/== qs (map vector (repeatedly l/lvar) (range n)))
    (nqueenso n qs)))

;;; Example 7.3

(defn print-nqueens-solution [solution n]
  (let [solution-set (set solution)
        positions (for [x (range n)
                        y (range n)]
                    (if (contains? solution-set [x y]) 1 0))]
    (binding [clojure.pprint/*print-right-margin* (* n n)]
      (clojure.pprint/pprint
       (partition n positions)))))

(defn print-all-nqueens-solutions [solutions n]
  (dorun (for [i (-> solutions count range)
               :let [s (nth solutions i)]]
           (do
             (println (str "\nSolution " (inc i) ":"))
             (print-nqueens-solution s n)))))

(defn solve-and-print-nqueens [n]
  (-> (solve-nqueens n)
      (print-all-nqueens-solutions n)))

;; user> (solve-and-print-nqueens 4)

;; Solution 1:
;; ((0 1 0 0)
;;  (0 0 0 1)
;;  (1 0 0 0)
;;  (0 0 1 0))

;; Solution 2:
;; ((0 0 1 0)
;;  (1 0 0 0)
;;  (0 0 0 1)
;;  (0 1 0 0))
;; nil
