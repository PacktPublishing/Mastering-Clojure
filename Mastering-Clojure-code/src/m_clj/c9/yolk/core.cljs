(ns m-clj.c9.yolk
  (:require [m-clj.c9.common :refer [by-id set-html!]]
            [yolk.bacon :as y]))

;;; Example 9.9

(defn render-philosophers [philosophers]
  (apply str
         (for [p (reverse philosophers)]
           (str "<div>" p "</div>"))))

(defn philosopher-fn [i n forks philosophers wait-ms]
  (let [p (nth philosophers i)
        fork-1 (nth forks i)
        fork-2 (nth forks (-> i inc (mod n)))]
    (fn []
      (js/setTimeout
       (fn []
         (y/push fork-1 :fork)
         (y/push fork-2 :fork)
         (y/push p {}))
       wait-ms)
      (str "Philosopher " (inc i) " ate!"))))

;;; Example 9.10

(let [out (by-id "ex-9-10-out")
      n 5
      [f1 f2 f3 f4 f5 :as forks] (repeatedly n #(y/bus))
      [p1 p2 p3 p4 p5 :as philosophers] (repeatedly n #(y/bus))
      eat #(philosopher-fn % n forks philosophers 1000)
      events (y/when [p1 f1 f2] (eat 0)
                     [p2 f2 f3] (eat 1)
                     [p3 f3 f4] (eat 2)
                     [p4 f4 f5] (eat 3)
                     [p5 f5 f1] (eat 4))]
  (-> events
      (y/sliding-window n)
      (y/on-value
       #(set-html! out (render-philosophers %))))
  (doseq [f forks]
    (y/push f :fork))
  (doseq [p philosophers]
    (y/push p {})))
