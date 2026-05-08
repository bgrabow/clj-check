(ns check-test
  (:require
    [bultitude.core :as bultitude]
    [clj-check.check :as check]
    [clojure.java.io :as io]
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
    (is (= 0 (check/check source-paths))
      "nses are only loaded once, so a load-require-load-use bug doesn't happen")
    (is (= '[impl.some-impl
             protocol.some-protocol
             client.some-client
             bad.bad]
          (bultitude/namespaces-on-classpath
            :classpath (map io/file (conj source-paths "test/bad"))
            :ignore-unreadable? false)))
    (is (= "Could not locate doesnt/exist__init.class, doesnt/exist.clj or doesnt/exist.cljc on classpath."
          (try (check/check ["test/bad"])
               (catch Exception e (:cause (Throwable->map e)))))
      "check still catches bad namespaces")))


