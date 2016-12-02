(ns gilded-rose.core
  (:gen-class))

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

(defn update-item
  [{:keys [name quality sell-in] :as item}]
  ((comp
     (fn [{:keys [name quality sell-in] :as item}]
       (if (< sell-in 0)
         (if (not= name "Aged Brie")
           (if (not= name "Backstage passes to a TAFKAL80ETC concert")
             (if (> quality 0)
               (if (not= name "Sulfuras, Hand of Ragnaros")
                 (update item :quality dec)
                 item)
               item)
             (assoc item :quality 0))
           (if (< quality 50)
             (update item :quality inc)
             item))
         item))
     (fn [{:keys [name quality sell-in] :as item}]
       (if (not= name "Sulfuras, Hand of Ragnaros")
         (update item :sell-in dec)
         item))
     (fn [{:keys [name quality sell-in] :as item}]
       (if (and (not= name "Aged Brie") (not= name "Backstage passes to a TAFKAL80ETC concert"))
         (if (> quality 0)
           (if (not= name "Sulfuras, Hand of Ragnaros")
             (update item :quality dec)
             item)
           item)
         (if (< quality 50)
           (let [item (update item :quality inc)]
             (if (= name "Backstage passes to a TAFKAL80ETC concert")
               ((comp
                  (fn [item]
                    (if (< sell-in 6)
                      (if (< quality 50)
                        (update item :quality inc)
                        item)
                      item))
                  (fn [item]
                    (if (< sell-in 11)
                      (if (< quality 50)
                        (update item :quality inc)
                        item)
                      item)))
                item)
               item))
           item))))
   item))

(defn update-quality
  [items]
  (map update-item items))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ())
