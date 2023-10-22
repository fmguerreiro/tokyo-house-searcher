(ns app.predictor.main
  (:require [app.predictor.transformer :as t]
            [app.db :as db]
            [app.scraper.parse :as p]
            [clojure.core.matrix.dataset :as d]
            [incanter.core :as i]
            [incanter.stats :as s]))

(def column-names t/coefficients)

(def data
  (->> (db/select 0) ;; TODO more than 1 page
       (map t/get-coefficients)
       (vec)))

(def total-data (db/select-all))

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

(def dataset (d/dataset column-names data))
;; [[6.45 10 "東京都あきる野市牛沼" 59.03] [5.5 35 "東京都八王子市下恩方町" 45.36] [4.6 56 "東京都昭島市郷地町３" 28.98] [5.2 60 "東京都八王子市小比企町" 49.2] [5.2 48 "東京都町田市本町田" 32.22] [6.5 41 "東京都東久留米市滝山７" 54] [8 66 "東京都北区志茂４" 36.49] [4.5 39 "東京都武蔵村山市中原２" 39] [10 59 "東京都三鷹市上連雀８" 58.74] [3.8 68 "東京都新宿区西新宿８" 9.9]]
(def dataset-matrix (i/to-matrix dataset :dummies true))
;; #vectorz/array [[6.45,10.0,0.0,0.0,0.0,0.0,59.03],[5.5,35.0,0.0,0.0,1.0,0.0,45.36],[4.6,56.0,0.0,1.0,1.0,0.0,28.98],[5.2,60.0,0.0,0.0,1.0,1.0,49.2],[5.2,48.0,1.0,0.0,0.0,1.0,32.22],[6.5,41.0,0.0,1.0,1.0,1.0,54.0],[8.0,66.0,0.0,1.0,0.0,0.0,36.49],[4.5,39.0,1.0,0.0,0.0,0.0,39.0],[10.0,59.0,0.0,0.0,0.0,1.0,58.74],[3.8,68.0,0.0,1.0,0.0,1.0,9.9]]
(def y (i/sel dataset-matrix :cols 0))
(def X (i/sel dataset-matrix :cols (range 1 (.sliceCount (first dataset-matrix)))))

(def model (s/linear-model y X :intercept false))

(def X' X)
(s/predict model X')

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
     (let [location (:houses/location house)]
       (merge house {:houses/location (p/normalize-town-name location)})))

(defn update-locations [houses]
  (->> houses
       (map #(normalize-location (select-keys % (keys %))))
       (map #(db/insert %))))

(loop [i      0
       cursor (db/select i)]
  (println "Page" i)
  (if (or (empty? cursor))
    (println "Done")
    (do
     (u/spy (update-locations cursor))
     (recur (inc i) (db/select (inc i))))))
)
