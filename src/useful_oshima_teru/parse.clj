(ns useful-oshima-teru.parse
  (:require [net.cgrand.enlive-html :as html]
            [ring.util.codec :as codec]
            [clojure.string :as str]))

(def ^:dynamic *filter* "告知事項")
(def ^:dynamic *base-url* (str "https://suumo.jp/jj/chintai/ichiran/FR301FC011/?ar=030&bs=040&kskbn=01&fw=" *filter*))

(def ^:dynamic *pagination-selector* [:div #{:.pagination :.pagination_set-nav} :a :> html/text-node])
(def ^:dynamic *price-selector* [#{:div.detailbox-property-point (html/right :div.detailbox-property-point) } :> html/text-node]) ; TODO: make it grep 管理費 as well
(def ^:dynamic *size-selector* [:td.detailbox-property--col3 [:div (html/nth-of-type 2)] (html/has [(html/re-pred #"\w*m$")]) :> html/text-node]) ; TODO: also selects building age
(def ^:dynamic *station-distance-selector* [:div.detailnote-box :div])

(def ^:dynamic *final-selector* *station-distance-select*)

(defn- fetch-url [url]
  (enlive/html-resource (java.net.URL. url)))

(defn- scrape [url enlive-exp]
  (let [data (fetch-url url)]
    (enlive/select data enlive-exp)))

(let [pages (scrape url *pagination-selector*)
      n-pages (->> pages
                   (filter #(not= "次へ" %))
                   (map #(Integer/parseInt %))
                   (apply max))
      urls (map #(str url "&pn=" (+ % 2)) (range (- n-pages 1)))
      listings (->> urls
                    (pmap #(scrape % [:div #{:.property :.property--highlight :.js-property :.js-cassetLink}])))]
  (map #(-> %) pages))

#_(
   (def data (enlive/html-resource (java.net.URL. url)))
   (def pages (enlive/select data *pagination-selector*))
   (def n-pages (->> (pages)
                     (filter #(not= "次へ" %))
                     (map #(Integer/parseInt %))
                     (apply max)))
   (def urls (map #(str url "&pn=" (+ % 2)) (range (- n-pages 1))))

   (def urls [(first urls)])
   (def listings (->> urls
                      (pmap (fn [url] (scrape url final-selector ; #{;[:div.property_group]
                                        ;      [:div.detailbox-property-point :> enlive/text-node]}
                                              )))))
   (doall listings)
   )
