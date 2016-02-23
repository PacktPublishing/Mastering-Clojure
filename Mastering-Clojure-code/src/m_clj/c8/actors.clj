(ns m-clj.c8.actors
  (:require [co.paralleluniverse.pulsar.core :as pc]
            [co.paralleluniverse.pulsar.actors :as pa]))

;;; Example 8.8

(def actor (pa/spawn
            #(pa/receive
              :finish (println "Finished")
              m (do
                  (println (str "Received: " m))
                  (recur)))))

;; user> (pa/! actor :foo)
;; nil
;; Received :foo
;; user> (pa/done? actor)
;; false

;; user> (pa/! actor :finish)
;; nil
;; Finished
;; user> (pa/done? actor)
;; true

;;; Example 8.9

(def divide-actor
  (pa/spawn
   #(loop [c 0]
      (pa/receive
       :result c
       [a b] (recur (/ a b))))))

;; user> (pa/! divide-actor 30 10)
;; nil
;; user> (pa/! divide-actor :result)
;; nil
;; user> (pc/join divide-actor)
;; 3

;;; Example 8.10

(defn ping-fn [n pong]
  (if (= n 0)
    (do
      (pa/! pong :finished)
      (println "Ping finished"))
    (do
      (pa/! pong [:ping @pa/self])
      (pa/receive
       :pong (println "Ping received pong"))
      (recur (dec n) pong))))

(defn pong-fn []
  (pa/receive
   :finished (println "Pong finished")
   [:ping ping] (do
                  (println "Pong received ping")
                  (pa/! ping :pong)
                  (recur))))

(defn start-ping-pong [n]
  (let [pong (pa/spawn pong-fn)
        ping (pa/spawn ping-fn n pong)]
    (pc/join pong)
    (pc/join ping)
    :finished))

;; user> (start-ping-pong 3)
;; Pong received ping
;; Ping received pong
;; Pong received ping
;; Ping received pong
;; Pong received ping
;; Ping received pong
;; Ping finished
;; Pong finished
;; :finished

;;; Example 8.11

(defn add-using-state [a b]
  (let [actor (pa/spawn
               #(do
                  (pa/set-state! 0)
                  (pa/set-state! (+ @pa/state (pa/receive)))
                  (pa/set-state! (+ @pa/state (pa/receive)))))]
    (pa/! actor a)
    (pa/! actor b)
    (pc/join actor)))

;; user> (add-using-state 10 20)
;; 30

;;; Example 8.12

(defn add-using-selective-receive [a b]
  (let [actor (pa/spawn
               #(do
                  (pa/set-state! 0)
                  (pa/receive
                   m (pa/receive
                      n (pa/set-state! (+ n m))))))]
    (pa/! actor a)
    (pa/! actor b)
    (pc/join actor)))

;; user> (add-using-selective-receive 10 20)
;; 30
