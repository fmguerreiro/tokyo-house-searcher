(ns app.predictor.transformer
  (:require [clojure.core.matrix.dataset :as d]
            [incanter.core :as i]
            [clojure.core.matrix :as m]))


(def coefficients [:houses/price :houses/building_age :houses/location :houses/size])

(defn row->x-y
  "Takes a row from a dataset and returns a map with the dependent variable y and the independent variables X"
  [row]
  (let [y (m/scalar (first row))
        X (into [] (m/subvector row 1 (dec (m/ecount row))))]
    {:y y :X X}))

(defn feature-matrix
  [col-names ds]
  (-> (i/$ col-names ds)
      (i/to-matrix :dummies true)))

(defn data->matrix
  [data]
  (let [dataset (d/dataset coefficients data)]
    (feature-matrix coefficients dataset)))

#_(
   (def records (db/select 0))
   (def record (first records))

   (defn- strs->vals [strs]
     (map #(let [converted-val (clojure.edn/read-string %)]
             (if (symbol? converted-val) % converted-val))
          strs))

   (defn get-coefficients [record]
     (as-> (select-keys record coefficients) $
       (clojure.core/vals $)
       (strs->vals $)
       (vec $)))

   (get-coefficients record)
   )
