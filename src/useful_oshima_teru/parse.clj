(ns useful-oshima-teru.parse
  (:require [useful-oshima-teru.util :as util]
            [net.cgrand.enlive-html :as html]
            [ring.util.codec :as codec]
            [clojure.string :as str]))

(def ^:dynamic *filter* "告知事項")
(def ^:dynamic *base-url* (str "https://suumo.jp/jj/chintai/ichiran/FR301FC011/?ar=030&bs=040&kskbn=01&fw=" (codec/percent-encode *filter*)))

(def ^:dynamic *pagination-selector* [:div #{:.pagination :.pagination_set-nav} :a :> html/text-node])
(def ^:dynamic *price-selector* [#{:div.detailbox-property-point (html/right :div.detailbox-property-point) } :> html/text-node]) ; TODO: make it grep 管理費 as well
(def ^:dynamic *size-selector* [:td.detailbox-property--col3 [:div (html/nth-of-type 2)] (html/has [(html/re-pred #"\w*m$")]) :> html/text-node]) ; TODO: also selects building age
(def ^:dynamic *station-distance-selector* [[:div.detailnote-box (html/nth-of-type 1)] :div :> html/text-node])

; (def ^:dynamic *final-selector* *station-distance-select*)

(util/defn-memo fetch-url [url] (html/html-resource (java.net.URL. url)))

(defn- scrape [url enlive-exp]
  (let [data (fetch-url url)]
    (html/select data enlive-exp)))

(let [pages (scrape *base-url* *pagination-selector*)
      n-pages (->> pages
                   (filter #(not= "次へ" %))
                   (map #(Integer/parseInt %))
                   (apply max))
      urls (map #(str *base-url* "&pn=" (+ % 2)) (range (- n-pages 1)))
      listings (->> urls
                    (pmap #(scrape % [:div #{:.property :.property--highlight :.js-property :.js-cassetLink}])))]
  (map #(-> %) pages))

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
   (def ^:dynamic *selector* [#{:div.detailbox-property-point (html/right :div.detailbox-property-point) } :> html/text-node])
   (scrape url *station-distance-selector*)
   (def listings (->> urls   ;; => ["https://suumo.jp/jj/chintai/ichiran/FR301FC011/?ar=030&bs=040&kskbn=01&fw=%E5%91%8A%E7%9F%A5%E4%BA%8B%E9%A0%85&pn=2"]
                      (pmap (fn [url] (scrape url *station-distance-selector*)))))
   (doall listings)
   )
