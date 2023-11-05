(ns behrica.polyglot-app
  (:require
   [cheshire.core :as json]
   [clojure.pprint :as pprint]))



(defn data-fn
  "Example data-fn handler.

  Result is merged onto existing options data."
  [data]
  nil)

(def clojure-features
  {
   "ghcr.io/devcontainers-contrib/features/clojure-asdf:2" {}
   "ghcr.io/devcontainers-contrib/features/bash-command:1" {"command" "apt-get update && apt-get install -y rlwrap"}})
   ;;
   ;; {}

(def base-deps
  {'org.clojure/clojure {:mvn/version "1.11.1"}})


(defn template-fn
  "Example template-fn handler.

  Result is used as the EDN for the template."
  [edn data]

  (let [features (merge clojure-features
                        (if (:with-python data) {"ghcr.io/devcontainers/features/python:1"           {}} {})
                        (if (:with-R data)      {"ghcr.io/rocker-org/devcontainer-features/r-apt:0"  {}} {}))
        deps (merge  base-deps
                     (if (:with-python data) {'clj-python/libpython-clj {:mvn/version "2.025"}} {})
                     (if (:with-R data) {'scicloj/clojisr {:mvn/version "1.0.0-BETA21"}} {}))]


    (assoc edn
           :features-str (json/generate-string features {:pretty true})
           :deps (with-out-str (pprint/pprint deps)))))
