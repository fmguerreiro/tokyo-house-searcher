(ns app.parser.util)

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
