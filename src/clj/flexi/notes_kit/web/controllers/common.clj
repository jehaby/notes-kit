(ns flexi.notes-kit.web.controllers.common
  (:require
   [flexi.notes-kit.web.htmx :refer [ui page] :as htmx]))

(defn ppage [content]
  (page
   [:head
    [:meta {:charset "UTF-8"}]
    [:title "Htmx + Kit"]
    [:script {:src "https://unpkg.com/htmx.org@1.7.0/dist/htmx.min.js" :defer true}]
    [:script {:src "https://unpkg.com/hyperscript.org@0.9.5" :defer true}]]
   [:body
    content]))
