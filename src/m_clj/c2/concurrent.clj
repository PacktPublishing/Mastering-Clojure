(ns m-clj.c2.concurrent)

;;; Example 2.1

(def delayed-1
  (delay
   (Thread/sleep 3000)
   (println "3 seconds later ...")
   1))

;; user> (delay? delayed-1)
;; true

;; user> (realized? delayed-1)
;; false
;; user> (realized? delayed-1)           ; after 3 seconds
;; false

;; user> @delayed-1
;; 3 seconds later ...
;; 1
;; user> (realized? delayed-1)
;; true

;; NOTE @x is equivalent to (deref x)

;; user> @delayed-1
;; 1

;;; Example 2.2

(defn wait-3-seconds []
  (Thread/sleep 3000)
  (println)
  (println "3 seconds later ..."))

;; user> (.start (Thread. wait-3-seconds))
;; nil
;; user>
;; 3 seconds later ...

;; user>


;;; Example 2.3

(defn val-as-future [n secs]
  (future
    (Thread/sleep (* secs 1000))
    (println)
    (println (str secs " seconds later ..."))
    n))

;; user> (def future-1 (val-as-future 1 3))
;; #'user/future-1
;; user>
;; 3 seconds later ...

;; user>

;; user> (realized? future-1)
;; true
;; user> (future-done? future-1)
;; true

;; user> (def future-10 (val-as-future 10 10))
;; #'user/future-10
;; user> (future-cancel future-10)
;; true

;; user> (future-cancelled? future-10)
;; true
;; user> @future-10
;; CancellationException   java.util.concurrent.FutureTask.report (FutureTask.java:121)

;; user> (def p (promise))
;; #'user/p
;; user> (deliver p 100)
;; #<core$promise$reify__6363@1792b00: 100>
;; user> (deliver p 200)
;; nil
;; user> @p
;; 100

;;; Example 2.4

(defn lock-for-2-seconds []
  (let [lock (Object.)
        task-1 (fn []
                 (future
                   (locking lock
                     (Thread/sleep 2000)
                     (println "Task 1 completed"))))
        task-2 (fn []
                 (future
                   (locking lock
                     (Thread/sleep 1000)
                     (println "Task 2 completed"))))]
    (task-1)
    (task-2)))

;; user> (lock-for-2-seconds)
;; [#<core$future_call$reify__6320@19ed4e9: :pending>
;;  #<core$future_call$reify__6320@ac35d5: :pending>]
;; user>

;; Task 1 completed
;; Task 2 completed
