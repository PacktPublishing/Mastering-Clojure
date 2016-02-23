(ns m-clj.c2.refs)

(def state (ref 0))

;; user> @state
;; 0
;; user> (dosync (ref-set state 1))
;; 1
;; user> @state
;; 1

;; user> @state
;; 1
;; user> (dosync (alter state + 2))
;; 3
;; user> (dosync (commute state + 2))
;; 5

;; user> (def r (ref 1 :validator pos?))
;; #'user/r
;; user> (dosync (alter r (fn [_] -1)))
;; IllegalStateException Invalid reference state  clojure.lang.ARef.validate (ARef.java:33)
;; user> (dosync (alter r (fn [_] 2)))
;; 2

;;; Example 2.6

(defn make-fork []
  (ref true))

(defn make-philosopher [name forks food]
  (ref {:name name
        :forks forks
        :eating? false
        :food food}))

;;; Example 2.7

(defn has-forks? [p]
  (every? true? (map ensure (:forks @p))))

(defn update-forks [p]
  (doseq [f (:forks @p)]
    (commute f not))
  p)

;;; Example 2.8

(defn start-eating [p]
  (dosync
   (when (has-forks? p)
     (update-forks p)
     (commute p assoc :eating? true)
     (commute p update-in [:food] dec))))

(defn stop-eating [p]
  (dosync
   (when (:eating? @p)
     (commute p assoc :eating? false)
     (update-forks p))))

(defn dine [p retry-ms max-eat-ms max-think-ms]
  (while (pos? (:food @p))
    (if (start-eating p)
      (do
        (Thread/sleep (rand-int max-eat-ms))
        (stop-eating p)
        (Thread/sleep (rand-int max-think-ms)))
      (Thread/sleep retry-ms))))

;;; Example 2.9

(defn init-forks [nf]
  (repeatedly nf #(make-fork)))

(defn init-philosophers [np food forks init-fn]
  (let [p-range (range np)
        p-names (map #(str "Philosopher " (inc %))
                     p-range)
        p-forks (map #(vector (nth forks %)
                              (nth forks (-> % inc (mod np))))
                     p-range)
        p-food (cycle [food])]
    (map init-fn p-names p-forks p-food)))

;;; Example 2.10

(defn check-philosophers [philosophers forks]
  (doseq [i (range (count philosophers))]
    (println (str "Fork:\t\t\t available=" @(nth forks i)))
    (if-let [p @(nth philosophers i)]
      (println (str (:name p)
                    ":\t\t eating=" (:eating? p)
                    " food=" (:food p))))))

;;; Example 2.11

(defn dine-philosophers [philosophers]
  (doall (for [p philosophers]
           (future (dine p 10 100 100)))))

;;; Example 2.12

(def all-forks (init-forks 5))

(def all-philosophers
  (init-philosophers 5 1000 all-forks make-philosopher))

;; user> (check-philosophers all-philosophers all-forks)
;; Fork:                         available=true
;; Philosopher 1:                eating=false food=1000
;; Fork:                         available=true
;; Philosopher 2:                eating=false food=1000
;; Fork:                         available=true
;; Philosopher 3:                eating=false food=1000
;; Fork:                         available=true
;; Philosopher 4:                eating=false food=1000
;; Fork:                         available=true
;; Philosopher 5:                eating=false food=1000
;; nil

;; user> (def philosophers-futures (dine-philosophers all-philosophers))
;; #'user/philosophers-futures
;; user> (check-philosophers all-philosophers all-forks)
;; Fork:                         available=false
;; Philosopher 1:                eating=true food=978
;; Fork:                         available=false
;; Philosopher 2:                eating=false food=979
;; Fork:                         available=false
;; Philosopher 3:                eating=true food=977
;; Fork:                         available=false
;; Philosopher 4:                eating=false food=980
;; Fork:                         available=true
;; Philosopher 5:                eating=false food=980
;; nil

;; user> (check-philosophers all-philosophers all-forks)
;; Fork:                         available=true
;; Philosopher 1:                eating=false food=932
;; Fork:                         available=true
;; Philosopher 2:                eating=false food=935
;; Fork:                         available=true
;; Philosopher 3:                eating=false food=933
;; Fork:                         available=true
;; Philosopher 4:                eating=false food=942
;; Fork:                         available=true
;; Philosopher 5:                eating=false food=935
;; nil

;; user> (map future-cancel philosophers-futures)
;; (true true true true true)
