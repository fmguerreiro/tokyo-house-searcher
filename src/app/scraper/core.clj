;; TODO: old file, remove, after making sure it's no longer useful
(ns app.scraper.core
  (:require [app.scraper.parse :as parse]
            [app.scraper.filter :as filter]
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
       (map #(do (.write wrtr (str % "\n"))) data)))))

(defn- write-stdout [data]
  (dorun (map #(println %) data)))

(defn- do-run
  [in out]
  (let [conf       (edn/read-string (slurp (io/resource in)))
        parsed-res (binding [parse/*filter* (:keywords conf)] (parse/parse))
        res        (filter/filter-listings conf parsed-res)]
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

;; scratch
#_((def in "example.conf")
   (def conf (edn/read-string (slurp (io/resource in))))
   (def parsed-res (parse/parse))
   (def a (first parsed-res))
   (first (user-filter conf parsed-res))
   (def res        (user-filter conf parsed-res))
   (write-file res "teru.txt")
   (take 2 res))
