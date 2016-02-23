(ns m-clj.c4.defmacro)

;;; Example 4.1

(defmacro to-infix [expr]
  (interpose (first expr) (rest expr)))

(defmacro to-postfix [expr]
  (concat (rest expr) [(first expr)]))

;; user> (macroexpand '(to-infix (+ 0 1 2)))
;; (0 + 1 + 2)
;; user> (macroexpand '(to-postfix (+ 0 1 2)))
;; (0 1 2 +)

;; user> (fn? @#'to-infix)
;; true
;; user> (fn? @#'to-postfix)
;; true

;; user> (symbol 'x)
;; x
;; user> (symbol "x")
;; x
;; user> (symbol "my-namespace" "x")
;; my-namespace/x

;; user> (gensym)
;; G__8090
;; user> (gensym 'x)
;; x8081
;; user> (gensym "x")
;; x8084
;; user> (gensym :x)
;; :x8087

;; user> `(let [x# 10] x#)
;; (clojure.core/let [x__8561__auto__ 10]
;;   x__8561__auto__)
;; user> (macroexpand `(let [x# 10] x#))
;; (let* [x__8910__auto__ 10]
;;   x__8910__auto__)

;;; Example 4.2

(defmacro to-list [x]
  `(list ~x))

(defmacro to-list-with-capture [x]
  `(list ~'x))

;; user> (let [x 10]
;;         (to-list 20))
;; (20)
;; user> (let [x 10]
;;         (to-list-with-capture 20))
;; (10)

;;; Example 4.3

(defmacro to-list-with-error [x]
  `(let [y ~x]
     (list y)))

;; user> (to-list-with-error 10)
;; CompilerException java.lang.RuntimeException: Can't let qualified name: user/y

;;; Example 4.4

(defmacro to-list-with-gensym [x]
  `(let [y# ~x]
     (list y#)))

;; user> (to-list-with-gensym 10)
;; (10)
