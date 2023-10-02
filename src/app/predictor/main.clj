(ns app.predictor.main
  (:require [clojure.core.matrix.dataset :as d]
            [incanter.core :as i]
            [incanter.stats :as s]))

;; HACK: fixes a bug with divide-by-zero in incanter
;; we dont need to calculate f-distribution for our purposes
;; (intern 'incanter.stats 'cdf-f (fn [a & b] (fn [] 1)))
;; (intern 'incanter.stats 'cdf-t (fn [a & b] 1))
;; (intern 'incanter.stats 'quantile-t (fn [a & b] 1))





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
   ;; (def X (i/trans (i/matrix [
   ;;                            [1000 1500 1200 1800 1350] ;; size
   ;;                            [1 2 1 2 1] ;; num-rooms
   ;;                            ["渋谷区" "中央区" "新宿区" "杉並区" "港区"]
   ;;                            ])))

   ;; (def X (i/matrix [[1000 1] [1500 2] [1200 1] [1800 2] [1350 1]]))

   ;; house prices - independent variable
   (def y (i/matrix [250000 350000 300000 400000 325000]))

   (def model (s/linear-model y X :intercept false))

   ;; predict
   (def X' [1000 1 0 1]) ;; size, num-rooms, is-shinjuku, is-chuo... wait, this is not right

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
)
