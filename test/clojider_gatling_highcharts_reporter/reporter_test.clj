(ns clojider-gatling-highcharts-reporter.reporter-test
  (:require [clojure.test :refer :all]
            [clj-containment-matchers.clojure-test :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [clojider-gatling-highcharts-reporter.core :refer [current-time
                                                               gatling-highcharts-reporter]])
  (:import (java.io File)
           (java.time ZonedDateTime ZoneId)))

(defn create-dir [^String dir]
    (.mkdirs (File. dir)))

(defn- delete-file-if-exists [path]
  (when (.exists (io/as-file path))
    (io/delete-file path)))

(def scenario-results
  [{:name "Test scenario" :id 1 :start 1391936496814 :end 1391936496814
    :requests [{:id 1 :name "Request1" :start 1391936496853 :end 1391936497299 :result true}
               {:id 1 :name "Request2" :start 1391936497299 :end 1391936497996 :result false}]}])

(def expected-lines
  ["RUN\tclj-gatling\tmySimulation\t1391936496000\t\u0020\t3.7.4"
   "USER\tTest scenario\tSTART\t1391936496814"
   "REQUEST\t\tRequest1\t1391936496853\t1391936497299\tOK\t\u0020"
   "REQUEST\t\tRequest2\t1391936497299\t1391936497996\tKO\t\u0020"
   "USER\tTest scenario\tEND\t1391936496814"])

(deftest maps-scenario-results-to-log-lines
  (create-dir "target/reporter-test")
  (delete-file-if-exists "target/reporter-test/input/simulation0.log")
  (with-redefs [current-time #(ZonedDateTime/of 2014 2 9 9 1 36 0 (ZoneId/of "UTC"))]
    (let [output-writer (:writer (gatling-highcharts-reporter "target/reporter-test"))]
      (output-writer {:name "mySimulation"} 0 scenario-results)))
  (let [results (str/split (slurp "target/reporter-test/input/simulation0.log") #"\n")]
    (is (equal? results expected-lines))))
