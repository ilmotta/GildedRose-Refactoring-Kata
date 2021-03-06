(ns gilded-rose.original-solution)

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
