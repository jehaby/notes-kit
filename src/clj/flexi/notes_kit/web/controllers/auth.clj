(ns flexi.notes-kit.web.controllers.auth
  (:require
   [flexi.notes-kit.web.htmx :refer [ui page] :as htmx]
   [flexi.notes-kit.web.controllers.common :refer [ppage]]))

(defn get-login-form [req]
  (ppage
   [:form
    {:hx-post "/auth/login"
     :hx-swap= "outerHTML"}
    [:input {:name "email"
             :type "email"}]

    [:input {:name "password"
             :type "password"}]

    [:button "ok"]]))

(defn post-login [req]
  (ui
   [:div "you logged in TODO redirect"]))



