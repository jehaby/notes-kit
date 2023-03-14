(ns flexi.notes-kit.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[notes-kit starting]=-"))
   :start      (fn []
                 (log/info "\n-=[notes-kit started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[notes-kit has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
