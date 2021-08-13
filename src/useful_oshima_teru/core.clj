(ns useful-oshima-teru.core
  (:require [useful-oshima-teru.parse :as parse]
            [clojure.tools.cli :refer [cli]]
            [clojure.java.io :as io]
            [clojure.edn :as edn])
  (:gen-class))

(defn- write-file [data file]
  (let [f (io/file file)]
    (-> f
        (.getAbsoluteFile)
        (.getParentFile)
        (.mkdirs))
    (with-open [wrtr (io/writer f)]
      (dorun
       (map #(do (.write wrtr (str %))) data)))))

(defn- write-stdout [data]
  (dorun (map #(println %) data)))

;; (defn- user-filter [conf res]
;;   (let [max-yen          (get conf :max-yen)
;;         max-walk-minutes (get conf :max-walk-minutes)
;;         min-size-meters  (get conf :min-size-meters)])
;;   (filter #(and (< (get % :price) max-yen)
;;                 (< (get-in % [:distance :walking]) max-walk-minutes)
;;                 (> (get % :size) min-size-meters))
;;           res))

(defn- do-run
  [in out]
  (let [conf       (edn/read-string (slurp (io/resource in)))
        parsed-res (parse/parse)
        res        parsed-res] ;; (user-filter conf parsed-res)
    (if out
      (write-file res out)
      (write-stdout res))))

(defn -main [& args]
  (let [[options args banner]
        (cli args
             "Useful house search alert."
             ["-i" "File where to read configuration from. Defaults to resources/example.conf" :default "example.conf"]
             ["-o" "File where to store the results. Defaults to stdout."]
             ["-h" "--help" "Shows this help" :flag true])]
    (cond
      (:help options) (println banner)
      :else (do-run
             (:i options)
             (:o options)))))
