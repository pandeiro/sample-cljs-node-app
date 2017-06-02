(ns example-cljs.core
  (:require
   [cljs.reader :as edn]
   [cljs.nodejs :as node]
   [macchiato.server :as http]))

(defonce db
  (atom {:submissions []}))

(defn handler [req res raise]
  (-> (case (:request-method req)
        :post (let [data (edn/read-string (:body req))]
                (swap! db update-in [:submissions] conj data)
                {:status 201, :body (pr-str {:ok true})})
        {:status 200, :body (pr-str @db)})
    (res)))

(defn wrap-body [handler]
  (let [concat-stream (node/require "concat-stream")
        assoc-body    (fn [req buf]
                        (assoc req :body (.toString buf)))]
    (fn [req res raise]
      (.pipe (:body req) (new concat-stream
                              #(handler (assoc-body req %) res raise))))))

(defn main [& [port]]
  (http/start {:handler (wrap-body handler) :port (js/parseInt (or port "8000"))}))

(set! *main-cli-fn* main)
