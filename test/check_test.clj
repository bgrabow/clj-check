(ns check-test
  (:require
    [bultitude.core :as bultitude]
    [clj-check.check :as check]
    [clojure.java.io :as io]
    [clojure.string :as str]
    [clojure.test :refer :all]))

(deftest repeated-load-test
  (let [source-paths ["test/impl"
                      "test/protocol"
                      "test/client"]]
    (is (= '[impl.some-impl
             protocol.some-protocol
             client.some-client]
          (bultitude/namespaces-on-classpath
            :classpath (map io/file source-paths)
            :ignore-unreadable? false)))
    (is (str/starts-with?
          (try (check/check source-paths)
               (catch Exception e
                 (:cause (Throwable->map e))))
          "No implementation of method: :my-method of protocol: #'protocol.some-protocol/P found for class:")
      "Impl is loaded. Impl requires protocol and defines protocol. Protocol is loaded and redefines protocol.
      Client is loaded and uses impl whose object doesn't match the new copy of Protocol.")))
