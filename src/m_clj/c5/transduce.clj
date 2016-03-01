(ns m-clj.c5.transduce)

;; user> (def xf-using-partial (comp
;;                              (partial filter even?)
;;                              (partial map inc)))
;; #'user/xf-using-partial
;; user> (xf-using-partial (vec (range 10)))
;; (2 4 6 8 10)

;;; Example 5.1

(declare unbundle-pallet)
(declare non-food?)
(declare label-heavy)

(def process-bags
  (comp
   (partial map label-heavy)
   (partial filter non-food?)
   (partial mapcat unbundle-pallet)))

;;; Example 5.2

(def process-bags
  (comp
   (mapcat unbundle-pallet)
   (filter non-food?)
   (map label-heavy)))

;; user> (def xf (map inc))
;; #'user/xf
;; user> (transduce xf conj [0 1 2])
;; [1 2 3]
;; user> (transduce xf conj () [0 1 2])
;; (3 2 1)

;; user> (def xf (comp
;;                (map inc)
;;                (filter even?)))
;; #'user/xf
;; user> (transduce xf conj (range 10))
;; [2 4 6 8 10]

;; user> (into [] xf (range 10))
;; [2 4 6 8 10]
;; user> (into () xf (range 10))
;; (10 8 6 4 2)

;; user> (sequence xf (range 10))
;; (2 4 6 8 10)

;;; Example 5.3

(def simple-eduction (eduction (map inc)
                               (filter even?)
                               (range)))

;; user> (time (nth simple-eduction 100000))
;; "Elapsed time: 65.904434 msecs"
;; 200001
;; user> (time (nth (map inc (filter even? (range))) 100000))
;; "Elapsed time: 159.039363 msecs"
;; 200001
