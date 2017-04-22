(ns clojider-gatling-highcharts-reporter.reporter-test
  (:require [clojure.test :refer :all]
            [clj-containment-matchers.clojure-test :refer :all]
            [clojure.java.io :as io]
            [clj-time.core :refer [local-date-time]]
            [clojider-gatling-highcharts-reporter.core :refer [start-time
                                                               gatling-highcharts-reporter]])
  (:import [java.io File]))

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
  ["clj-gatling\tmySimulation\tRUN\t20140209110136\t\u0020\t2.0"
   "Test scenario\t1\tUSER\tSTART\t1391936496814\t1"
   "Test scenario\t1\tREQUEST\t\tRequest1\t1391936496853\t1391936496853\t1391936497299\t1391936497299\tOK\t\u0020"
   "Test scenario\t1\tREQUEST\t\tRequest2\t1391936497299\t1391936497299\t1391936497996\t1391936497996\tKO\t\u0020"
   "Test scenario\t1\tUSER\tEND\t1391936496814\t1391936496814"])

(deftest maps-scenario-results-to-log-lines
  (create-dir "target/test-results")
  (delete-file-if-exists "target/test-results/input/simulation0.log")
  (with-redefs [start-time #(local-date-time 2014 2 9 11 1 36)]
    (let [output-writer (:writer (gatling-highcharts-reporter "target/test-results"))]
      (output-writer {:name "mySimulation"} 0 scenario-results)))
  (let [results (clojure.string/split (slurp  "target/test-results/input/simulation0.log") #"\n")]
    (is (equal? results expected-lines))))
