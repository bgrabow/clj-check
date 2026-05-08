(ns protocol.some-protocol
  (:require [clojure.test :refer :all]))

(defprotocol P
  (my-method [this]))
