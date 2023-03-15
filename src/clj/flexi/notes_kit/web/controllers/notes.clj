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
     [:h3.p-3.text-2xl "team notes"]
     (if-let [notes (seq @notes-db)]

       [:div.p-3.flex.flex-wrap.gap-3
        (for [{:keys [id name content]} notes
              :let [uri (str "/notes/" id)]]
          [:div {:class "bg-slate-50 border-slate-300 rounded max-w-md"}
           [:div.flex.gap-2.text-slate-600.mx-4.p-2
            [:a.text-sm {:href uri} "edit"]
            [:button.text-sm
             {:hx-delete uri
              :hx-confirm "you sure?"}
             "delete"]]
           [:h4.text-lg.mx-3 name]
           [:p.mx-2 content]])]

       [:div "no notes"])]

    [:div
     [:h3.p-3.text-xl "create new note"]
     [:form.px-3.flex.gap-3
      {:hx-post "/notes"}
      [:input.border-slate-600.rounded.bg-slate-50.px-2
       {:name "name"
        :type "text"
        :placeholder "note name"}]

      [:button.btn "create"]]]]))

(defn get-form [req]
  (let [note-id (-> req :path-params :id parse-uuid)
        {:keys [name content]} (->> @notes-db
                                    (filter #(= note-id (:id %)))
                                    first)]
    (ppage
     (if name
       [:div.p-3
        [:h3.px-3.text-2xl "edit note"]
        [:form.px-3
         {:hx-put (str "/notes/" note-id)}
         [:div.my-2
          [:input.text-xl.text-slate-600.border-slate-600.rounded.bg-slate-50.px-2.py-1
           {:name "name"
            :type :text
            :value name}]]

         [:div.my-2
          [:input.border-slate-600.text-slate-600.rounded.bg-slate-50.px-2.py-1
           {:name "content"
            :type :textfield
            :placeholder "note content"
            :value content}]]

         [:button.btn "update note"]]]

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
