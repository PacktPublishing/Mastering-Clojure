(ns m-clj.c11.logging
  (:require [clojure.tools.logging :as log]))

;;; Example 11.4

(defn divide [a b]
  (log/info "Dividing" a "by" b)
  (try
    (log/spyf "Result: %s" (/ a b))
    (catch Exception e
      (log/error e "There was an error!"))))

;; user> (divide 10 1)
;; INFO  - Dividing 10 by 1
;; DEBUG - Result: 10
;; 10
;; user> (divide 10 0)
;; INFO  - Dividing 10 by 0
;; ERROR - There was an error!
;; java.lang.ArithmeticException: Divide by zero
;; at clojure.lang.Numbers.divide(Numbers.java:158) ~[clojure-1.7.0.jar:na]
;; ...
;; at java.lang.Thread.run(Thread.java:744) [na:1.7.0_45]
;; nil

;;; Example 11.5

(Thread/setDefaultUncaughtExceptionHandler
 (reify Thread$UncaughtExceptionHandler
   (uncaughtException [_ thread ex]
     (log/error ex "Uncaught exception on" (.getName thread)))))
