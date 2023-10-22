(ns app.scraper
  (:require [app.scraper.parse :as parse]
            [app.db :as db]))

(defn -main
  [& args]
  (println "Scraping suumo!")
  (map #(db/insert %) (parse/scrape-suumo))
  (println "Done!"))

#_(
   (def res (parse/scrape-suumo))
   (count (distinct (map #(% :id) res)))
   (map #(db/insert %) res)
   )
