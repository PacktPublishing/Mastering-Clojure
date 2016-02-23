(ns m-clj.c8.async
  (:require [clojure.core.async :as a]))

;;; Example 8.1

(defn wait-and-print [c]
  (a/go-loop [n 1]
    (let [v (a/<! c)]
      (when v
        (println (str "Got a message: " v))
        (println (str "Got " n " messages so far!"))
        (recur (inc n)))))
  (println "Waiting..."))

;; user> (def c (a/chan))
;; #'user/c
;; user> (wait-and-print c)
;; Waiting...
;; nil
;; user> (a/>!! c :foo)
;; true
;; Got a message: :foo
;; Got 1 messages so far!
;; user> (a/>!! c :bar)
;; true
;; Got a message: :bar
;; Got 2 messages so far!

;; user> (a/close! c)
;; nil
;; user> (a/>!! c :foo)
;; false

;; user> (def c (a/chan 4))
;; #'user/c
;; user> (a/onto-chan c (range 4))
;; #<ManyToManyChannel@0x86f03a>
;; user> (repeatedly 4 #(-> c a/<!!))
;; (0 1 2 3)

;;; Example 8.2

(defn process-channels [c0 c1 c2 c3 c4 c5]
  (a/go
    (a/alt!
      ;; read from c0, c1, c2, c3
      c0 :r
      c1 ([v] (str v))
      [c2 c3] ([v c] (str v))
      ;; write to c4, c5
      [[c4 :v1] [c5 :v2]] :w)))

;; user> (time  (a/<!! (a/timeout 1000)))
;; "Elapsed time: 1029.502223 msecs"
;; nil

;; user> (def c (a/chan 5))
;; #'user/c
;; user> (a/onto-chan c (range 5))
;; #<ManyToManyChannel@0x4adadd>
;; user> (def rc (a/reduce #(str %1 %2 " ") "" c))
;; #'user/rc
;; user> (a/<!! rc)
;; "0 1 2 3 4 "

;;; Example 8.3

(def xform
  (comp
   (map inc)
   (map #(* % 2))))

;; user> (def xc (a/chan 10 xform))
;; #'user/xc
;; user> (a/onto-chan xc (range 10) false)
;; #<ManyToManyChannel@0x17d6a37>
;; user> (repeatedly 10 #(-> xc a/<!!))
;; (2 4 6 8 10 12 14 16 18 20)
