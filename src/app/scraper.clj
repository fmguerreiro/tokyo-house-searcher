(ns app.scraper
  (:require [app.scraper.parse :as parse]
            [app.predictor.main :as predictor]
            [app.email :as email]
            [app.db :as db]))

(defn -main
  [& args]
  (println "Scraping suumo!")
  (map #(db/insert %) (parse/scrape-suumo))
  (println "Scraping done!")
  (let [results (predictor/find-outliers)]
    (println "Predicting done!")
    (println "Outliers:")
    (println results)
    (println "Sending email...")
    (email/send-email "Recommended rentals" (email/create-msg-body results))
    (println "Email sent!")))

#_(
   (def res (parse/scrape-suumo))
   (count (distinct (map #(% :id) res)))
   (map #(db/insert %) res)
   )
