(ns impl.some-impl
  (:require
    [clojure.test :refer :all]
    [protocol.some-protocol :as some-protocol]))

(def my-o
  (reify some-protocol/P
    (my-method [_this] nil)))
