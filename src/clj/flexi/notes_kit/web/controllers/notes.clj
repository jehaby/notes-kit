(ns flexi.notes-kit.web.controllers.notes
  (:require
   [flexi.notes-kit.web.htmx :refer [ui page] :as htmx]
   [flexi.notes-kit.web.controllers.common :refer [ppage]]))

(def note-id-1  #uuid "7d94952c-bc22-499f-8a73-30118d2d2d22")

(defonce notes-db
  (atom
   [{:id     note-id-1
     :name    "Our first note "
     :content "htmx is pretty nice btw"}
    {:id     (random-uuid)
     :name    "2nd note"
     :content "Clojure rocks"}
    {:id     (random-uuid)
     :name    "Hi there"
     :content "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"}]))

(defn get-list [req]
  (ppage
   [:div

    [:div
     [:h3 "Team notes"]
     (if-let [notes (seq @notes-db)]

       (for [{:keys [id name content]} notes
             :let [uri (str "/notes/" id)]]
         [:div
          [:a {:href uri} "Edit"]
          [:button
           {:hx-delete uri
            :hx-confirm "you sure?"}
           "Delete"]
          [:h4 name]
          [:p content]])

       [:div "no notes"])]

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

(defn delete [req]
  (let [note-id (-> req :path-params :id parse-uuid)]
    (swap! notes-db
           (fn [notes]
             (remove #(= (:id %) note-id) notes)))
    {:status 200
     :headers {"HX-Refresh" "true"}}))

(comment
  @notes-db

;-)
  )
