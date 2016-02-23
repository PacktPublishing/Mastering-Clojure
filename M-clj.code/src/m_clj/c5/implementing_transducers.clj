(ns m-clj.c5.implementing-transducers
  (:refer-clojure :exclude [map filter take-while take transduce]))

;;; Example 5.4

(defn map
  ([f]
   (fn [step]
     (fn
       ([] (step))
       ([result] (step result))
       ([result input]
        (step result (f input))))))
  ([f coll]
   (sequence (map f) coll)))

;;; Example 5.5

(defn filter
  ([p?]
   (fn [step]
     (fn
       ([] (step))
       ([result] (step result))
       ([result input]
        (if (p? input)
          (step result input)
          result)))))
  ([p? coll]
   (sequence (filter p?) coll)))

;; user> (def r (reduced 0))
;; #'user/r
;; user> (reduced? r)
;; true

;;; Example 5.6

(defn rf [result input]
  (if (< result 100)
    (+ result input)
    (reduced :too-big)))

;; user> (reduce rf (range 3))
;; 3
;; user> (reduce rf (range 100))
;; :too-big

;;; Example 5.7

(defn take-while [p?]
  (fn [step]
    (fn
      ([] (step))
      ([result] (step result))
      ([result input]
       (if (p? input)
         (step result input)
         (reduced result))))))

;;; Example 5.8

(defn take [n]
  (fn [step]
    (let [nv (volatile! n)]
      (fn
        ([] (step))
        ([result] (step result))
        ([result input]
         (let [n @nv
               nn (vswap! nv dec)
               result (if (pos? n)
                        (step result input)
                        result)]
           (if (not (pos? nn))
             (ensure-reduced result)
             result)))))))

;;; Example 5.9

(defn transduce
  ([xform f coll] (transduce xform f (f) coll))
  ([xform f init coll]
   (let [xf (xform f)
         ret (if (instance? clojure.lang.IReduceInit coll)
               (.reduce ^clojure.lang.IReduceInit coll xf init)
               (clojure.core.protocols/coll-reduce coll xf init))]
     (xf ret))))
