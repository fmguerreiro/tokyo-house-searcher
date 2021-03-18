(ns useful-oshima-teru.parse
  (:require [useful-oshima-teru.util :as util]
            [net.cgrand.enlive-html :as html]
            [ring.util.codec :as codec]
            [clojure.string :as str]))

(def ^:dynamic *filter* "告知事項")
(def ^:dynamic *base-url* (str "https://suumo.jp/jj/chintai/ichiran/FR301FC011/?ar=030&bs=040&kskbn=01&fw=" (codec/percent-encode *filter*)))
(def ^:dynamic *host* (let [split (str/split *base-url* #"/")]
            (str (split 0) "//" (split 2))))

(def ^:dynamic *pagination-selector* [:div #{:.pagination :.pagination_set-nav} :a :> html/text-node])
(def ^:dynamic *name-selector* [:h2.property_inner-title :.js-cassetLinkHref :> html/text-node])
(def ^:dynamic *link-selector* [:h2.property_inner-title :.js-cassetLinkHref])
(def ^:dynamic *price-selector* [#{:div.detailbox-property-point (html/right :div.detailbox-property-point) } :> html/text-node]) ; TODO: make it grep 管理費 as well
(def ^:dynamic *size-selector* [:td.detailbox-property--col3 [:div (html/nth-of-type 2)] :> [html/text-node (html/text-pred #(re-matches #".*m$" %))]])
(def ^:dynamic *distance-selector* [[:div.detailnote-box (html/nth-of-type 1)] :div :> html/text-node])

(util/defn-memo fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn- scrape [url enlive-exp]
  (let [data (fetch-url url)]
    (html/select data enlive-exp)))

(defn- get-page-count [url]
  (let [pages (scrape *base-url* *pagination-selector*)]
    (->> pages
         (filter #(not= "次へ" %))
         (map #(Integer/parseInt %))
         (apply max))))

(defn scrape-listings [url]
  (let [names  (scrape url *name-selector*)
        prices (scrape url *price-selector*)
        sizes  (scrape url *size-selector*)
        dists  (scrape url *distance-selector*)
        links  (map #(str *host* (:href (:attrs %))) (scrape url *link-selector*))]
    (->> (interleave names prices sizes dists links)
         (partition 5)
         (map #(zipmap [:name :price :size :distance :link] %)))))

(defn get-listings [url]
  (->> url
       (pmap scrape-listings)))

(defn parse
  ([]    (parse *base-url*))
  ([url] (let [n-pages (get-page-count url)
               urls (map #(str url "&pn=" (+ % 2)) (range (- n-pages 1)))]
           (flatten (map #(scrape-listings %) urls)))))

#_(
   (def data (html/html-resource (java.net.URL. *base-url*)))
   (def pages (html/select data *pagination-selector*))
   (def n-pages (->> pages
                     (filter #(not= "次へ" %))
                     (map #(Integer/parseInt %))
                     (apply max)))
   (def urls (map #(str *base-url* "&pn=" (+ % 2)) (range (- n-pages 1))))
   (def urls [(first urls)])
   (def url "https://suumo.jp/jj/chintai/ichiran/FR301FC011/?ar=030&bs=040&kskbn=01&fw=%E5%91%8A%E7%9F%A5%E4%BA%8B%E9%A0%85&pn=2")
   (def ^:dynamic *link-selector* [:h2.property_inner-title :.js-cassetLinkHref])
   (map #(:href (:attrs %)) (scrape url *link-selector*))
   (def listings (->> urls   ;; => ["https://suumo.jp/jj/chintai/ichiran/FR301FC011/?ar=030&bs=040&kskbn=01&fw=%E5%91%8A%E7%9F%A5%E4%BA%8B%E9%A0%85&pn=2"]
                      (pmap (fn [url] (scrape url *station-distance-selector*)))))
   (scrape-listings url)
   (zipmap '(:name :price :size :distance) '("a" "b" "c" "d"))
   (->> urls (pmap scrape-listings))
   (doall listings)
   (def a (parse *base-url*))
   (count (flatten a))
   (first a)
   )
