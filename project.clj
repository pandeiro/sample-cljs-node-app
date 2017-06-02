(defproject example-cljs "0.1.0-SNAPSHOT"
  :dependencies [[com.cemerick/piggieback "0.2.1"]
                 [com.taoensso/timbre "4.10.0"]
                 [hiccups "0.3.0"]
                 [macchiato/core "0.1.8"]
                 [macchiato/env "0.0.6"]
                 [mount "0.1.11"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.562"]]
  :plugins [[macchiato/lein-npm "0.6.3"]
            [lein-figwheel "0.5.10"]
            [lein-cljsbuild "1.1.5"]]
  :npm {:dependencies [[source-map-support "0.4.6"]]
        :write-package-json true}
  :source-paths ["src" "target/classes"]
  :clean-targets ["target"]
  :target-path "target"
  :profiles
  {:dev
   {:npm {:package {:main "target/out/example-cljs.js"
                    :scripts {:start "node target/out/example-cljs.js"}}}
    :cljsbuild
    {:builds {:dev
              {:source-paths ["src"]
               :compiler     {:main          example-cljs.core
                              :output-to     "target/out/example-cljs.js"
                              :output-dir    "target/out"
                              :target        :nodejs
                              :optimizations :none
                              :pretty-print  true
                              :source-map    true
                              :source-map-timestamp false}}}}}
   :release
   {:npm {:package {:main "target/release/example-cljs.js"
                    :scripts {:start "node target/release/example-cljs.js"}}}
    :cljsbuild
    {:builds
     {:release
      {:source-paths ["src"]
       :compiler     {:main          example-cljs.core
                      :output-to     "target/release/example-cljs.js"
                      :language-in   :ecmascript5
                      :target        :nodejs
                      :optimizations :simple
                      :pretty-print  false}}}}}}
  :aliases
  {"build" ["do"
            ["clean"]
            ["npm" "install"]
            ["cljsbuild" "auto" "dev"]]
   "package" ["do"
              ["clean"]
              ["npm" "install"]
              ["with-profile" "release" "npm" "init" "-y"]
              ["with-profile" "release" "cljsbuild" "once"]]})
