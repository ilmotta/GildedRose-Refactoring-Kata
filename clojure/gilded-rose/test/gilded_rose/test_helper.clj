(ns gilded-rose.test-helper
  (:use expectations))

(defn- expect-property
  [property]
  (fn [expected actual]
    (expect expected (property (first actual)))))

(def expect-quality (expect-property :quality))
(def expect-sell-in (expect-property :sell-in))
