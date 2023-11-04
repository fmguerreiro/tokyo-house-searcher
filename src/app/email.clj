(ns app.email
  (:require [postal.core :as postal]
            [clojure.string :as str]
            [dotenv :refer [env]]))

(defn send-email
  "ref: https://andersmurphy.com/2022/06/14/clojure-sending-emails-with-postal-and-gmail-smtp.html"
  [subject body]
  (postal/send-message {:host "smtp.gmail.com"
                        :user (env :EMAIL_USER)
                        :pass (env :EMAIL_PASS)
                        :port 587
                        :tls true}
                       {:from "me@any.com" ;; Gets re-written by google to user email
                        :to (env :EMAIL_USER)
                        :subject subject
                        :body body}))

(defn create-msg-body
  [results]
  (let [n     (count results)
        data  (map #(let [name  (str (get-in % [:data :houses/id]))
                          price (str (get-in % [:data :houses/price]))
                          size  (str (get-in % [:data :houses/size]))
                          link  (str (get-in % [:data :houses/link]))]
                      (str "House " name "\n" "Price: " price " Size: " size " Link: " link "\n"))
                   results)]
    (str "Found " n " outliers:\n" (str/join "\n" data))))

#_(
   (def res '({:diff 47.986851375274355, :i 8324, :data #:houses{:id "広尾ガーデンヒルズ　Ｍ棟", :price 70.0, :size 86.0, :location "渋谷", :transportation "東京メトロ日比谷線/広尾駅 歩5分", :building_age 40, :building_floor "地下1地上7階建", :link "/chintai/jnc_000084485069/?bc=100344577164"}}))
   (create-msg-body res)
   )
