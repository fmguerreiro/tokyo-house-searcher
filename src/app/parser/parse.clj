(ns app.parser.parse
  (:require
   [app.parser.fetch :as fetch]
   [net.cgrand.enlive-html :as html]
   [clojure.string :as str]))

(defn- html->map [x]
     (let [map1 {:id (get-in (second (:content (first (html/select x [:.cassetteitem_object-item])))) [:attrs :src]) ;; TODO: to get id? 100342839692 in this case? "https://img01.suumo.com/front/gazo/fr/bukken/692/100342839692/100342839692_gw.jpg"
                 :price (first (html/select x [:.cassetteitem_price--rent > html/text-node])) ;; "4万円"
                 :size (first (html/select x [:.cassetteitem_menseki html/text-node])) ;; "18.22m"
                 :location (first (html/select x [:.cassetteitem_detail-col1 html/text-node])) ;; "東京都日野市多摩平６"
                 :transportation (str/join " " (html/select x [:.cassetteitem_detail-col2 > html/text-node])) ;; ("ＪＲ中央線/日野駅 歩185分" "ＪＲ中央線/豊田駅 歩15分" "京王線/南平駅 歩30分")
                 :link (get-in (second (:content (first (html/select x [:.ui-text--midium])))) [:attrs :href]) ;; "/chintai/jnc_000084910027/?bc=100343599855"
                 }
           map2 (as-> (html/select x [:.cassetteitem_detail-col3 > html/text-node]) $
                  {:building-age (first $) :building-floor (second $)}) ;; ("築35年" "3階建")
           ]
       (merge map1 map2)))


(defn scrape-suumo
  []
  (let [first-page (fetch/fetch 1)
        page-count (Integer/parseInt (first (html/select first-page [:.pagination-parts html/last-child > html/text-node])))]
    (->> (map #(fetch/fetch %) (range 1 (min 3 page-count))) ; TODO: remove max when done, also, do it slowly
         (map #(html/select % [:.cassetteitem])) ;; listings list
         (flatten) ;; TODO: this is probably pretty inefficient, ah well
         (map #(html->map %)))))

;; (defn- parse-distance [ds]
;;   (let [[_ line station] (re-matches #"(.+)/([^\s]+) .*" ds)
;;         [_ bus] (re-matches #".*バス(\d+)分.*" ds)
;;         [_ walk] (re-matches #".*歩(\d+)分.*" ds)]
;;     {:line line, :station station, :walk (util/parse-int-with-def walk), :bus (util/parse-int-with-def bus)}))

;; (defn- get-yen-amount [s]
;;   (let [[n _] (re-find #"[+-]?([0-9]*[.])?[0-9]+" s)]
;;     (* 10000 (Double/parseDouble n))))

;; scratch
#_((str/join "+" (map #(codec/percent-encode %) ["告知事項", "hello"]))
   (select *base-url* *distance-selector*)
   (def ls (get-listings *base-url*))

   (def url (str *base-url* (str/join "+" (map #(codec/percent-encode %) *filter*))))
   (def memoed-page (fetch-url url))

   (def page-count (Integer/parseInt (first (html/select memoed-page [:.pagination-parts html/last-child > html/text-node]))))
   (def listings (html/select memoed-page [:.cassetteitem]))
   (defn html->map [x]
     (let [map1 {:id (get-in (second (:content (first (html/select x [:.cassetteitem_object-item])))) [:attrs :src]) ;; TODO: to get id? 100342839692 in this case? "https://img01.suumo.com/front/gazo/fr/bukken/692/100342839692/100342839692_gw.jpg"
                 :price (first (html/select x [:.cassetteitem_price--rent > html/text-node])) ;; "4万円"
                 :size (first (html/select x [:.cassetteitem_menseki html/text-node])) ;; "18.22m"
                 :location (first (html/select x [:.cassetteitem_detail-col1 html/text-node])) ;; "東京都日野市多摩平６"
                 :transportation (str/join " " (html/select x [:.cassetteitem_detail-col2 > html/text-node])) ;; ("ＪＲ中央線/日野駅 歩185分" "ＪＲ中央線/豊田駅 歩15分" "京王線/南平駅 歩30分")
                 :link (get-in (second (:content (first (html/select x [:.ui-text--midium])))) [:attrs :href]) ;; "/chintai/jnc_000084910027/?bc=100343599855"
                 }
           map2 (as-> (html/select x [:.cassetteitem_detail-col3 > html/text-node]) $
                  {:building-age (first $) :building-floor (second $)}) ;; ("築35年" "3階建")
           ]
       (merge map1 map2)))
   (first (map html->map listings))
   (def house {:id "/edit/assets/suumo/img/img_nowprinting_180_180.jpg", :price "4万円", :size "18.22m", :location "東京都日野市多摩平６", :transportation "ＪＲ中央線/日野駅 歩185分 ＪＲ中央線/豊田駅 歩15分 京王線/南平駅 歩30分", :link "/chintai/jnc_000084910027/?bc=100343599855", :building-age "築35年", :building-floor "3階建"})

   (def first-page (fetch/fetch 1))
   (def page-count (Integer/parseInt (first (html/select first-page [:.pagination-parts html/last-child > html/text-node]))))
   (def pages (map #(fetch/fetch %) (range 1 (min 3 page-count))))
   (def listings (map #(html/select % [:.cassetteitem]) pages))
   (flatten listings)
   (def mapped-listings (map #(html->map %) (flatten listings)))
   (def first-res (first mapped-listings))
   (map first-res [:id :price :size :location :transportation :building-info :link])

   (def eg (->  ls second (html/select [:div.detailnote-box :> :div :> [html/text-node (html/text-pred #(re-matches #".*" %))]]))))
