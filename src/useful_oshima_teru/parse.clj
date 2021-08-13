(ns useful-oshima-teru.parse
  (:require [useful-oshima-teru.util :as util]
            [net.cgrand.enlive-html :as html]
            [ring.util.codec :as codec]
            [clojure.string :as str]
            [clojure.edn :as edn]))

(def ^:dynamic *filter* "告知事項")
(def ^:dynamic *base-url* (str "https://suumo.jp/jj/chintai/ichiran/FR301FC011/?ar=030&bs=040&kskbn=01&fw=" (codec/percent-encode *filter*)))
(def ^:dynamic *host* (let [split (str/split *base-url* #"/")]
                        (str (split 0) "//" (split 2))))

(def ^:dynamic *pagination-selector* [:div #{:.pagination :.pagination_set-nav} :a :> html/text-node])
(def ^:dynamic *listing-selector* [:div [:.property :.property--highlight :.js-property :.js-cassetLink]])
(def ^:dynamic *name-selector* [:h2.property_inner-title :.js-cassetLinkHref :> html/text-node])
(def ^:dynamic *link-selector* [:h2.property_inner-title :> :a])
(def ^:dynamic *price-selector* [#{:div.detailbox-property-point (html/right :div.detailbox-property-point)} :> html/text-node]) ; TODO: make it grep 管理費 as well
(def ^:dynamic *size-selector* [:td.detailbox-property--col3 [:div (html/nth-of-type 2)] :> [html/text-node (html/text-pred #(re-matches #".*m$" %))]])
(def ^:dynamic *distance-selector* [[:div.detailnote-box (html/nth-of-type 1)] :div :> html/text-node])

(util/defn-memo fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn- select [url enlive-exp]
  (let [data (fetch-url url)]
    (html/select data enlive-exp)))

(defn- get-page-count [url]
  (let [pages (select url *pagination-selector*)]
    (->> pages
         (filter #(not= "次へ" %))
         (map #(Integer/parseInt %))
         (apply max))))

(defn- get-listings [url]
  (select url *listing-selector*))

(defn- listing->map [l]
  (let [name (first (html/select l *name-selector*))
        price (first (html/select l *price-selector*))
        distance (first (html/select l *distance-selector*))
        size (first (html/select l *size-selector*))
        link (str *host* (-> (html/select l *link-selector*) first :attrs :href))]
    {:name name, :price price, :distance distance, :size size, :link link}))

;; TODO finish and test
;; (defn- extract-numbers [s]
;;   (edn/read-string ((re-find #"[+-]?([0-9]*[.])?[0-9]+" s) 0)))

;; (defn- parse-distance [s] ;;=> "ＪＲ東海道本線/国府津駅 歩15分"
;;   (let [split (str/split s #" ")
;;         walking (extract-numbers (split 1))
;;         from (split 0)]
;;     {:walking (if (str/includes? (split 1) "バス") (* 2 walking) walking)
;;      :from from})) ;; => {:walking "15" :from "国府津駅"}

;; (defn- parse-amount [s]
;;   (* 10000 (extract-numbers s)))

(defn parse
  ([]    (parse *base-url*))
  ([url] (let [n-pages (get-page-count url)
               urls (map #(str url "&pn=" (+ % 2)) (range (- n-pages 1)))
               listings-per-url (pmap #(get-listings %) urls)
               listings (flatten listings-per-url)
               res (map #(listing->map %) listings)]
           res)))

#_()
