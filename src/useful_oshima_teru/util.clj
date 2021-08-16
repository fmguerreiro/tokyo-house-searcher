(ns useful-oshima-teru.util)

(defn spy
  [& args]
  (apply prn args)
  (last args))

(defmacro defn-memo [name & body]
  `(def ~name (memoize (fn ~body))))

(defn parse-int [s] (Integer/parseInt s))
(def parse-int-with-def (fnil parse-int "0"))
