(ns m-clj.c3.io
  (:require [iota :as i]
            [clojure.string :as cs]
            [clojure.core.reducers :as r]))

;;; resources/data/sample.tsv has records like:
;; ...
;; brown	brian	:m	:child	:east
;; smith	bill	:f	:child	:south
;; jones	jill	:f	:parent	:west
;; ...

;; user> (time (def file-as-seq (i/seq "resources/data/sample.tsv")))
;; "Elapsed time: 0.905326 msecs"
;; #'user/file-as-seq
;; user> (time (def file-as-vec (i/vec "resources/data/sample.tsv")))
;; "Elapsed time: 4.95506 msecs"
;; #'user/file-as-vec

;; user> (time (def first-100-lines (doall (take 100 file-as-seq))))
;; "Elapsed time: 63.470598 msecs"
;; #'user/first-100-lines
;; user> (time (def first-100-lines (doall (take 100 file-as-vec))))
;; "Elapsed time: 0.984128 msecs"
;; #'user/first-100-lines


;;; Example 3.15

(defn into-records [file]
  (->> file
       (r/filter identity)
       (r/map #(cs/split % #"[\t]"))))

;;; Example 3.16

(defn count-females [coll]
  (->> coll
       (r/map #(-> (nth % 2)
                   ({":m" 0 ":f" 1})))
       (r/fold +)))

;; user> (-> file-as-seq into-records count-females)
;; 10090
;; user> (-> file-as-vec into-records count-females)
;; 10090

;;; Example 3.17

(defn get-children-names-in-family [coll family]
  (->> coll
       (r/filter #(and (= (nth % 0) family)
                       (= (nth % 3) ":child")))
       (r/map #(nth % 1))
       (into [])))

;; user> (-> file-as-seq into-records
;;           (get-children-names-in-family "brown"))
;; ["sue" "walter" ... "jill"]
;; user> (-> file-as-vec into-records
;;           (get-children-names-in-family "brown"))
;; ["sue" "walter" ... "jill"]
