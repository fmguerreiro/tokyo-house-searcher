(ns app.email
  (:require [postal.core :as postal]
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
