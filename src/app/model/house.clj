(ns app.model.house
  (:require
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
    [com.wsscode.pathom.connect :as pc :refer [defresolver]]
    [taoensso.timbre :as log]))

(defmutation favorite [{:house/keys [id]}]
  (action [{:keys [state]}]
    (swap! state update-in [:house/id id :house/name] #(str name "Updated!"))))

;; TODO dont use in-memory map
(def houses
  (atom {1 {:house/id 1
            :house/name "House1"
            :house/price 50000}
         2 {:house/id 2
            :house/name "House2"
            :house/price 60000}}))
(defresolver house-resolver [env {:house/keys [id]}]
  {::pc/input #{:house/id}}
  {::pc/output [:house/id :house/name :house/price]}
  (get @houses id))

(defresolver all-houses-resolver [env {:house/keys [id]}]
  {::pc/output [{:all-houses [:house/id]}]}
  {:all-houses (mapv (fn [i] {::id i}) (keys @houses))})

(def resolvers [house-resolver all-houses-resolver])
