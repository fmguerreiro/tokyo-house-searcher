(ns app.db
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :as h :refer [create-table with-columns]]))

;; TODO: dont hardcode these
(def db-spec
  {:dbtype   "postgresql"
   :dbname   "postgres"
   :user     "admin"
   :password "password"
   :host     "localhost"
   :port     5432})

(def db (jdbc/get-datasource db-spec))

;; initialize tables
(let [db-source (jdbc/get-datasource db-spec)]
  (with-open [connection (jdbc/get-connection db-source)]
    (jdbc/execute! connection
                   (sql/format
                    (create-table :houses :if-not-exists
                                  (with-columns
                                    [:id :varchar :primary-key]
                                    [:price :float]
                                    [:size :float]
                                    [:location :varchar]
                                    [:transportation :varchar]
                                    [:building-age :int]
                                    [:building-floor :varchar]
                                    [:link :varchar]))))))

(defn insert
  [house]
  (let [query (-> (h/insert-into :houses)
                  (h/values [house])
                  (h/upsert (-> (h/on-conflict :id)
                                (h/do-update-set {:fields [:price :size :location :transportation :building-age :building-floor :link]})))
                  (sql/format {:pretty true}))]
    (jdbc/execute! db query)))

(defn delete
  [house]
  (let [query (-> (h/delete-from :houses)
                  (h/where [:= :id (:houses/id house)])
                  (sql/format {:pretty true}))]
    (jdbc/execute! db query)))

(defn select
  ([] (select 0))
  ([page]
   (let [limit  10
         offset (* page limit)
         query  (-> (h/select :*)
                    (h/from :houses)
                    (h/limit limit)
                    (h/offset offset)
                    (sql/format))]
     (jdbc/execute! db query))))

(defn select-all
  ([] (select-all 1000))
  ([max-page]
   (loop [i         0
          cursor    (select i)
          res       []]
     (if (or (empty? cursor) (> i max-page))
       res
       (let [new-res (into res cursor)]
         (recur (inc i) (select (inc i)) new-res))))))

#_((jdbc/execute! db (sql/format {:drop-table [:houses]})))
