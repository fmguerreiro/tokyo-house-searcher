(ns app.scraper.fetch
  (:require [clojure.string :as str]
            [ring.util.codec :as codec]
            [app.scraper.util :as util]
            [net.cgrand.enlive-html :as html]))

(def ^:dynamic *base-url* "https://suumo.jp/jj/chintai/ichiran/FR301FC001/?ar=030&bs=040&ta=13&tc=0401303&tc=0401304&sngz=&po1=09pc=50&fw=")
(def ^:dynamic *filter* ["告知事項"])

(def url (str *base-url* (str/join "+" (map #(codec/percent-encode %) *filter*))))

(util/defn-memo fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn url-still-valid?
  [path]
  (try (do
         (fetch-url (str "https://suumo.jp" path)) ;; TODO: target shouldnt be hard-coded
         true)
       (catch Exception e false)))

(defn fetch
  [page]
  (let [u (str url "&page=" page)]
    (println "Fetching" u)
    (fetch-url u)))
