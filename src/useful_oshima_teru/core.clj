(ns useful-oshima-teru.core
  (:require [useful-oshima-teru.parse :as parse]
            [clojure.tools.cli :refer [cli]]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [useful-oshima-teru.util :as util]
            [clojure.string :as str])
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

(defn- min-distance [ds]
  (apply min (map #(+ (:bus %)
                      (:walk %)) (:distance-min ds))))

(defn- should-include-location? [loc include exclude]
  (let [is-contained? (fn [el col] (seq (filter #(str/includes? % el) col)))]
    (and (or (empty? include)
             (is-contained? loc include))
         (or (empty? exclude)
             (not (is-contained? loc exclude))))))

(defn- user-filter [conf res]
  (let [max-yen          (get conf :max-yen)
        max-walk-minutes (get conf :max-walk-minutes)
        location-include (get conf :location-include)
        location-exclude (get conf :location-exclude)
        min-size-meters  (get conf :min-size-meters)]
    (filter #(and (< (get % :price-yen) max-yen)
                  (< (min-distance %) max-walk-minutes)
                  (should-include-location? (:location %) location-include location-exclude))
;;                  (> (get % :size-m) min-size-meters)
            res)))

(defn- do-run
  [in out]
  (let [conf       (edn/read-string (slurp (io/resource in)))
        parsed-res (parse/parse)
        res        (user-filter conf parsed-res)]
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
   (should-include-location? (:location a) [] ["八王子市", "調布市"])
   (min-distance a)
   (first (user-filter conf parsed-res))
   (def res        (user-filter conf parsed-res))
   (write-file res "teru.txt")
   (take 2 res))
