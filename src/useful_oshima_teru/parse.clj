(ns useful-oshima-teru.parse
  (:require [useful-oshima-teru.util :as util]
            [net.cgrand.enlive-html :as html]
            [ring.util.codec :as codec]
            [clojure.string :as str]))

(def ^:dynamic *filter* ["告知事項"])
(def ^:dynamic *base-url* "https://suumo.jp/jj/chintai/ichiran/FR301FC011/?ar=030&bs=040&kskbn=01&fw=")
(def ^:dynamic *host* (let [split (str/split *base-url* #"/")]
                        (str (split 0) "//" (split 2))))

(def ^:dynamic *pagination-selector* [:div #{:.pagination :.pagination_set-nav} :a :> html/text-node])
(def ^:dynamic *listing-selector* [:div [:.property :.property--highlight :.js-property :.js-cassetLink]])
(def ^:dynamic *name-selector* [:h2.property_inner-title :.js-cassetLinkHref :> html/text-node])
(def ^:dynamic *link-selector* [:h2.property_inner-title :> :a])
(def ^:dynamic *price-selector* [#{:div.detailbox-property-point (html/right :div.detailbox-property-point)} :> html/text-node]) ; TODO: make it grep 管理費 as well
(def ^:dynamic *size-selector* [:td.detailbox-property--col3 [:div (html/nth-of-type 2)] :> [html/text-node (html/text-pred #(re-matches #".*m$" %))]])
(def ^:dynamic *distance-selector* [:div.detailnote-box :> :div :> [html/text-node (html/text-pred #(re-matches #".*" %))]])
(def ^:dynamic *location-selector* [[:td.detailbox-property-col (html/nth-last-child 1)] :> html/text-node])

(defn- parse-distance [ds]
  (let [[_ line station] (re-matches #"(.+)/([^\s]+) .*" ds)
        [_ bus] (re-matches #".*バス(\d+)分.*" ds)
        [_ walk] (re-matches #".*歩(\d+)分.*" ds)]
    {:line line, :station station, :walk (util/parse-int-with-def walk), :bus (util/parse-int-with-def bus)}))

(defn- get-yen-amount [s]
  (let [[n _] (re-find #"[+-]?([0-9]*[.])?[0-9]+" s)]
    (* 10000 (Double/parseDouble n))))

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
  (let [;; name (first (html/select l *name-selector*))
        price (get-yen-amount (first (html/select l *price-selector*)))
        distance (map #(parse-distance %) (html/select l *distance-selector*))
        size (first (html/select l *size-selector*))
        location (->> (html/select l *location-selector*) (map #(str/triml %)) (filter #(not-empty %)) first)
        link (str *host* (-> (html/select l *link-selector*) first :attrs :href))]
    {:location location, :price-yen price, :distance-min distance, :size-m size, :link link}))

(defn parse
  ([]    (parse (str *base-url* (str/join "+" (map #(codec/percent-encode %) *filter*)))))
  ([url] (let [n-pages (get-page-count url)
               urls (map #(str url "&pn=" (+ % 2)) (range (- n-pages 1)))
               listings-per-url (pmap #(get-listings %) urls)
               listings (flatten listings-per-url)
               res (map #(listing->map %) listings)]
           res)))

;; scratch
#_((str/join "+" (map #(codec/percent-encode %) ["告知事項", "hello"]))
   (select *base-url* *distance-selector*)
   (def ls (get-listings *base-url*))
   (def eg (->  ls second (html/select [:div.detailnote-box :> :div :> [html/text-node (html/text-pred #(re-matches #".*" %))]]))))
