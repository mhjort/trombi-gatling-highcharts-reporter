(ns clojider-gatling-highcharts-reporter.core
  (:require [trombi-gatling-highcharts-reporter.core :as trombi]))

;This is here for backwards compatibility reasons
(defn gatling-highcharts-reporter [results-dir]
  (trombi/gatling-highcharts-reporter results-dir))
