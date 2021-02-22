(ns useful-oshima-teru.core
    (:require [useful-oshima-teru.stream :as stream])
    (:gen-class))

(defn parse-args [args]
  (println "Arguments are ignored: " args))

(defn parse-line [line]
  (str " :: " line))

(defn print-line [line]
  (->> line
       parse-line
       println))

(defn -main [& args]
  (let [index (parse-args args)]
    (stream/mapper *in* print-line)))
