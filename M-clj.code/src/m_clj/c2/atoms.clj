(ns m-clj.c2.atoms)

(def state (atom 0))

;; user> @state
;; 0
;; user> (reset! state 1)
;; 1
;; user> @state
;; 1

;; user> @state
;; 1
;; user> (swap! state + 2)
;; 3

;;; Example 2.13

(defn make-state-with-watch []
  (let [state (atom 0)
        state-is-changed? (atom false)
        watch-fn (fn [key r old-value new-value]
                   (swap! state-is-changed? (fn [_] true)))]
    (add-watch state nil watch-fn)
    [state
     state-is-changed?]))

;; user> (def s (make-state-with-watch))
;; #'user/s
;; user> @(nth s 1)
;; false
;; user> (swap! (nth s 0) inc)
;; 1
;; user> @(nth s 1)
;; true
