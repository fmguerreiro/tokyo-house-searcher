(ns app.scraper
  (:gen-class)
  (:require [app.scraper.parse :as parse]
            [app.predictor :as predictor]
            [app.email :as email]
            [app.db :as db]))

(defn -main
  [& args]
  (println "Scraping suumo!")
  (->> (parse/scrape-suumo)
       (map #(db/insert %))
       (dorun))
  (println "Scraping done!")
  (let [results (predictor/find-outliers)] ;; NOTE: find-outliers gets data from db
    (println "Predicting done!")
    (println (str (count results) " outliers"))
    (println "Sending email...")
    (email/send-email "Recommended rentals" (email/create-msg-body results))
    (println "Email sent!")))

#_(
   (def res (parse/scrape-suumo))
   (count (distinct (map #(% :id) res)))
   (map #(db/insert %) res)

   (-main)
   )
