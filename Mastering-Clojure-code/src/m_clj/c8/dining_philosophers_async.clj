(ns m-clj.c8.dining-philosophers-async
  (:require [clojure.core.async :as a]
            [m-clj.c2.refs :as c2]))

;;; Example 8.4

(defn make-philosopher [name forks food]
  {:name name
   :forks forks
   :food food})

(defn make-forks [nf]
  (let [forks (repeatedly nf #(a/chan 1))]
    (doseq [f forks]
      (a/>!! f :fork))
    forks))

;;; Example 8.5

(defn philosopher-process [p-chan max-eat-ms max-think-ms]
  (a/go-loop []
    (let [p (a/<! p-chan)
          food (:food p)
          fork-1 ((:forks p) 0)
          fork-2 ((:forks p) 1)
          ;; take forks
          fork-1-result (a/alt!
                          (a/timeout 100) :timeout
                          fork-1 :fork-1)
          fork-2-result (a/alt!
                          (a/timeout 100) :timeout
                          fork-2 :fork-2)]
      (if (and (= fork-1-result :fork-1)
               (= fork-2-result :fork-2))
        (do
          ;; eat
          (a/<! (a/timeout (rand-int max-eat-ms)))
          ;; put down both acquired forks
          (a/>! fork-1 :fork)
          (a/>! fork-2 :fork)
          ;; think
          (a/<! (a/timeout (rand-int max-think-ms)))
          (a/>! p-chan (assoc p :food (dec food))))
        (do
          ;; put down any acquired forks
          (if (= fork-1-result :fork-1)
            (a/>! fork-1 :fork))
          (if (= fork-2-result :fork-2)
            (a/>! fork-2 :fork))
          (a/>! p-chan p)))
      ;; recur
      (when (pos? (dec food)) (recur)))))

;;; Example 8.6

(defn start-philosophers [p-chan philosophers]
  (a/onto-chan p-chan philosophers false)
  (dorun (repeatedly (count philosophers)
                     #(philosopher-process p-chan 100 100))))

(defn print-philosophers [p-chan n]
  (let [philosophers (repeatedly n #(a/<!! p-chan))]
    (doseq [p philosophers]
      (println (str (:name p) ":\t food=" (:food p)))
      (a/>!! p-chan p))))

;;; Example 8.7

(def all-forks (make-forks 5))
(def all-philosophers
  (c2/init-philosophers 5 1000 all-forks make-philosopher))

(def philosopher-chan (a/chan 5))

;; user> (start-philosophers philosopher-chan all-philosophers)
;; nil
;; user> (print-philosophers philosopher-chan 5)
;; Philosopher 3:	 food=937
;; Philosopher 2:	 food=938
;; Philosopher 1:	 food=938
;; Philosopher 5:	 food=938
;; Philosopher 4:	 food=937
;; nil
;; user> (print-philosophers philosopher-chan 5)
;; Philosopher 4:	 food=729
;; Philosopher 1:	 food=729
;; Philosopher 2:	 food=729
;; Philosopher 5:	 food=730
;; Philosopher 3:	 food=728
;; nil

;;; output could have different ordering and duplicates
