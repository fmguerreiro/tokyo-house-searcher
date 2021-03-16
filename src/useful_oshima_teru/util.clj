(ns useful-oshima-teru.util)

(defmacro defn-memo [name & body]
  `(def ~name (memoize (fn ~body))))
