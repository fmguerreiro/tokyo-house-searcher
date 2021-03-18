(ns useful-oshima-teru.core
  (:require [useful-oshima-teru.stream :as stream]
            [useful-oshima-teru.parse :as parse]
            [clojure.tools.cli :refer [cli]]
            [clojure.java.io :as io]
            [clojure.edn :as edn])
    (:gen-class))

(defn parse-args [args]
  (println "Arguments are ignored: " args))

(defn parse-line [line]
  (str " :: " line))

(defn print-line [line]
  (->> line
       parse-line
       println))

(defn- load-edn [source]
  (try
    (with-open [r (io/reader source)]
      (edn/read (java.io.PushbackReader. r)))
    (catch java.io.IOException e
      (printf "Couldn't open '%s': %s\n" source (.getMessage e)))
    (catch RuntimeException e
      (printf "Error parsing edn file '%s': %s\n" source (.getMessage e)))))

(defn- write-file [data file]
  (let [f (io/file file)]
    (-> f
        (.getAbsoluteFile)
        (.getParentFile)
        (.mkdirs))
    (with-open [wrtr (io/writer f)]
      (dorun
       (map #(do (.write wrtr (str %)))
            data)))))

(defn- write-stdout [data]
  (dorun (map #(println %) data)))

(defn- do-run
  [in out]
  (let [conf (load-edn in)
        res ()] ; todo parse suumo and filter based on conf
    (if out
      (write-file res out)
      (write-stdout res))))

(defn -main [& args]
  (let [[options args banner]
        (cli args
             "Useful house search alert."
             ["-i" "File where to read configuration from."]
             ["-o" "File where to store the results. Defaults to stdout."]
             ["-h" "--help" "Shows this help" :flag true])]
    (cond
      (:help options) (println banner)
      :else (do-run
             (:i options)
             (:o options)))))
