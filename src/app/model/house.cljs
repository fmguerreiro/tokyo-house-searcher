(ns app.model.house
  (:require ["react-number-format" :as NumberFormat]
            [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
            [com.fulcrologic.fulcro.dom :as dom]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))

(def ui-number-format (interop/react-factory NumberFormat))

(defsc House [this {:house/keys [id name price] :as props}]
  {:query [:house/id :house/name :house/price]
   :ident :house/id
   :initial-state {:house/id :param/id
                   :house/name :param/name
                   :house/price :param/price}}
  (dom/div :.ui.segment {}
    (dom/div :.field {} (dom/label {} "Name: ") name)
    (dom/div {}
             (dom/label {} "Price: ")
             (ui-number-format {:thousandSeparator true
                                :prefix "¥"
                                :value (str price)}))
    (dom/button :.ui.button {:onClick (fn [] (comp/transact! this `[(favorite ~{:house/id id})]))}
                "Update name")))

(def ui-house (comp/factory House {:keyfn :house/id}))

(defsc HouseList [_ {:house-list/keys [houses] :as props}]
  {:query         [{:house-list/houses (comp/get-query House)}]
   :ident         (fn [_ _] [:component/id ::house-list])
   :initial-state {:house-list/houses []}}
  (dom/div {}
    (dom/h3 {} "Houses")
    (map ui-house houses)))

(def ui-house-list (comp/factory HouseList))
