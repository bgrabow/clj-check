(ns client.some-client
  (:require
    [clojure.test :refer :all]
    [impl.some-impl :as some-impl]
    [protocol.some-protocol :as some-protocol]))

(some-protocol/my-method some-impl/my-o)
