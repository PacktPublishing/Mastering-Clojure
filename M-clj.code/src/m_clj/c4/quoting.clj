(ns m-clj.c4.quoting)

;; user> 'x
;; x
;; user> (quote x)
;; x

;;; other data structures can be quoted as well

;; user> (def a 1)
;; #'user/a
;; user> `(list ~a 2 3)
;; (clojure.core/list 1 2 3)
;; user> `(list ~@[1 2 3])
;; (clojure.core/list 1 2 3)

;; user> (def a 1)
;; #'user/a
;; user> `(list ~a 2 3)
;; (clojure.core/list 1 2 3)
;; user> '(list ~a 2 3)
;; (list (clojure.core/unquote a) 2 3)

;; user> `(vector x y z)
;; (clojure.core/vector user/x user/y user/z)
;; user> `(vector ~'x ~'y ~'z)
;; (clojure.core/vector x y z)

;; user> `[1 :b ~(+ 1 2)]
;; [1 :b 3]
;; user> `[1 :b '~(+ 1 2)]
;; [1 :b (quote 3)]
;; user> `[1 ~'b ~(+ 1 2)]
;; [1 b 3]

;; user> (def ops ['first 'second])
;; #'user/ops
;; user> `{:a (~(nth ops 0) ~'xs)
;;         :b (~(nth ops 1) ~'xs)}
;; {:b (second xs),
;;  :a (first xs)}
