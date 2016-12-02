(ns gilded-rose.core-test
  (:use expectations gilded-rose.core))

(expect [] (update-quality []))

; It does not change the name
(expect "foo"
        (:name (first (update-quality [{:name "foo" :quality 0 :sell-in 0}]))))

; It degrades twice as fast after the sell by date has passed
(expect '({:name "foo" :quality 3 :sell-in -1})
        (update-quality [{:name "foo" :quality 5 :sell-in 0}]))

; It caps the quality to a minimum of zero
(expect 0
        (:quality (first (update-quality
                           (update-quality [{:name "foo" :quality 1 :sell-in 20}])))))

; Sulfuras item
(let [items [{:name "Sulfuras, Hand of Ragnaros" :sell-in 20 :quality 10}]]
  ; It never gets old
  (expect 20
          (:sell-in (first (update-quality (update-quality items)))))

  ; It never decreases in quality
  (expect 10
          (:quality (first (update-quality (update-quality items))))))

(defn expect-to-increase-quality
  [item]
  (do
    ; It increases the quality the older it gets
    (expect (+ (:quality item) 2)
            (:quality (first (update-quality (update-quality [item])))))

    ; It caps the quality to a maximum
    (expect 50
            (:quality (first (update-quality [(assoc item :quality 49)]))))))

; "Aged Brie" item
(let [item {:name "Aged Brie" :sell-in 20 :quality 0}]
  (expect-to-increase-quality item))

; "Backstage passes..." item
(let [item {:name "Backstage passes to a TAFKAL80ETC concert" :sell-in 20 :quality 20}]
  (expect-to-increase-quality item)

  ; It drops the quality to zero after the concert
  (expect 0 (:quality (first (update-quality [(assoc item :sell-in 0)]))))
  (expect 0 (:quality (first (update-quality [(assoc item :sell-in -1)]))))

  ; It does not increase the quality by 3 when there are 6 days left
  (expect 22 (:quality (first (update-quality [(assoc item :sell-in 6)]))))

  ; It increases the quality by 3 when there are 5 days or less left
  (expect 23 (:quality (first (update-quality [(assoc item :sell-in 5)]))))

  ; It does not increase the quality by 2 when there are 11 days left
  (expect 21 (:quality (first (update-quality [(assoc item :sell-in 11)]))))

  ; It increases the quality by 2 when there are 10 days or less left
  (expect 22 (:quality (first (update-quality [(assoc item :sell-in 10)])))))
