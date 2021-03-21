(ns useful-oshima-teru.util)

(defn spy
  [& args]
  (apply prn args)
  (last args))

(defmacro defn-memo [name & body]
  `(def ~name (memoize (fn ~body))))
