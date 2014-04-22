(ns opendaylight-rest-wrapper.core
    (:require [clj-http.client :as client]
            [clojure.data.json :as json]))


(client/get "http://192.168.56.101:8080/controller/nb/v2/topology/default" {:basic-auth "admin:admin"})
