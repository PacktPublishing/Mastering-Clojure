(ns m-clj.c4.read-and-eval)

;; user> (read (-> "(list 1 2 3)"
;;                 .toCharArray
;;                 java.io.CharArrayReader.
;;                 java.io.PushbackReader.))
;; (list 1 2 3)

;; user> (read-string "(list 1 2 3)")
;; (list 1 2 3)

;; user> (eval '(list 1 2 3))
;; (1 2 3)
;; user> (eval (list + 1 2 3))
;; 6
;; user> (eval (read-string "(+ 1 2 3)"))
;; 6

;; user> (read (-> "#=(list 1 2 3)"
;;                 .toCharArray
;;                 java.io.CharArrayReader.
;;                 java.io.PushbackReader.))
;; (1 2 3)
;; user> (read-string "#=(list 1 2 3)")
;; (1 2 3)

;; user> #=(list + 1 2 3)
;; 6
;; user> (read-string "#=(list + 1 2 3)")
;; (+ 1 2 3)
;; user> (read-string "#=#=(list + 1 2 3)")
;; 6

;; user> (binding [*read-eval* false]
;;         (read-string (read-string "#=(list 1 2 3)")))
;; RuntimeException EvalReader not allowed when *read-eval* is false.  clojure.lang.Util.runtimeException (Util.java:221)

;; user> (load-string "(+ 1 2 3)")
;; 6

;; user> (eval (read-string "(println 1) (println 2)"))
;; 1
;; nil
;; user> (load-string "(println 1) (println 2)")
;; 1
;; 2
;; nil
