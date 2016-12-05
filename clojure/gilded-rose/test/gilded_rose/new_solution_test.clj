(ns gilded-rose.new-solution-test
  (use expectations gilded-rose.new-solution))

(defn expect-to-increase-quality
  [item]
  (do
    ; It increases the quality the older it gets
    (expect (+ (:quality item) 2)
            (:quality (first (update-quality (update-quality [item])))))

    ; It caps the quality to a maximum
    (expect 50 (:quality (first (update-quality [(assoc item :quality 50)]))))))

(defn expect-to-decrease-sell-in
  [item]
  (do
    (expect -2 (:sell-in (first (update-quality [(assoc item :sell-in -1)]))))
    (expect -1 (:sell-in (first (update-quality [(assoc item :sell-in 0)]))))
    (expect 19 (:sell-in (first (update-quality [item]))))))

(expect [] (update-quality []))

; Any non-special item
(let [item {:name "foo" :quality 1 :sell-in 20}]
  (expect-to-decrease-sell-in item)

  ; It does not change the name
  (expect "foo" (:name (first (update-quality [item]))))

  ; It degrades twice as fast after the sell by date has passed
  (expect 3 (:quality (first (update-quality [{:name "foo" :quality 5 :sell-in 0}]))))

  ; It degrades if quality is greater than zero
  (expect 9 (:quality (first (update-quality [(assoc item :quality 10)]))))

  ; It caps the quality to a minimum of zero
  (expect 0 (:quality (first (update-quality [(assoc item :quality 0)])))))

; Sulfuras item
(let [items [{:name "Sulfuras, Hand of Ragnaros" :sell-in 20 :quality 10}]]
  ; It never gets old
  (expect 20 (:sell-in (first (update-quality items))))

  ; It never decreases in quality
  (expect 10 (:quality (first (update-quality items)))))

; "Aged Brie" item
(let [item {:name "Aged Brie" :sell-in 20 :quality 0}]
  (expect-to-increase-quality item)
  (expect-to-decrease-sell-in item))

; "Backstage passes..." item
(let [item {:name "Backstage passes to a TAFKAL80ETC concert" :sell-in 20 :quality 20}]
  (expect-to-increase-quality item)
  (expect-to-decrease-sell-in item)

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
