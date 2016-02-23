(ns m-clj.c1.recur)

;;; Example 1.1

(defn fibo
  ([n]
   (fibo [0N 1N] n))
  ([xs n]
   (if (<= n (count xs))
     xs
     (let [x' (+ (last xs)
                 (nth xs (- (count xs) 2)))
           xs' (conj xs x')]
       (fibo xs' n)))))

;; user> (fibo 10)
;; [0N 1N 1N 2N 3N 5N 8N 13N 21N 34N]
;; user> (last (fibo 100))
;; 218922995834555169026N
;; user> (last (fibo 10000))
;; StackOverflowError   clojure.lang.Numbers.lt (Numbers.java:219)

;;; Example 1.2

(defn fibo-recur
  ([n]
   (fibo-recur [0N 1N] n))
  ([xs n]
   (if (<= n (count xs))
     xs
     (let [x' (+ (last xs)
                 (nth xs (- (count xs) 2)))
           xs' (conj xs x')]
       (recur xs' n)))))

;; user> (fibo-recur 10)
;; [0N 1N 1N 2N 3N 5N 8N 13N 21N 34N]
;; user> (last (fibo-recur 10000))
;; 207936...230626N

;; user> (time (last (fibo-recur 10000)))
;; "Elapsed time: 1320.050942 msecs"
;; 207936...230626N

;;; Example 1.3

(defn fibo-loop [n]
  (loop [xs [0N 1N]
         n n]
    (if (<= n (count xs))
      xs
      (let [x' (+ (last xs)
                  (nth xs (- (count xs) 2)))
            xs' (conj xs x')]
        (recur xs' n)))))

;; user> (fibo-loop 10)
;; [0N 1N 1N 2N 3N 5N 8N 13N 21N 34N]
;; user> (last (fibo-loop 10000))
;; 207936...230626N

;;; Example 1.4

(defn fibo-trampoline [n]
  (letfn [(fibo-fn [xs n]
            (if (<= n (count xs))
              xs
              (let [x' (+ (last xs)
                          (nth xs (- (count xs) 2)))
                    xs' (conj xs x')]
                #(fibo-fn xs' n))))]
    (trampoline fibo-fn [0N 1N] n)))

;; user> (fibo-trampoline 10)
;; [0N 1N 1N 2N 3N 5N 8N 13N 21N 34N]
;; user> (time (last (fibo-trampoline 10000)))
;; "Elapsed time: 1346.629108 msecs"
;; 207936...230626N

;;; NOTE
;; fibo-trampoline does NOT consume stack space

;;; Example 1.5

(defn sqrt-div2-recur [n]
  (letfn [(sqrt [n]
            (if (< n 1)
              n
              (div2 (Math/sqrt n))))
          (div2 [n]
            (if (< n 1)
              n
              (sqrt (/ n 2))))]
    (sqrt n)))

;;; Example 1.6

(defn sqrt-div2-trampoline [n]
  (letfn [(sqrt [n]
            (if (< n 1)
              n
              #(div2 (Math/sqrt n))))
          (div2 [n]
            (if (< n 1)
              n
              #(sqrt (/ n 2))))]
    (trampoline sqrt n)))

;; user> (time (sqrt-div2-recur 10000000000N))
;; "Elapsed time: 0.327439 msecs"
;; 0.5361105866719398
;; user> (time (sqrt-div2-trampoline 10000000000N))
;; "Elapsed time: 0.326081 msecs"
;; 0.5361105866719398
