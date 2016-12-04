(ns gilded-rose.new-solution-test
  (use expectations gilded-rose.new-solution))

; Sulfuras item
; =============
(let [items [{:name "Sulfuras, Hand of Ragnaros" :sell-in 20 :quality 10}]]
  ; It never gets old
  (expect 20
          (:sell-in (first (evolve-items (evolve-items items)))))

  ; It never decreases in quality
  (expect 10
          (:quality (first (evolve-items (evolve-items items))))))

(defn new-expect-to-increase-quality
  [item]
  (do
    ; It increases the quality the older it gets
    (expect (+ (:quality item) 2)
            (:quality (first (evolve-items (evolve-items [item])))))

    ; It caps the quality to a maximum
    (expect 50
            (:quality (first (evolve-items [(assoc item :quality 50)]))))))

; "Aged Brie" item
(let [item {:name "Aged Brie" :sell-in 20 :quality 0}]
  (new-expect-to-increase-quality item)
  (expect -2
          (:sell-in (first (evolve-items [(assoc item :sell-in -1)]))))
  (expect -1
          (:sell-in (first (evolve-items [(assoc item :sell-in 0)]))))
  (expect 19
          (:sell-in (first (evolve-items [item])))))

