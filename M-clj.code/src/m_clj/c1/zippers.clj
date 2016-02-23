(ns m-clj.c1.zippers
  (:require [clojure.xml :as xml]
            [clojure.zip :as z]))

(def tree [:a [1 2 3] :b :c])

(def root (z/vector-zip tree))

;; user> (def tree-nodes (iterate z/next root))
;; #'user/tree-nodes
;; user> (nth tree-nodes 0)
;; [[:a [1 2 3] :b :c] nil]
;; user> (nth tree-nodes 1)
;; [:a {:l [], :pnodes ... }]
;; user> (nth tree-nodes 2)
;; [[1 2 3] {:l [:a], :pnodes ... }]
;; user> (nth tree-nodes 3)
;; [1 {:l [], :pnodes ... }]

;; user> (-> root z/down)
;; [:a {:l [], :pnodes ... }]
;; user> (-> root z/down z/right)
;; [[1 2 3] {:l [:a], :pnodes ... }]
;; user> (-> root z/down z/right z/up)
;; [[:a [1 2 3] :b :c] nil]
;; user> (-> root z/down z/right z/right)
;; [:b {:l [:a [1 2 3]], :pnodes ... }]
;; user> (-> root z/down z/right z/left)
;; [:a {:l [], :pnodes ... }]

;; user> (-> root z/down z/right z/right z/node)
;; :b
;; user> (-> root z/down z/right z/left z/node)
;; :a

;; user> (-> root z/down z/rightmost z/node)
;; :c
;; user> (-> root z/down z/rightmost z/leftmost z/node)
;; :a

;; user> (-> root z/down z/rights)
;; ([1 2 3] :b :c)
;; user> (-> root z/down z/lefts)
;; nil

;; user> (-> root z/down z/right z/root)
;; [:a [1 2 3] :b :c]
;; user> (-> root z/down z/right r/left z/root)
;; [:a [1 2 3] :b :c]

;; user> (def e (-> root z/down z/right z/down))
;; #'user/e
;; user> (z/node e)
;; 1
;; user> (z/path e)
;; [[:a [1 2 3] :b :c]
;;  [1 2 3]]

;; user> (-> root (z/insert-child :d) z/root)
;; [:d :a [1 2 3] :b :c]
;; user> (-> root z/down z/right (z/insert-child 0) z/root)
;; [:a [0 1 2 3] :b :c]

;; user> (-> root z/down z/remove z/root)
;; [[1 2 3] :b :c]
;; user> (-> root z/down (z/replace :d) z/root)
;; [:d [1 2 3] :b :c]

;;; from resources/data/sample.xml
;;
;; <countries>
;;   <country name="England">
;;     <city>Birmingham</city>
;;     <city>Leeds</city>
;;     <city capital="true">London</city>
;;   </country>
;;   <country name="Germany">
;;     <city capital="true">Berlin</city>
;;     <city>Frankfurt</city>
;;     <city>Munich</city>
;;   </country>
;;   <country name="France">
;;     <city>Cannes</city>
;;     <city>Lyon</city>
;;     <city capital="true">Paris</city>
;;   </country>
;; </countries>
;;

;;; Example 1.10

(defn is-capital-city? [n]
  (and (= (:tag n) :city)
       (= "true" (:capital (:attrs n)))))

(defn find-capitals [file-path]
  (let [xml-root (z/xml-zip (xml/parse file-path))
        xml-seq (iterate z/next (z/next xml-root))]
    (->> xml-seq
         (take-while #(not= (z/root xml-root) (z/node %)))
         (map z/node)
         (filter is-capital-city?)
         (mapcat :content))))

;; user> (find-capitals "resources/data/sample.xml")
;; ("London" "Berlin" "Paris")
