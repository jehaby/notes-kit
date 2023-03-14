(ns flexi.notes-kit.web.controllers.notes
  (:require
   [flexi.notes-kit.web.htmx :refer [ui page] :as htmx]
   [flexi.notes-kit.web.controllers.common :refer [ppage]]))

(def note-id-1  #uuid "7d94952c-bc22-499f-8a73-30118d2d2d22")

(defonce notes-db
  (atom
   [{:id     note-id-1
     :name    "Our first note "
     :content "htmx is pretty nice btw"}]))

(defn get-list [req]
  (ppage
   [:div

    [:div
     [:h3 "Team notes"]
     (for [{:keys [name content]} @notes-db]
       [:div
        [:h4 name]
        [:p content]])]

    [:div
     [:h3 "Create new note"]
     [:form
      {:hx-post "/notes"}
      [:input
       {:name "name"
        :type "text"
        :placeholder "Note name"}]

      [:button "Create new note"]]]]))

(defn get-form [req]
  (let [note-id (-> req :path-params :id parse-uuid)
        {:keys [name content]} (->> @notes-db
                                    (filter #(= note-id (:id %)))
                                    first)]
    (ppage
     (if name
       [:form
        {:hx-put (str "/notes/" note-id)}
        [:input {:name "name"
                 :type :text
                 :value name}]

        [:input {:name "content"
                 :type :textfield
                 :placeholder "note content"
                 :value content}]

        [:button "update note"]]

       [:div "note not found"]))))

(defn post-create [req]
  (let [new-id (random-uuid)]
    (swap! notes-db conj
           {:id new-id
            :name (-> req :params :name)})
    {:status 200
     :headers {"HX-Redirect" (str "/notes/" new-id)}}))

(defn put-update [req]
  (let [note-id (-> req :path-params :id parse-uuid)
        new-note (:form-params req)]
    (swap! notes-db
           (fn [notes]
             (conj
              (remove #(= (:id %) note-id) notes)
              {:id note-id
               :name (get new-note "name")
               :content (get new-note "content")})))
    {:status 200
     :headers {"HX-Redirect" (str "/notes/" note-id)}}))

(comment
  @notes-db

;-)
  )
