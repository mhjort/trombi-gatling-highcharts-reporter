(ns clojider-gatling-highcharts-reporter.core
  (:require [clojider-gatling-highcharts-reporter.reporter :refer [csv-writer]]
            [clojider-gatling-highcharts-reporter.generator :refer [create-chart]]
            [clojure.java.io :as io])
  (:import [org.joda.time LocalDateTime]))

(defn- path-join [& paths]
  (.getCanonicalPath (apply io/file paths)))

(defn start-time []
  (LocalDateTime.))

(defn gatling-highcharts-reporter [results-dir]
  {:writer (partial csv-writer (path-join results-dir "input") (start-time))
   :generator (fn [_]
                (println "Creating report from files in" results-dir)
                (create-chart results-dir)
                (println (str "Open " results-dir "/index.html with your browser to see a detailed report." )))})
