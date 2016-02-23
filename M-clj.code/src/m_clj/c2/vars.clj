(ns m-clj.c2.vars)

(def ^:dynamic *thread-local-state* [1 2 3])

;; user> (binding [*thread-local-state* [10 20]]
;;         (map #(* % %) *thread-local-state*))
;; (100 400)
;; user> (map #(* % %) *thread-local-state*)
;; (1 4 9)

;; user> (with-bindings {#'*thread-local-state* [10 20]}
;;         (map #(* % %) *thread-local-state*))
;; (100 400)
;; user> (with-bindings {(var *thread-local-state*) [10 20]}
;;         (map #(* % %) *thread-local-state*))
;; (100 400)

;; user> (def ^:dynamic *unbound-var*)
;; #'user/*unbound-var*
;; user> (thread-bound? (var *unbound-var*))
;; false
;; user> (binding [*unbound-var* 1]
;;         (thread-bound? (var *unbound-var*)))
;; true

;; user> *thread-local-state*
;; [1 2 3]
;; user> (var *thread-local-state*)
;; #'user/*thread-local-state*

;;; Example 2.5

(defn factorial [n]
  (with-local-vars [i n acc 1]
    (while (> @i 0)
      (var-set acc (* @acc @i))
      (var-set i (dec @i)))
    (var-get acc)))
