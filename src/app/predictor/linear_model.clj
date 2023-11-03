(ns app.predictor.linear-model
  (:require [app.predictor.transformer :as t]
            [app.db :as db]
            [clojure.core.matrix.dataset :as d]
            [incanter.core :as i]
            [incanter.stats :as s]))

(def column-names t/coefficients)

(def data (into [] (db/select-all)))

(def dataset (d/dataset column-names data))

(def dataset-matrix (t/feature-matrix column-names dataset))

(def y (i/$ 0 dataset-matrix)) ;; dependent variable - prices
(def X (i/$ (range 1 (.sliceCount (first dataset-matrix))) dataset-matrix)) ;; independent variables - everything else

(def price-lm (s/linear-model y X :intercept true))

(def price-lm-lite (dissoc price-lm :y :design-matrix :coef-var :t-probs :residuals :coefs-ci :x :std-errors :fitted))

(s/predict price-lm-lite [30 1 0 0 0 0 0 23])

(def trained-lm
  "Generated from `price-lm-lite` above.`"
  {:sse 164363.55855668333,
   :msr 35425.835662432575,
   :mse 16.43471238442989,
   :adj-r-square 0.6325984407926909,
   :df [8 10001],
   :ssr 283406.6852994606,
   :sst 447770.2438561439,
   :coefs '(-1.4168672320922227 0.06667763305942348 0.019857410524435792 -0.7803614866227927 -0.9215383195673017 -0.3929947979167765 -0.044764060665578365 -0.78683749866974 0.24119829213856392),
   :f-stat 2155.5494756327294,
   :r-square 0.6329288048683094,
   :f-prob 1.1102230246251565E-16,
   :t-tests [-6.144673586704838 14.487363645056925 0.2077183156940153 -8.655219927497791 -11.083828319489646 -4.718094467971342 -0.5364320164725784 -9.25637403472869 128.99555444001646]}
  )
