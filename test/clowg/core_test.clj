(ns clowg.core-test
  (:require [clojure.test :refer :all]))

(require '[clowg.core :refer [get-code get-code-str]])
(import java.util.concurrent.LinkedBlockingDeque)

(get-code-str LinkedBlockingDeque)

; TODO: add tests
