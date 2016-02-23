(ns m-clj.c9.reactive.core
  (:require [goog.events :as events]
            [goog.events.EventType]
            [goog.style :as style]
            [cljs.core.async :as a]
            [yolk.bacon :as y]
            [m-clj.c9.common :refer [by-id set-html!]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop alt!]]))

;;; Example 9.11

(defn render-div [q]
  (apply str
         (for [p (reverse q)]
           (str "<div class='proc-" p "'>Process " p "</div>"))))

(defn start-process [v t]
  (let [c (a/chan)]
    (go (while true
          (a/<! (a/timeout t))
          (a/>! c v)))
    c))

(let [out (by-id "ex-9-11-out")
      c1 (start-process 1 250)
      c2 (start-process 2 1000)
      c3 (start-process 3 1500)
      c (a/merge [c1 c2 c3])
      firstn (fn [v n]
               (if (<= (count v) n)
                 v
                 (subvec v (- (count v) n))))]
  (go-loop [q []]
    (set-html! out (render-div q))
    (recur (-> (conj q (a/<! c))
               (firstn 10)))))

;;; Example 9.12

(let [out (by-id "ex-9-12-out")
      events [(y/interval 250 1)
              (y/interval 1000 2)
              (y/interval 1500 3)]]
  (-> events
      y/merge-all
      (y/sliding-window 10)
      (y/on-value
       #(set-html! out (render-div %)))))

;;; Example 9.13

(defn listen
  ([el type] (listen el type nil))
  ([el type f] (listen el type f (a/chan)))
  ([el type f out]
   (events/listen el type
                  (fn [e] (when f (f e)) (a/put! out e)))
   out))

;;; Example 9.14

(defn offset [el]
  [(style/getPageOffsetLeft el) (style/getPageOffsetTop el)])

(let [el (by-id "ex-9-14")
      out (by-id "ex-9-14-out")
      events-chan (listen el goog.events.EventType.MOUSEMOVE)
      [left top] (offset el)
      location (fn [e]
                 {:x (+ (.-offsetX e) (int left))
                  :y (+ (.-offsetY e) (int top))})]
  (go-loop []
    (if-let [e (a/<! events-chan)]
      (let [loc (location e)]
        (set-html! out (str (:x loc) ", " (:y loc)))
        (recur)))))

;;; Example 9.15

(let [el (by-id "ex-9-15")
      out (by-id "ex-9-15-out")
      events (y/from-event-target el "mousemove")]
  (-> events
      (y/map (juxt (fn [e] (.-pageX e))
                   (fn [e] (.-pageY e))))
      (y/map (fn [[x y]] (str x ", " y)))
      (y/on-value
       #(set-html! out %))))

;;; Example 9.16

(defn chan-search [kind]
  (fn [query]
    (go
      (a/<! (a/timeout (rand-int 100)))
      [kind query])))

(def chan-web1 (chan-search :web1))
(def chan-web2 (chan-search :web2))
(def chan-image1 (chan-search :image1))
(def chan-image2 (chan-search :image2))
(def chan-video1 (chan-search :video1))
(def chan-video2 (chan-search :video2))

;;; Example 9.17

(defn chan-search-all [query & searches]
  (let [cs (for [s searches]
             (s query))]
    (-> cs vec a/merge)))

(defn chan-search-fastest [query]
  (let [t (a/timeout 80)
        c1 (chan-search-all query chan-web1 chan-web2)
        c2 (chan-search-all query chan-image1 chan-image2)
        c3 (chan-search-all query chan-video1 chan-video2)
        c (a/merge [c1 c2 c3])]
    (go (loop [i 0
               ret []]
          (if (= i 3)
            ret
            (recur (inc i)
                   (conj ret (alt!
                               [c t] ([v] v)))))))))

;;; Example 9.18

(let [out (by-id "ex-9-18-out")
      button (by-id "search-1")
      c (listen button goog.events.EventType.CLICK)]
  (go (while true
        (let [e (a/<! c)
              result (a/<! (chan-search-fastest "channels"))
              s (str result)]
          (set-html! out s)))))

;;; Example 9.19

(defn frp-search [kind]
  (fn [query]
    (y/later (rand-int 100) [kind query])))

(def frp-web1 (frp-search :web1))
(def frp-web2 (frp-search :web2))
(def frp-image1 (frp-search :image1))
(def frp-image2 (frp-search :image2))
(def frp-video1 (frp-search :video1))
(def frp-video2 (frp-search :video2))

;;; Example 9.20

(defn frp-search-all [query & searches]
  (let [results (map #(% query) searches)
        events (cons (y/later 80 "nil") results)]
    (-> (apply y/merge events)
        (y/take 1))))

(defn frp-search-fastest [query]
  (y/combine-as-array
   (frp-search-all query frp-web1 frp-web2)
   (frp-search-all query frp-image1 frp-image2)
   (frp-search-all query frp-video1 frp-video2)))

;;; Example 9.21

(let [out (by-id "ex-9-21-out")
      button (by-id "search-2")
      events (y/from-event-target button "click")]
  (-> events
      (y/flat-map-latest #(frp-search-fastest "events"))
      (y/on-value
       #(set-html! out %))))
