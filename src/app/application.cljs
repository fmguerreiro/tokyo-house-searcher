(ns app.application
  (:require [app.model.house :as model]
            [com.fulcrologic.fulcro.application :as app]
            [com.fulcrologic.fulcro.networking.http-remote :as http]
            [com.fulcrologic.fulcro.rendering.keyframe-render2 :as keyframe-render2]
            [com.fulcrologic.fulcro.data-fetch :as df]))

(defonce APP (app/fulcro-app {:remotes          {:remote (http/fulcro-http-remote {})}
                              :client-did-mount (fn [app]
                                                  (df/load! app :all-houses model/House
                                                    {:target [:component/id ::house-list :house-list/houses]}))
                              :optimized-render! keyframe-render2/render!}))
