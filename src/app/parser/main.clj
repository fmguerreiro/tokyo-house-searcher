(ns app.parser.main
  (:require [app.parser.parse :as parse]
            [app.parser.db :as db]))

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
