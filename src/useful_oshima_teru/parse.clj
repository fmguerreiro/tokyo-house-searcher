(ns useful-oshima-teru.parse
  (:require [net.cgrand.enlive-html :as enlive]
            [ring.util.codec :as codec]
            [clojure.string :as str]))

(def keyword (codec/percent-encode "告知事項"))
(def url (str "https://suumo.jp/jj/chintai/ichiran/FR301FC011/?ar=030&bs=040&kskbn=01&fw=" keyword))

#_(
   (def data (enlive/html-resource (java.net.URL. url)))
   (def pages (enlive/select data [:div #{:.pagination :.pagination_set-nav}]))
   (def n-pages (->> (enlive/select pages [:a :> enlive/text-node])
                     (filter #(not= "次へ" %))
                     (map #(Integer/parseInt %))
                     (apply max)))
   (def urls (map #(str url "&pn=" (+ % 2)) (range (- n-pages 1))))

   )

(defn- scrape [url enlive-exp]
  (let [data (enlive/html-resource (java.net.URL. url))]
    (enlive/select data enlive-exp)))

(let [pages (scrape url [:div #{:.pagination :.pagination_set-nav}])
      n-pages (->> (enlive/select pages [:a :> enlive/text-node])
                   (filter #(not= "次へ" %))
                   (map #(Integer/parseInt %))
                   (apply max))
      urls (map #(str url "&pn=" (+ % 2)) (range (- n-pages 1)))
      listings (->> urls
                    (pmap #(scrape % [:div#js-bukkenList])))]
  (map #(-> %) pages))
