(ns gilded-rose.new-solution)

(defn indestructible?
  [name]
  (= name "Sulfuras, Hand of Ragnaros"))

(defn aged-brie?
  [name]
  (= name "Aged Brie"))

(defn evolve-item
  [{:keys [name sell-in quality] :as item}]
  (cond
    (aged-brie? name) {:quality #(min 50 (inc %)) :sell-in dec}
    (indestructible? name) {:quality identity :sell-in identity}
    :else {:quality identity :sell-in identity}))

(defn evolve-items
  [items]
  (map (fn [item]
         (update (update item :quality (:quality (evolve-item item)))
                 :sell-in (:sell-in (evolve-item item))))
        items))

(defn log [step]
  (fn [item] (do
               (println (str "step-" step)  item)
               item)))
