(ns clowg.core-test
  (:require [clojure.test :refer :all]))

(require '[clowg.core :refer [get-code]])

(require '[clowg.core :refer [make-functions]])

(make-functions String)

(get-code String)

; TODO: add tests
