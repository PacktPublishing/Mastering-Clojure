(ns m-clj.c4.threading
  (:refer-clojure :exclude [-> ->> as->]))

;;; Example 4.5

(defmacro -> [x & forms]
  (loop [x x
         forms forms]
    (if forms
      (let [form (first forms)
            threaded (if (seq? form)
                       (with-meta
                         `(~(first form) ~x ~@(next form))
                         (meta form))
                       (list form x))]
        (recur threaded (next forms)))
      x)))

(defmacro ->> [x & forms]
  (loop [x x
         forms forms]
    (if forms
      (let [form (first forms)
            threaded (if (seq? form)
                       (with-meta
                         `(~(first form) ~@(next form) ~x)
                         (meta form))
                       (list form x))]
        (recur threaded (next forms)))
      x)))

;;; Example 4.6

(defn thread-form [first? x form]
  (if (seq? form)
    (let [[f & xs] form
          xs (conj (if first? xs (vec xs)) x)]
      (apply list f xs))
    (list form x)))

(defn threading [first? x forms]
  (reduce #(thread-form first? %1 %2)
          x forms))

;;; Example 4.7

(defmacro -> [x & forms]
  (threading true x forms))

(defmacro ->> [x & forms]
  (threading false x forms))

;;; Example 4.8

(defmacro as-> [expr name & forms]
  `(let [~name ~expr
         ~@(interleave (repeat name) forms)]
     ~name))

;; user> (macroexpand '(as-> 1 x (+ 1 x) (+ x 1)))
;; (let* [x 1
;;        x (+ 1 x)
;;        x (+ x 1)]
;;       x)
;; user> (as-> 1 x (+ 1 x) (+ x 1))
;; 3
