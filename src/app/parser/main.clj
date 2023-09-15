(ns app.parser.main
  (:require [app.parser.parse :as parse]
            [app.parser.db :as db]))

(defn -main
  [& args]
  (println "Scraping suumo!")
  (map #(do (println %) (db/insert %)) (parse/scrape-suumo))
  (println "Done!"))

#_(
   (def res (parse/scrape-suumo))
    (map #(db/insert %) res)
   )
