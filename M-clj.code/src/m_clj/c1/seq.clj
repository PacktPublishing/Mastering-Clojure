(ns m-clj.c1.seq
  (:require [clojure.xml :as xml]
            [clojure.zip :as z]))

;;; Example 1.7

(defn fibo-lazy [n]
  (->> [0N 1N]
       (iterate (fn [[a b]] [b (+ a b)]))
       (map first)
       (take n)))

;; user> (fibo-lazy 10)
;; (0N 1N 1N 2N 3N 5N 8N 13N 21N 34N)
;; user> (last (fibo-lazy 10000))
;; 207936...230626N
;; user> (time (last (fibo-lazy 10000)))
;; "Elapsed time: 18.593018 msecs"
;; 207936...230626N

;; user> (time (def fibo-xs (fibo-lazy 10000)))
;; "Elapsed time: 0.191981 msecs"
;; #'user/fibo-xs
;; user> (type fibo-xs)
;; clojure.lang.LazySeq

(def fibo-mem (memoize fibo-lazy))

;; user> (time (last (fibo-mem 10000)))
;; "Elapsed time: 19.776527 msecs"
;; 207936...230626N
;; user> (time (last (fibo-mem 10000)))
;; "Elapsed time: 2.82709 msecs"
;; 207936...230626N

;;; thinking in sequences

