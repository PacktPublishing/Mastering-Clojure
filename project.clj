(defproject m-clj "0.1"
  :description "Code for Mastering Clojure"
  :uberjar-name "m-clj.jar"

  :java-agents [[co.paralleluniverse/quasar-core "0.7.3"]]
  :jvm-opts ["-Dco.paralleluniverse.pulsar.instrument.auto=all"]

  :test-paths ["spec" "test"]
  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-midje "3.1.3"]
            [speclj "3.3.1"]]

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [defun "0.2.0-RC"]
                 [org.clojure/core.match "0.2.2"
                  :exclusions [org.clojure/tools.analyzer.jvm]]
                 [org.clojure/core.typed "0.3.0"]
                 [org.clojure/core.logic "0.8.10"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.clojure/tools.trace "0.7.9"]
                 [com.climate/claypoole "1.0.0"]
                 [co.paralleluniverse/quasar-core "0.7.3"]
                 [co.paralleluniverse/pulsar "0.7.3"]
                 [iota "1.1.2"]
                 [funcool/cats "1.0.0"]
                 [io.reactivex/rxclojure "1.0.0"]
                 [spyscope "0.1.5"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.3"]

                 ;; cljs deps
                 [yolk "0.9.0"]
                 [org.omcljs/om "0.8.8"]

                 ;; tests
                 [midje "1.8.2"]
                 [speclj "3.3.1"]
                 [org.clojure/test.check "0.9.0"]]

  :injections [(require 'spyscope.core)]
  :cljsbuild {:builds [{:source-paths ["src/m_clj/c9/reactive/"]
                        :compiler {:output-to "resources/js/out/reactive.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       {:source-paths ["src/m_clj/c9/yolk/"]
                        :compiler {:output-to "resources/js/out/yolk.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       {:source-paths ["src/m_clj/c9/om/"]
                        :compiler {:output-to "resources/js/out/om.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]})
