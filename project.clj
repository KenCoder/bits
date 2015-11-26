(defproject clojure-getting-started "1.0.0-SNAPSHOT"
  :description "Demo Clojure web app"
  :url "http://clojure-getting-started.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2843"]
                 [compojure "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [environ "1.0.0"]
                 [domina "1.0.3"]
                 [org.clojure/java.jdbc "0.3.5"]
                 [yesql "0.5.1"]
                 [camel-snake-kebab "0.2.4"]
                 [hiccup "1.0.4"]
                 [postgresql "9.1-901-1.jdbc4"]]
  :resource-paths ["out-resources" "sql"]

  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.3.1"]
            [lein-cljsbuild "1.1.0"]
            [lein-ring "0.8.7"]]
  :cljsbuild {
              ;:crossovers [three.crossover]
              ;:crossover-path "crossover-cljs"
              :builds [{:source-paths ["src-cljs" ]
                        :compiler {:output-to "out-resources/main.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  :hooks [environ.leiningen.hooks]
  :ring {:handler clojure-getting-started.web/app}
  :uberjar-name "clojure-getting-started-standalone.jar"
  :profiles {:production {:env {:production true}}})
