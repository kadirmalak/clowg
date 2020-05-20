(defproject clowg "0.1.3"
  :description "A Clojure library for generating Clojure wrappers around Java"
  :url "https://github.com/kadirmalak/clowg"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.processing/core "3.3.7"]
                 [org.jogamp.gluegen/gluegen-rt-main "2.3.2"]
                 [org.jogamp.jogl/jogl-all-main "2.3.2"]]
  :repl-options {:init-ns clowg.core})
