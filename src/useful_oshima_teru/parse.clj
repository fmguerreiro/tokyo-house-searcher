(ns useful-oshima-teru.parse
  (:require [net.cgrand.enlive-html :as enlive]
            [ring.util.codec :as codec]))

(def keyword (codec/percent-encode "告知事項"))
(def url (str "https://suumo.jp/jj/chintai/ichiran/FR301FC011/?ar=030&bs=040&kskbn=01&fw=" keyword))

(let [data  (enlive/html-resource (java.net.URL. url))
      pages (enlive/select data [:div #{:.pagination :.pagination_set-nav}])]
  (map #(-> %) pages))
