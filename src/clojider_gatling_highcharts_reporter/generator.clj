(ns clojider-gatling-highcharts-reporter.generator
  (:import (io.gatling.charts.report ReportsGenerationInputs ReportsGenerator)
           (io.gatling.charts.stats LogFileReader)
           (io.gatling.core.config GatlingConfiguration)
           (scala.collection.immutable List$)
           (scala.collection.mutable HashMap)))

(defn create-chart [results-dir]
  (let [conf-map (doto (HashMap.)
                   (.put "gatling.core.directory.results" results-dir))
        conf     (GatlingConfiguration/load conf-map)
        reader (LogFileReader. "input" conf)
        ;; The list of AssertionResults doesn't appear to be used for report generation
        inputs (ReportsGenerationInputs. results-dir reader (.empty List$/MODULE$))]
    (-> (ReportsGenerator. conf)
        (.generateFor inputs))))
