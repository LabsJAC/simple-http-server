(ns simple-http-server.components.database
  (:require [medley.core :as medley]))

(defn new-database [initial-state]
  (atom initial-state))

(defn reset-database [db]
  (reset! db {}))

(defn insert [db entities]
  (doseq [entity entities]
    (swap! db assoc (medley/random-uuid) entity))
  @db)

(defn get-all [db]
  (let [db-data @db
        db-ids (keys db-data)
        db-maps (vals db-data)]
    (map #(assoc %1 :id %2) db-maps db-ids)))

(defn delete [db ids]
  (doseq [id ids]
    (swap! db dissoc id))
  @db)

(defn find-entity [db id]
  (some-> (get @db id)
          (assoc :id id)))

(defn update-entity [db id update-fn]
  (swap! db
         (fn [db#]
           (update db# id #(if (nil? %)
                             %
                             (update-fn %)))))
  (find-entity db id))
