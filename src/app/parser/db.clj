(ns app.parser.db
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
;; (let [db-source (jdbc/get-datasource db-spec)]
;;   (with-open [connection (jdbc/get-connection db-source)]
;;     (jdbc/execute! connection
;;                    (sql/format
;;                     (create-table :houses :if-not-exists
;;                                   (with-columns
;;                                     [:id :varchar :primary-key]
;;                                     [:price :varchar]
;;                                     [:size :varchar]
;;                                     [:location :varchar]
;;                                     [:transportation :varchar]
;;                                     [:building-age :varchar]
;;                                     [:building-floor :varchar]
;;                                     [:link :varchar]))))))

(def spy #(do (println "DEBUG:" %) %))

(def house-columns [:id :price :size :location :transportation :building-age :building-floor :link])
(defn insert
  [house]
  (println "Inserting" house)
  (let [query (-> (h/insert-into :houses)
                  (h/values [house])
                  (h/upsert (-> (h/on-conflict :id)
                                h/do-nothing))
                  (sql/format {:pretty true}))]
    (spy query)
    (jdbc/execute! db query)))

#_((jdbc/execute! db (sql/format {:drop-table [:houses]})))
