(ns opendaylight-rest-wrapper.core
   (:use [clojure.pprint])
    (:require [clj-http.client :as client]
            [clojure.data.json :as json]))


(def topology (client/get "http://192.168.56.101:8080/controller/nb/v2/topology/default" {:basic-auth "admin:admin"}))
(def topology-json (json/read-str (:body topology) :key-fn keyword))



(defn pprint-json-scheme
  "this fn analyze the json structure thinking that in vectors the elements repeat the same scheme,
   that's to say the elements of the vectors are of the same 'class' from an OO point of view.
   But not the same prediction on maps where each element have his identity(class) on the map, that's the point of the dictionary elements they are (class)    definitions
"
  [the-json-data]
  (let [keywordize (fn [v]
                     (let [s (.getSimpleName (type v))
                           t (keyword (clojure.string/lower-case s))]
                       (condp = t
                         :long "N"
                         :string "S"
                         :persistentarraymap "{}"
                         t)))
        deep-read (fn deep-read [jsondata]
             (cond
              (map? jsondata) (into {} (vec
                                  (map (fn [[json-key json-value]]
                                         (cond
                                          (nil? json-value) [json-key "nil"]
                                          (map? json-value) (if (empty? json-value) [json-key (keywordize {})] [json-key   (into {} (deep-read json-value))])
                                          (vector? json-value) (if (empty? json-value) [json-key []] [json-key [(deep-read (first json-value))]])
                                          :else [json-key (keywordize json-value)])) jsondata)))
              (vector? jsondata)[(deep-read (first jsondata))]
              :else (keywordize jsondata)))
        result  (into {}  (deep-read the-json-data))
        ]
    (pprint result)
    result
    )

  )

(pprint-json-scheme topology-json)

(get-in topology-json [:edgeProperties 0 :edge])

(comment
  (client/get "http://192.168.56.101:8080/controller/nb/v2/topology/default/userLinks" {:basic-auth "admin:admin"})

  (client/get "http://192.168.56.101:8080/controller/nb/v2/hosttracker/default/hosts/active" {:basic-auth "admin:admin"})
  (client/get "http://192.168.56.101:8080/controller/nb/v2/hosttracker/default/hosts/inactive" {:basic-auth "admin:admin"})


  (client/get "http://192.168.56.101:8080/controller/nb/v2/subnetservice/default/subnets" {:basic-auth "admin:admin"})

  (client/get "http://192.168.56.101:8080/controller/nb/v2/subnetservice/default/subnets" {:basic-auth "admin:admin"})
)
