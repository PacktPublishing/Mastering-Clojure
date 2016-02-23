(ns m-clj.c2.agents
  (:require [m-clj.c2.refs :refer [has-forks?
                                   init-forks
                                   init-philosophers
                                   update-forks
                                   check-philosophers]]))

(def state (agent {}))

;;; Example 2.14

(defn set-value-in-ms [n ms]
  (fn [a]
    (Thread/sleep ms)
    (assoc a :value n)))

;; user> (send state (set-value-in-ms 5 5000))
;; #<Agent@7fce18: {}>
;; user> (send-off state (set-value-in-ms 10 5000))
;; #<Agent@7fce18: {}>
;; user> @state
;; {}
;; user> @state	; after 5 seconds
;; {:value 5}
;; user> @state	; after another 5 seconds
;; {:value 10}

;; user> (send-off state (set-value-in-ms 100 3000))
;; #<Agent@af9ac: {:value 10}>
;; user> (await state)	; will block
;; nil
;; user> @state
;; {:value 100}

;;; await-for is a variant of await that will wait for a
;;; limited amount of time

;; user> (def a (agent 1))
;; #'user/a
;; user> (send a / 0)
;; #<Agent@5d29f1: 1>
;; user> (agent-error a)
;; #<ArithmeticException java.lang.ArithmeticException: Divide by zero>
;; user> (clear-agent-errors a)
;; 1
;; user> (agent-error a)
;; nil
;; user> @a
;; 1

(def pool (java.util.concurrent.Executors/newFixedThreadPool 10))

;; user> (send-via pool state assoc :value 1000)
;; #<Agent@8efada: {:value 100}>
;; user> @state
;; {:value 1000}


;;; Example 2.15

(defn make-philosopher-agent [name forks food]
  (agent {:name name
          :forks forks
          :eating? false
          :food food}))

(defn start-eating [max-eat-ms]
  (dosync (if (has-forks? *agent*)
            (do
              (-> *agent*
                  update-forks
                  (send assoc :eating? true)
                  (send update-in [:food] dec))
              (Thread/sleep (rand-int max-eat-ms))))))

(defn stop-eating [max-think-ms]
  (dosync (-> *agent*
              (send assoc :eating? false)
              update-forks))
  (Thread/sleep (rand-int max-think-ms)))

(def running? (atom true))

(defn dine [p max-eat-ms max-think-ms]
  (when (and p (pos? (:food p)))
    (if-not (:eating? p)
      (start-eating max-eat-ms)
      (stop-eating max-think-ms))
    (if-not @running?
      @*agent*
      @(send-off *agent* dine max-eat-ms max-think-ms))))

(defn dine-philosophers [philosophers]
  (swap! running? (fn [_] true))
  (doall (for [p philosophers]
           (send-off p dine 100 100))))

(defn stop-philosophers []
  (swap! running? (fn [_] false)))

;;; Example 2.16

(def all-forks (init-forks 5))

(def all-philosophers
  (init-philosophers 5 1000 all-forks make-philosopher-agent))

;; user> (def philosophers-agents (dine-philosophers all-philosophers))
;; #'user/philosophers-agents
;; user> (check-philosophers all-philosophers all-forks)
;; Fork:                    available=false
;; Philosopher 1:           eating=false food=936
;; Fork:                    available=false
;; Philosopher 2:           eating=false food=942
;; Fork:                    available=true
;; Philosopher 3:           eating=true food=942
;; Fork:                    available=true
;; Philosopher 4:           eating=false food=935
;; Fork:                    available=true
;; Philosopher 5:           eating=true food=943
;; nil
;; user> (check-philosophers all-philosophers all-forks)
;; Fork:                    available=false
;; Philosopher 1:           eating=true food=743
;; Fork:                    available=false
;; Philosopher 2:           eating=false food=747
;; Fork:                    available=true
;; Philosopher 3:           eating=false food=751
;; Fork:                    available=true
;; Philosopher 4:           eating=false food=741
;; Fork:                    available=true
;; Philosopher 5:           eating=false food=760
;; nil
