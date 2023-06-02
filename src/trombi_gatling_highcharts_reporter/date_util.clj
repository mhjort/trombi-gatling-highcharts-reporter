(ns trombi-gatling-highcharts-reporter.date-util
  (:import (java.time LocalDateTime ZoneId)))

(defn local-now []
  (LocalDateTime/now))

(defn at-system-default-zone [date-time]
  (.atZone date-time (ZoneId/systemDefault)))
