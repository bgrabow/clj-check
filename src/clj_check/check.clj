(ns clj-check.check
  (:require
   [bultitude.core :as bultitude]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn- file-for [ns] (-> ns name (str/replace \- \_) (str/replace \. \/)))

(defn- check-ns
  [ns]
  (binding [*out* *err*]
    (println "Compiling namespace" ns))
  (try
    (binding [*warn-on-reflection* true]
      (when-not (contains? (loaded-libs) ns)
        (load (file-for ns))))
    (catch ExceptionInInitializerError e
      (doto e .printStackTrace))))

(defn check
  [source-paths]
  (let [namespaces (bultitude/namespaces-on-classpath
                    :classpath (map io/file source-paths)
                    :ignore-unreadable? false)]
    (count
      (sequence
        (comp (map check-ns) (remove nil?))
        namespaces))))

(defn -main [& source-paths]
  (let [failures (check (or (seq source-paths) ["src"]))]
    (shutdown-agents)
    (when-not (zero? failures)
      (System/exit failures))))
