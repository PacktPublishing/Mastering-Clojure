(ns m-clj.c10.speclj
  (:require [speclj.core :refer :all]))

;;; Example 10.8

(describe "*"
  (it "2 times 3 is 6"
    (should (= 6 (* 2 3)))))

;;; Example 10.9

(describe "/"
  (it "5 divided by 5 is 1"
    (should= 1 (/ 5 5)))
  (it "5 divided by 5 is not 0"
    (should-not= 0 (/ 5 5)))
  (it "fail if 5 divided by 5 is not 1"
    (if (not= 1 (/ 5 5))
      (should-fail "divide not working")))
  (it "throw an error if 5 is divided by 0"
    (should-throw ArithmeticException
      (/ 5 0))))

(defn echo []
  (let [s (read-line)]
    (println (str "Echo: " s))))

(describe "echo"
  (it "reads a line and prints it"
    (should= "Echo: Hello!\r\n"
      (with-out-str
        (with-in-str "Hello!"
          (echo))))))

(describe "echo"
  (it "reads a line and prints it"
    (with-redefs [read-line (fn [] "Hello!")
                  println (fn [x] x)]
      (should= "Echo: Hello!" (echo)))))

;; user> (run-specs)
;; ...

;; Finished in 0.00547 seconds
;; 7 examples, 0 failures
;; #<speclj.run.standard.StandardRunner 0x10999>
