(ns m-clj.c9.om.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [m-clj.c9.common :refer [by-id]]))

;;; Example 9.22

(defn update-input-value-fn [owner]
  (fn [e]
    (let [target (.-target e)
          val (.-value target)
          id (keyword (.-id target))]
      (om/set-state! owner id val))))

(defn input-field [text owner attrs]
  (let [handler (update-input-value-fn owner)
        event-attr {:onChange handler}
        js-attrs (-> attrs (merge event-attr) clj->js)]
    (dom/div
     nil
     (dom/div nil text)
     (dom/input js-attrs))))

;;; Example 9.23

(defn form [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:username "" :password ""})
    om/IRenderState
    (render-state [_ state]
      (dom/form
       nil
       (input-field "Username" owner
                    {:type "text"
                     :id "username"
                     :value (:username state)})
       (input-field "Password" owner
                    {:type "password"
                     :id "password"
                     :value (:password state)})
       (dom/br nil)
       (dom/input
        #js {:type "submit"
             :value "Login"
             :disabled (or (-> state :username empty?)
                           (-> state :password empty?))})))))

(om/root form nil {:target (by-id "ex-9-23")})
