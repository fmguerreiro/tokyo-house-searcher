(ns app.predictor.transformer
  (:require [app.db :as db]))


(def coefficients [:houses/price :houses/building_age :houses/location :houses/size])

(defn- strs->vals [strs]
  (map #(let [converted-val (clojure.edn/read-string %)]
          (if (symbol? converted-val) % converted-val))
       strs))

(defn get-coefficients [record]
  (as-> (select-keys record coefficients) $
    (clojure.core/vals $)
    (strs->vals $)
    (vec $)))

#_(
   (def records (db/select 0))
   (def record (first records))
   (get-coefficients record)
   )
