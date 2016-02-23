(ns m-clj.c2.parallel
  (:require [com.climate.claypoole :as cp]
            [com.climate.claypoole.lazy :as cpl]))

;;; Example 2.17

(defn square-slowly [x]
  (Thread/sleep 2000)
  (* x x))

;; user> (time (doall (map square-slowly (repeat 3 10))))
;; "Elapsed time: 6000.329702 msecs"
;; (100 100 100)

;; user> (time (doall (pmap square-slowly (repeat 3 10))))
;; "Elapsed time: 2001.543439 msecs"
;; (100 100 100)

;; user> (time (doall (pvalues (square-slowly 10)
;;                             (square-slowly 10)
;;                             (square-slowly 10))))
;; "Elapsed time: 2007.702703 msecs"
;; (100 100 100)
;; user> (time (doall (pcalls #(square-slowly 10)
;;                            #(square-slowly 10)
;;                            #(square-slowly 10))))
;; "Elapsed time: 2005.683279 msecs"
;; (100 100 100)

;;; pvalues is a macro that uses pcalls
;;; pcalls is just a composition of pmap

;;; claypoole

;; user> (time (doall (cpl/pmap 2 square-slowly [10 10 10])))
;; "Elapsed time: 4004.029789 msecs"
;; (100 100 100)

(def pool (cp/threadpool (cp/ncpus)))

;; user> (def pool (cp/threadpool (cp/ncpus)))
;; #'user/pool
;; user> (time (doall (cpl/pmap pool square-slowly [10 10 10])))
;; "Elapsed time: 4002.05885 msecs"
;; (100 100 100)

;; user> (def pool (cp/priority-threadpool (cp/ncpus))
;; #'user/pool
;; user> (def task-1 (cp/pmap (cp/with-priority pool 1000)
;;                            square-slowly [10 10 10]))
;; #'user/task-1
;; user> (def task-2 (cp/pmap (cp/with-priority pool 0)
;;                            square-slowly [5 5 5]))
;; #'user/task-2

;;; (cp/shutdown pool)

;;; Example 2.18

(defn square-slowly-with-pool [v]
  (cp/with-shutdown! [pool (cp/threadpool (cp/ncpus))]
    (doall (cp/pmap pool square-slowly v))))

;;; cp/pcalls, cp/pvalues, cp/future, cp/pfor
;;; cp/upmap

;; user> (def lazy-pmap (cpl/pmap pool square-slowly (range)))
;; #'user/lazy-pmap
;; user> (time (doall (take 4 lazy-pmap)))
;; "Elapsed time: 4002.556548 msecs"
;; (0 1 4 9)
