(ns app.filter
  (:require [clojure.string :as str]))

(defn- min-distance [ds]
  (apply min (map #(+ (:bus %)
                      (:walk %)) (:distance-min ds))))

(defn- should-include-location? [loc exclude]
  (let [is-contained? (fn [el col] (seq (filter #(str/includes? el %) col)))]
    (or (empty? exclude)
        (not (is-contained? loc exclude)))))

(defn filter-listings [conf ls]
  (let [max-yen          (get conf :max-yen)
        max-walk-minutes (get conf :max-walk-minutes)
        location-exclude (get conf :location-exclude)
        min-size-meters  (get conf :min-size-meters)]
    (filter #(and (< (get % :price-yen) max-yen)
                  (< (min-distance %) max-walk-minutes)
                  (should-include-location? (:location %) location-exclude))
;;                  (> (get % :size-m) min-size-meters)
            ls)))

#_((def loc "神奈川県川崎市中原区田尻町")
   (def exc ["神奈川"])
   (should-include-location? loc [] exc))
