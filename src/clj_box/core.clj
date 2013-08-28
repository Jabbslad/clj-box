(ns clj-box.core
	"Clojure Xbox Live client"
	(:require [clj-http.client :as client]))

(def ^:dynamic *cookie-store* (clj-http.cookies/cookie-store))
(def ^:dynamic *signin-url* "https://live.xbox.com/en-US/Friends")

(defn get-page-data [body patterns]
	(reduce #(assoc %1 (first %2) (last %2)) {} (for [[k v] patterns] [k (first (rest (re-find v body)))])))

(defn- request [method url & [opts]]
  (client/request
    (merge {:method method :url url :cookie-store *cookie-store*} opts)))

(defn- check-signin [username password]
	(let [opts (get-page-data (:body (request :get *signin-url*)) {:url #"urlPost:'(.+?)'" :PPFT #"PPFT.*value=\"(.+?)\""})]
		(request :post (:url opts)
			{:form-params (merge opts {:login username
									   :passwd password
									   :type 11
									   :LoginOptions 3
									   :PPSX "Passp"})})))

(defn- set-cookies [form-data])

(defn login 
	"Take a username and password and logs the user in"
	[username password]
	(check-signin username password))


