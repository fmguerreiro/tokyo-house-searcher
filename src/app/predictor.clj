(ns app.predictor.main
  (:require [app.predictor.transformer :as t]
            [app.db :as db]
            [clojure.core.matrix.dataset :as d]
            [clojure.core.matrix :as m]
            [incanter.core :as i]
            [incanter.stats :as s]
            [incanter.charts :as c]))

(def column-names t/coefficients)

(def total-data (db/select-all))

;; (def dataset (d/dataset column-names total-data))
(def dataset (d/dataset column-names total-data))

(defn feature-matrix [col-names ds]
  (-> (i/$ col-names ds)
      (i/to-matrix :dummies true)))

;; (def dataset-matrix (i/to-matrix dataset :dummies false))
(def dataset-matrix (feature-matrix column-names dataset))

(def y (i/$ 0 dataset-matrix)) ;; dependent variable - prices
(def X (i/$ (range 1 (.sliceCount (first dataset-matrix))) dataset-matrix)) ;; independent variables - everything else

(def price-lm (s/linear-model y X :intercept true))

(s/predict price-lm [30 1 0 0 0 0 0 23])

;; view graph in relation to the first independent variable
(i/view (c/add-lines (c/scatter-plot (first X) y) (first X) (:fitted price-lm)))

;; (defn has-categories? [seq]
;;   (->> seq
;;        (filter #(or (number? %) (boolean? %)))
;;        (empty?)))

;; (def columns (:columns dataset)) ;; TODO: need this?
;; (def categorical-values
;;      (->> columns
;;           (filter has-categories?)
;;           (apply distinct)))

;; (defn indices [pred coll]
;;   (keep-indexed #(when (pred %2) %1) coll))
;;
; ...
;
;; (def X' [1000 1 0 1])
;;

#_(
   (def data '())

   (def column-names ["size" "num-rooms" "area"])
   (def data [[1000 1 "新宿区"] [1200 1 "中央区"] [1300 1 "新宿区"] [1500 2 "新宿区"] [1800 2 "新宿区"]])
   (def dataset' (d/dataset column-names data))

   (def columns (:columns dataset'))
   (defn has-categories? [seq]
     (->> seq
          (filter #(or (number? %) (boolean? %)))
          (empty?)))

   (def categorical-values
     (->> columns
          (filter has-categories?)
          (apply distinct)))

   (defn indices [pred coll]
     (keep-indexed #(when (pred %2) %1) coll))

   (defn insert-and-replace [array index array-to-insert]
     (let [[left right] (split-at index array)]
       (concat left array-to-insert (rest right))))

   (def new-column-names
     (insert-and-replace column-names (first (indices has-categories? columns)) categorical-values))

   (def i-to-replace (first (indices has-categories? columns)))

   (defn data->dataset-with-dummies [data]
     (->> data
          (map #(insert-and-replace
                 %
                 i-to-replace
                 (map (fn [cat] (if (= cat (nth % i-to-replace)) 1 0)) categorical-values)))
          (d/dataset new-column-names)))

   (def dataset-with-dummies (data->dataset-with-dummies data))
   (def X (i/to-matrix dataset-with-dummies :dummies false))

   ;; (def X (i/to-matrix dataset' :dummies true)) ;; TODO: need to assign dummy variables, i.e. new columns for each area

   ;; dependent variables
   ;; (def X (i/trans (i/matrix [[1000 1500 1200 1800 1350]
   ;;                            [1 2 1 2 1]
   ;;                            ["渋谷区" "中央区" "新宿区" "杉並区" "港区"]])))

   ;; (def X (i/matrix [[1000 1] [1500 2] [1200 1] [1800 2] [1350 1]]))

   ;; house prices - independent variable
   (def y (i/matrix [250000 350000 300000 400000 325000]))

   (def model (s/linear-model y X :intercept false))

   ;; predict
   (def X' [1000 1 0 1]) ;; size, num-rooms, is-shinjuku, is-chuo

   ;; sum of multiplications of coefficients and values
   (s/predict model X')

   ;; or
   (defn predict [coefs X']
     (apply + (map * coefs X')))
   (predict (:coefs model) X')

   (:coefs model)
   (:fitted model)
   (:r-square model)
   (:residuals model) ; higher value means worse fit => outliers
                      ; or maybe can go and call predict on each row and see which ones are too off

   (defn feature-matrix [col-names data]
     (-> (i/$ col-names data)
         (i/to-matrix)))

   (feature-matrix ["id" "price" "size" "location" "transportation" "building-age" "building-floor"] data)

   (defn normalize-location [house]
     (let [location (:houses/location house)
           new-house (merge house {:houses/location (p/normalize-town-name location)})]
       (println new-house)
       new-house))

   (defn numerify-fields [house]
     (let [price (:houses/price house)
           size  (:houses/size house)
           age   (:houses/building_age house)
           new-house (merge house {:houses/price (Double/parseDouble price)
                                   :houses/building_age (Integer/parseInt age)
                                   :houses/size (Double/parseDouble size)})]
       (println new-house)
       new-house))

   (defn update-locations [houses]
     (->> houses
          (map #(normalize-location (select-keys % (keys %))))
          (map #(db/insert %))))

   (update-locations total-data)

   (defn update-numerical-fields [houses]
     (->> houses
          (map #(numerify-fields (select-keys % (keys %))))
          (map #(db/insert %))))

   (update-numerical-fields (db/select-all))

   (def town-names
     (loop [i         0
            cursor    (db/select i)
            locations #{}]
       (if (empty? cursor)
         (do
           (println "Done")
           locations)
         (let [new-locations (into locations (map #(p/normalize-town-name (:houses/location %)) cursor))]
           (recur (inc i) (db/select (inc i)) new-locations)))))

   (loop [i      0
          cursor (db/select i)]
     (println "Page" i)
     (if (or (empty? cursor))
       (println "Done")
       (do
         (update-locations cursor)
         (recur (inc i) (db/select (inc i))))))
   )
