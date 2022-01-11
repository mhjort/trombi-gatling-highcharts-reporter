(ns clojider-gatling-highcharts-reporter.core
  (:require [clojider-gatling-highcharts-reporter.reporter :refer [csv-writer]]
            [clojider-gatling-highcharts-reporter.generator :refer [create-chart]]
            [clojure.java.io :as io])
  (:import [java.time LocalDateTime]
           [java.io File]))

(defn- path-join [& paths]
  (.getCanonicalPath (apply io/file paths)))

(defn start-time []
  (LocalDateTime/now))

(defn create-dir [dir]
  (.mkdirs (File. ^String dir)))

(defn gatling-highcharts-reporter [results-dir]
  (let [input-dir (path-join results-dir "input")]
    (create-dir input-dir)
    {:writer (partial csv-writer input-dir (start-time))
     :generator (fn [_]
                  (println "Creating report from files in" results-dir)
                  (create-chart results-dir)
                  (println (str "Open " results-dir "/index.html with your browser to see a detailed report.")))}))
