(ns clojider-gatling-highcharts-reporter.core
  (:require [clojider-gatling-highcharts-reporter.reporter :refer [csv-writer]]
            [clojider-gatling-highcharts-reporter.generator :refer [create-chart]]
            [clojure.java.io :as io])
  (:import (java.io File)
           (java.time ZonedDateTime)))

(defn- path-join [& paths]
  (.getCanonicalPath (apply io/file paths)))

(defn current-time []
  (ZonedDateTime/now))

(defn create-dir [dir]
  (.mkdirs (File. ^String dir)))

(defn gatling-highcharts-reporter [results-dir]
  (let [input-dir (path-join results-dir "input")]
    (create-dir input-dir)
    {:writer (partial csv-writer input-dir (current-time))
     :generator (fn [_]
                  (println "Creating report from files in" results-dir)
                  (create-chart results-dir)
                  (println ))}))

(def collector
  (fn [{:keys [results-dir]}]
    (let [start-time (current-time)
          input-dir (path-join results-dir "input")]
      (create-dir input-dir)
      {:collect (fn [simulation {:keys [batch _ batch-id]}]
                  (csv-writer input-dir start-time simulation batch-id batch))
       :combine concat})))

(def generator
  (fn [{:keys [results-dir]}]
    {:generate (fn [_]
                 (println "Creating report from files in" results-dir)
                 (create-chart results-dir))
     :as-str (constantly (str "Open " results-dir "/index.html with your browser to see a detailed report."))}))

(def reporter
  {:reporter-key :highcharts
   :collector 'clojider-gatling-highcharts-reporter.core/collector
   :generator 'clojider-gatling-highcharts-reporter.core/generator})
