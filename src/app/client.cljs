(ns app.client
  (:require [app.application :refer [APP]]
            [app.model.house :refer [ui-house-list HouseList House]]
            [com.fulcrologic.fulcro.algorithms.merge :as merge]
            [com.fulcrologic.fulcro.application :as app]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [taoensso.timbre :as log]))

;; defsc User (id)
;; defsc Alert (alert by... price, size, etc)

(defsc Root [_ {:root/keys [houses]}]
  {:query [{:root/houses (comp/get-query HouseList)}]
   :initial-state {:root/houses {}}}
  (dom/div
   (ui-house-list houses)))

(defn ^:export refresh []
  (log/info "Hot code reload...")
  (app/mount! APP Root "app"))

(defn ^:export init []
  (log/info "Application starting...")
  (app/mount! APP Root "app"))

(comment
  (reset! (::app/state-atom APP) {})
  (merge/merge-component! APP House {:house/id 1
                                     :house/name "Househouse"
                                     :house/price 50000}
                          :replace [:root/house])
  (app/current-state APP)
  (app/schedule-render! APP))