;; user> (def xs (cons 0 '(1 2 3)))
;; #'user/xs
;; user> (first xs)
;; 0
;; user> (rest xs)
;; (1 2 3)

;; user> (cons 0 ())
;; (0)
;; user> (cons 0 nil)
;; (0)
;; user> (rest (cons 0 nil))
;; ()

;;; using the seq library

;; (seq (rest coll)) is (next coll)

;; user> (empty? ())
;; true
;; user> (empty? nil)
;; true
;; user> (nil? ())
;; false
;; user> (nil? nil)
;; true

;; user> (= (rest ()) nil)
;; false
;; user> (= (seq ()) nil)
;; true
;; user> (= (next ()) nil)
;; true

;; user> (seq? '(1 2 3))
;; true
;; user> (seq? [1 2 3])
;; false
;; user> (seq? (seq [1 2 3]))
;; true

;; user> (sequential? '(1 2 3))
;; true
;; user> (sequential? [1 2 3])
;; true
;; user> (sequential? {:a 1 :b 2})
;; false
;; user> (sequential? #{:a :b})
;; false

;; user> (associative? '(1 2 3))
;; false
;; user> (associative? [1 2 3])
;; true
;; user> (associative? {:a 1 :b 2})
;; true
;; user> (associative? #{:a :b})
;; false

;; user> ([:a :b] 1)
;; :b
;; user> ({:a 1 :b 2} :a)
;; 1

;; user> (#{1 2 3} 1)
;; 1
;; user> (#{1 2 3} 0)
;; nil

;;; creating sequences

;; user> (conj [1 2 3] 4 5 6)
;; [1 2 3 4 5 6]
;; user> (conj '(1 2 3) 4 5 6)
;; (6 5 4 1 2 3)

;; user> (concat [1 2 3] [])
;; (1 2 3)
;; user> (concat [] [1 2 3])
;; (1 2 3)
;; user> (concat [1 2 3] [4 5 6] [7 8 9])
;; (1 2 3 4 5 6 7 8 9)

;; user> (reverse [1 2 3 4 5 6])
;; (6 5 4 3 2 1)
;; user> (reverse (reverse [1 2 3 4 5 6]))
;; (1 2 3 4 5 6)

;; user> (range 5)
;; (0 1 2 3 4)
;; user> (range 15 10 -1)
;; (15 14 13 12 11)
;; user> (range 0 10 3)
;; (0 3 6 9)

;; (range 15 10) => ()

;; user> (take 5 (range 10))
;; (0 1 2 3 4)
;; user> (drop 5 (range 10))
;; (5 6 7 8 9)

;; user> (nth (range 10) 0)
;; 0
;; user> (nth (range 10) 9)
;; 9

;; user> (repeat 10 0)
;; (0 0 0 0 0 0 0 0 0 0)
;; user> (repeat 5 :x)
;; (:x :x :x :x :x)

;; user> (repeat 5 (rand-int 100))
;; (75 75 75 75 75)
;; user> (repeatedly 5 #(rand-int 100))
;; (88 80 17 52 32)

;; user> (take 5 (cycle [0]))
;; (0 0 0 0 0)
;; user> (take 5 (cycle (range 3)))
;; (0 1 2 0 1)

;; user> (interleave [0 1 2] [3 4 5 6] [7 8])
;; (0 3 7 1 4 8)
;; user> (interleave [1 2 3] (cycle [0]))
;; (1 0 2 0 3 0)
;; user> (interpose 0 [1 2 3])
;; (1 0 2 0 3)

;; user> (take 5 (iterate inc 5))
;; (5 6 7 8 9)
;; user> (take 5 (iterate #(+ 2 %) 0))
;; (0 2 4 6 8)

;;; transforming sequences

;; user> (map inc [0 1 2 3])
;; (1 2 3 4)
;; user> (map #(* 2 %) [0 1 2 3])
;; (0 2 4 6)

;; user> (map + [0 1 2 3] [4 5 6])
;; (4 6 8)

;; user> (mapv inc [0 1 2 3])
;; [1 2 3 4]

;; user> (map-indexed (fn [i x] [i x]) "Hello")
;; ([0 \H] [1 \e] [2 \l] [3 \l] [4 \o])

;; user> (require '[clojure.string :as cs])
;; nil
;; user> (map #(cs/split % #"\d") ["aa1bb" "cc2dd" "ee3ff"])
;; (["aa" "bb"] ["cc" "dd"] ["ee" "ff"])
;; user> (mapcat #(cs/split % #"\d") ["aa1bb" "cc2dd" "ee3ff"])
;; ("aa" "bb" "cc" "dd" "ee" "ff")

;; user> (into {} [[:a 1] [:c 3] [:b 2]])
;; {:a 1, :c 3, :b 2}
;; user> (into [] {1 2 3 4})
;; [[1 2] [3 4]]

;; user> (into [1 2 3] '(4 5 6))
;; [1 2 3 4 5 6]
;; user> (into '(1 2 3) '(4 5 6))
;; (6 5 4 1 2 3)

;; user> (reduce + [1 2 3 4 5])
;; 15
;; user> (reduce + [])
;; 0
;; user> (reduce + 1 [])
;; 1

;; user> (for [x (range 3 7)]
;;         (* x x))
;; (9 16 25 36)
;; user> (for [x [0 1 2 3 4 5]
;;             :let [y (* x 3)]
;;             :when (even? y)]
;;         y)
;; (0 6 12)

;; user> (for [x ['a 'b 'c]
;;             y [1 2 3]]
;;         [x y])
;; ([a 1] [a 2] [a 3] [b 1] [b 2] [b 3] [c 1] [c 2] [c 3])

;; user> (doseq [x (range 3 7)]
;;         (* x x))
;; nil
;; user> (doseq [x (range 3 7)]
;;         (println (* x x)))
;; 9
;; 16
;; 25
;; 36
;; nil

;; user> (partition 2 (range 11))
;; ((0 1) (2 3) (4 5) (6 7) (8 9))
;; user> (partition-all 2 (range 11))
;; ((0 1) (2 3) (4 5) (6 7) (8 9) (10))

;; user> (partition 3 2 (range 11))
;; ((0 1 2) (2 3 4) (4 5 6) (6 7 8) (8 9 10))
;; user> (partition-all 3 2 (range 11))
;; ((0 1 2) (2 3 4) (4 5 6) (6 7 8) (8 9 10) (10))

;; user> (partition 3 (range 11))
;; ((0 1 2) (3 4 5) (6 7 8))
;; user> (partition 3 3 (range 11 12) (range 11))
;; ((0 1 2) (3 4 5) (6 7 8) (9 10 11))
;; user> (partition 3 3 (range 11 15) (range 11))
;; ((0 1 2) (3 4 5) (6 7 8) (9 10 11))
;; user> (partition 3 4 (range 11 12) (range 11))
;; ((0 1 2) (4 5 6) (8 9 10))

;; user> (partition-by #(= 0 %) [-2 -1 0 1 2])
;; ((-2 -1) (0) (1 2))
;; user> (partition-by identity [-2 -1 0 1 2])
;; ((-2) (-1) (0) (1) (2))

;; user> (sort [3 1 2 0])
;; (0 1 2 3)
;; user> (sort > [3 1 2 0])
;; (3 2 1 0)
;; user> (sort ["Carol" "Alice" "Bob"])
;; ("Alice" "Bob" "Carol")

;; user> (sort #(compare (first %1) (first %2)) [[1 1] [2 2] [3 3]])
;; ([1 1] [2 2] [3 3])
;; user> (sort-by first [[1 1] [2 2] [3 3]])
;; ([1 1] [2 2] [3 3])
;; user> (sort-by first > [[1 1] [2 2] [3 3]])
;; ([3 3] [2 2] [1 1])

;;; filtering sequences

;; user> (keep #(if (odd? %) %) (range 10))
;; (1 3 5 7 9)
;; user> (keep seq [() [] '(1 2 3) [:a :b] nil])
;; ((1 2 3) (:a :b))

;; user> (keep {:a 1, :b 2, :c 3} [:a :b :d])
;; (1 2)
;; user> (keep #{0 1 2 3} #{2 3 4 5})
;; (3 2)

;; user> (filter even? (range 10))
;; (0 2 4 6 8)
;; user> (filterv even? (range 10))
;; [0 2 4 6 8]

;; user> (keep #(if (odd? %) %) (range 10))
;; (1 3 5 7 9)
;; user> (for [x (range 10) :when (odd? x)] x)
;; (1 3 5 7 9)
;; user> (filter odd? (range 10))
;; (1 3 5 7 9)

;; user> (subvec [0 1 2 3 4 5] 3)
;; [3 4 5]
;; user> (subvec [0 1 2 3 4 5] 3 5)
;; [3 4]

;; user> (select-keys {:a 1 :b 2} [:a])
;; {:a 1}
;; user> (select-keys {:a 1 :b 2 :c 3} [:a :c])
;; {:c 3, :a 1}

;; user> (find {:a 1 :b 2} :a)
;; [:a 1]

;; user> (take-while neg? [-2 -1 0 1 2])
;; (-2 -1)
;; user> (drop-while neg? [-2 -1 0 1 2])
;; (0 1 2)

;;; lazy sequences

;;; Example 1.8

(defn fibo-cons [a b]
  (cons a (lazy-seq (fibo-cons b (+ a b)))))

;; user> (def fibo (fibo-cons 0N 1N))
;; #'user/fibo
;; user> (take 2 fibo)
;; (0N 1N)
;; user> (take 11 fibo)
;; (0N 1N 1N 2N 3N 5N 8N 13N 21N 34N 55N)
;; user> (last (take 10000 fibo))
;; 207936...230626N

;;; Example 1.9

(def fibo-seq
  (lazy-cat [0N 1N] (map + fibo-seq (rest fibo-seq))))

;; user> (first fibo-seq)
;; 0N
;; user> (nth fibo-seq 1)
;; 1N
;; user> (nth fibo-seq 10)
;; 55N
;; user> (nth fibo-seq 9999)
;; 207936...230626N

;; user> (def xs (map println (range 3)))
;; #'user/xs
;; user> xs
;; 0
;; 1
;; 2
;; (nil nil nil)

;; user> (def xs (doall (map println (range 3))))
;; 0
;; 1
;; 2
;; #'user/xs
;; user> xs
;; (nil nil nil)
