(ns gilded-rose.new-solution)

(def BACKSTAGE-NAME "Backstage passes to a TAFKAL80ETC concert")
(def AGED-BRIE-NAME "Aged Brie")
(def SULFURAS-NAME "Sulfuras, Hand of Ragnaros")

(defn- item-updater
  [{:keys [name sell-in quality]}]
  (condp = name
    BACKSTAGE-NAME
    {:sell-in dec
     :quality #(cond
                 (<= sell-in 0) 0
                 (< sell-in 6) (min 50 (+ % 3))
                 (<= sell-in 10) (min 50 (+ % 2))
                 :else (min 50 (inc %)))}

    AGED-BRIE-NAME
    {:quality #(min 50 (inc %)) :sell-in dec}

    SULFURAS-NAME
    {:quality identity :sell-in identity}

    {:sell-in dec
     :quality #(cond
                 (<= sell-in 0) (- % 2)
                 (> % 0) (dec %)
                 :else %)}))

(defn- update-item
  [item prop]
  (update item prop (prop (item-updater item))))


(defn update-quality
  [items]
  (map (fn [item] (reduce #(update-item %1 %2) item [:quality :sell-in]))
       items))
