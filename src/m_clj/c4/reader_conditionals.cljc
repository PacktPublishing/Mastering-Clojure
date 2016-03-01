(ns m-clj.c4.reader-conditionals)

;; user> (read-string {:read-cond :allow :features #{:clj}}
;;                    "#?(:cljs \"ClojureScript\" :clj \"Clojure\")")
;; "Clojure"
;; user> (read-string {:read-cond :allow :features #{:cljs}}
;;                    "#?(:cljs \"ClojureScript\" :clj \"Clojure\")")
;; "ClojureScript"

;; user> (read-string {:read-cond :allow :features #{:clr}}
;;                    "[1 2 #?@(:cljs [3 4] :default [5 6])]")
;; [1 2 5 6]
;; user> (read-string {:read-cond :allow :features #{:clj}}
;;                    "[1 2 #?@(:cljs [3 4] :default [5 6])]")
;; [1 2 5 6]
;; user> (read-string {:read-cond :allow :features #{:cljs}}
;;                    "[1 2 #?@(:cljs [3 4] :default [5 6])]")
;; [1 2 3 4]

;; user> (read-string {:read-cond :preserve}
;;                    "[1 2 #?@(:cljs [3 4] :clj [5 6])]")
;; [1 2 #?@(:cljs [3 4] :clj [5 6])]

;;; Example 4.9

(defmacro get-milliseconds-since-epoch []
  `(.getTime #?(:cljs (js/Date.)
                :clj (java.util.Date.))))

;; user> (macroexpand '(get-milliseconds-since-epoch))
;; (. (java.util.Date.) getTime)
