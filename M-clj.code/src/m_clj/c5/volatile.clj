(ns m-clj.c5.volatile)

;; user> (def v (volatile! 0))
;; #'user/v
;; user> @v
;; 0
;; user> (vreset! v 1)
;; 1

;; user> (vswap! v inc)
;; 2
;; user> (vswap! v + 3)
;; 5
