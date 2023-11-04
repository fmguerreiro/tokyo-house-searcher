(ns app.scraper.util)

(defn spy
  [& args]
  (apply prn args)
  (last args))

(defmacro defn-memo [name & body]
  `(def ~name (memoize (fn ~body))))

(defn parse-int [s] (Integer/parseInt s))
(def parse-int-with-def (fnil parse-int "0"))

(defn unchunk [s]
  (when (seq s)
    (lazy-seq
      (cons (first s)
            (unchunk (next s))))))

(defn env-dynamic
  [k]
  (if-let [v (-> k name System/getenv)]
    v
    (throw (ex-info (str "Missing env variable: " k)
                    {:missing-env-variable k}))))

(defmacro env
  [k]
  (env-dynamic k))
