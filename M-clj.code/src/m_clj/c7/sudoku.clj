(ns m-clj.c7.sudoku
  (:require [clojure.core.logic :as l]
            [clojure.core.logic.fd :as fd]))

;;; Example 7.4

(l/defne init-sudoku-board [vars puzzle]
  ([[] []])
  ([[_ . vs] [0 . ps]] (init-sudoku-board vs ps))
  ([[n . vs] [n . ps]] (init-sudoku-board vs ps)))

(defn solve-sudoku [puzzle]
  (let [board (repeatedly 81 l/lvar)
        rows (into [] (map vec (partition 9 board)))
        cols (apply map vector rows)
        val-range (range 1 10)
        in-range (fn [x]
                   (fd/in x (apply fd/domain val-range)))
        get-square (fn [x y]
                     (for [x (range x (+ x 3))
                           y (range y (+ y 3))]
                       (get-in rows [x y])))
        squares (for [x (range 0 9 3)
                      y (range 0 9 3)]
                  (get-square x y))]
    (l/run* [q]
      (l/== q board)
      (l/everyg in-range board)
      (init-sudoku-board board puzzle)
      (l/everyg fd/distinct rows)
      (l/everyg fd/distinct cols)
      (l/everyg fd/distinct squares))))

;;; Example 7.5

(defn solve-and-print-sudoku [puzzle]
  (let [solutions (solve-sudoku puzzle)]
    (dorun (for [i (-> solutions count range)
                 :let [s (nth solutions i)]]
             (do
               (println (str "\nSolution " (inc i) ":"))
               (clojure.pprint/pprint
                (partition 9 s)))))))

;;; Example 7.6

(def puzzle-1
  [0 9 0 0 0 0 0 5 0
   6 0 0 0 5 0 0 0 2
   1 0 0 8 0 4 0 0 6
   0 7 0 0 8 0 0 3 0
   8 0 3 0 0 0 2 0 9
   0 5 0 0 3 0 0 7 0
   7 0 0 3 0 2 0 0 5
   3 0 0 0 6 0 0 0 7
   0 1 0 0 0 0 0 4 0])

;; user> (solve-and-print-sudoku puzzle-1)
;; Solution 1:
;; ((4 9 8 6 2 3 7 5 1)
;;  (6 3 7 9 5 1 4 8 2)
;;  (1 2 5 8 7 4 3 9 6)
;;  (9 7 1 2 8 6 5 3 4)
;;  (8 4 3 5 1 7 2 6 9)
;;  (2 5 6 4 3 9 1 7 8)
;;  (7 6 9 3 4 2 8 1 5)
;;  (3 8 4 1 6 5 9 2 7)
;;  (5 1 2 7 9 8 6 4 3))
;; nil

;;; Example 7.7

(def puzzle-2
  [0 8 0 0 0 9 7 4 3
   0 5 0 0 0 8 0 1 0
   0 1 0 0 0 0 0 0 0
   8 0 0 0 0 5 0 0 0
   0 0 0 8 0 4 0 0 0
   0 0 0 3 0 0 0 0 6
   0 0 0 0 0 0 0 7 0
   0 3 0 5 0 0 0 8 0
   9 7 2 4 0 0 0 5 0])
