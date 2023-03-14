(ns flexi.notes-kit.env
  (:require
    [clojure.tools.logging :as log]
    [flexi.notes-kit.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[notes-kit starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[notes-kit started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[notes-kit has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})
