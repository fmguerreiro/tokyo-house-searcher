(ns app.scraper.fetch
  (:require [clojure.string :as str]
            [ring.util.codec :as codec]
            [app.scraper.util :as util]
            [net.cgrand.enlive-html :as html]))

(def ^:dynamic *base-url* "https://suumo.jp/chintai/tokyo/new/?ar=030&bs=040&kskbn=01&pc=50&fw=")
(def ^:dynamic *filter* ["告知事項"])

(def url (str *base-url* (str/join "+" (map #(codec/percent-encode %) *filter*))))

(util/defn-memo fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn url-still-valid?
  [path]
  (try (do (fetch-url (str "https://suumo.jp" path)) true) ;; TODO: target shouldnt be hard-coded
       (catch Exception e false)))

(defn fetch
  [page]
  (let [u (str url "&page=" page)]
    (println "Fetching" u)
    (fetch-url u)))
