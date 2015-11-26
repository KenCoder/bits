(ns clojure-getting-started.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [sqlingvo.db :as db]
            [clojure.java.jdbc :as db]
            [camel-snake-kebab.core :as kebab]
            [hiccup
             [page :refer [html5]]
             [page :refer [include-js]]]
            [environ.core :refer [env]]))
(def sample "this_one_started_as_snake_case")

(defn splash []
  (html5
    [:head
     [:title "Play Threes"]
     (include-js "/main.js")
     ]
    [:body "Hello world"
      ]))

(defroutes app
           (GET "/camel" {{input :input} :params}
             {:status 200
              :headers {"Content-Type" "text/plain"}
              :body })
           (GET "/" []
             (splash))
           (GET "/main.js" [] (slurp (io/resource "main.js")))
           (route/resources "/")

           (ANY "*" []
             (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
