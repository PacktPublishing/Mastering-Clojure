(ns m-clj.c9.rx
  (:require [rx.lang.clojure.core :as rx]
            [rx.lang.clojure.blocking :as rxb]
            [rx.lang.clojure.interop :as rxj]))

;; user> (def o (rx/return 0))
;; #'user/o
;; user> (rxb/into [] o)
;; [0]
;; user> (rxb/first o)
;; 0

;; user> (def o (rx/seq->o [1 2 3]))
;; #'user/o
;; user> (rxb/o->seq o)
;; (1 2 3)

;; user> (def o (rx/cons 0 (rx/empty)))
;; #'user/o
;; user> (rxb/first o)
;; 0

;;; Example 9.4

(def observer
  (reify rx.Observer
    (onNext [this v] (println (str "Got value: " v "!")))
    (onError [this e] (println e))
    (onCompleted [this] (println "Done!"))))

;;; Example 9.5

(defn make-observable []
  (rx/observable* (fn [s]
                    (-> s
                        (rx/on-next :a)
                        (rx/on-next :b)
                        rx/on-completed))))

;; user> (def o (make-observable))
;; #'user/o
;; user> (rxb/into [] o)
;; [:a :b]

;;; Example 9.6

(defn rx-inc [o]
  (rx/subscribe o (fn [v] (println (str "Got value: " v "!"))))
  (rx/map inc o))

;; user> (def o (rx/seq->o [0 1 2]))
;; #'user/o
;; user> (rx-inc o)
;; Got value: 0!
;; Got value: 1!
;; Got value: 2!
;; #<rx.Observable 0xc3fae8>

;;; Example 9.7

(defn rxj-inc [o]
  (.subscribe o (rxj/action [v]
                  (println (str "Got value: " v "!"))))
  (.map o (rxj/fn [v] (inc v))))

;; user> (def o (rx.Observable/from [0 1 2]))
;; #'user/o
;; user> (rxj-inc o)
;; Got value: 0!
;; Got value: 1!
;; Got value: 2!
;; #<rx.Observable 0x16459ef>

;; user> (rxb/into [] (->> (rx/range)
;;                         (rx/take 10)))
;; [0 1 2 3 4 5 6 7 8 9]
;; user> (rxb/into [] (->> (rx/cycle (rx/return 1))
;;                         (rx/take 5)))
;; [1 1 1 1 1]

;; user> (rxb/into [] (->> (rx/seq->o [:a :b :c :d :e])
;;                         (rx/filter #{:b :c})))
;; [:b :c]

;;; Example 9.8

(defn group-maps [ms]
  (->> ms
       (rx/seq->o)
       (rx/group-by :k)
       (rx/mapcat (fn [[k vs :as me]]
                    (rx/map #(vector k %) vs)))
       (rxb/into [])))

;; user> (group-maps [{:k :a :v 1}
;;                    {:k :b :v 2}
;;                    {:k :a :v 3}
;;                    {:k :c :v 4}])
;; [[:a {:k :a, :v 1}]
;;  [:a {:k :a, :v 3}]
;;  [:b {:k :b, :v 2}]
;;  [:c {:k :c, :v 4}]]

;; user> (let [o1 (rx/seq->o (range 5))
;;             o2 (rx/seq->o (range 5 10))
;;             o (rx/merge o1 o2)]
;;         (rxb/into [] o))
;; [0 1 2 3 4 5 6 7 8 9]

;; user> (->> (range 6)
;;            rx/seq->o
;;            (rx/split-with (partial >= 3))
;;            rxb/first
;;            (map (partial rxb/into [])))
;; ([0 1 2 3] [4 5])
