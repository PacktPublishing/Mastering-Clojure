(ns m-clj.c10.test
  (:require [clojure.test :refer :all]))

;;; Example 10.1

(deftest test-*
  (is (= 6 (* 2 3)))
  (is (= 4 (* 1 4)))
  (is (= 6 (* 3 2))))

(deftest test-*-with-are
  (are [x y] (= 6 (* x y))
    2 3
    1 6
    3 2))

;; user> (run-tests)

;; Testing ...

;; Ran 2 tests containing 6 assertions.
;; 0 failures, 0 errors.
;; {:test 2, :pass 6, :fail 0, :error 0, :type :summary}

;;; Example 10.2

;; (deftest test-*-fails
;;   (is (= 5 (* 2 3))))

;; user> (run-tests)

;; Testing ...

;; FAIL in (test-*-fails) (test.clj:24)
;; expected: (= 5 (* 2 3))
;;   actual: (not (= 5 6))

;; Ran 3 tests containing 7 assertions.
;; 1 failures, 0 errors.
;; {:test 3, :pass 6, :fail 1, :error 0, :type :summary}
