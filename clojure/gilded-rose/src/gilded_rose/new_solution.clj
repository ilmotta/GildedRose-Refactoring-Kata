(ns gilded-rose.new-solution)

(defn indestructible?
  [name]
  (= name "Sulfuras, Hand of Ragnaros"))

(defn aged-brie?
  [name]
  (= name "Aged Brie"))

(defn backstage?
  [name]
  (= name "Backstage passes to a TAFKAL80ETC concert"))

(defn update-generic
  [{:keys [sell-in] :as item}]
  {:sell-in dec
   :quality #(cond
               (<= sell-in 0) (- % 2)
               (> % 0) (dec %)
               :else %)})

(defn update-backstage
  [{:keys [sell-in] :as item}]
  {:sell-in dec
   :quality #(cond
               (<= sell-in 0) 0
               (< sell-in 6) (min 50 (+ % 3))
               (<= sell-in 10) (min 50 (+ % 2))
               :else (min 50 (inc %)))})

(defn update-aged-brie
  []
  {:quality #(min 50 (inc %)) :sell-in dec})

(defn update-indestructible
  []
  {:quality identity :sell-in identity})

(defn update-item-quality
  [{:keys [name sell-in quality] :as item}]
  (cond
    (backstage? name) (update-backstage item)
    (aged-brie? name) (update-aged-brie)
    (indestructible? name) (update-indestructible)
    :else (update-generic item)))

(defn update-quality
  [items]
  (map (fn [item]
         (update (update item :quality (:quality (update-item-quality item)))
                 :sell-in (:sell-in (update-item-quality item))))
        items))

(defn log [step]
  (fn [item] (do
               (println (str "step-" step)  item)
               item)))
