(ns clj-box.core
	"Clojure Xbox Live client"
	(:require [clj-http.client :as client]))

(def ^:dynamic *cookie-store* (clj-http.cookies/cookie-store))
(def ^:dynamic *signin-url* "https://live.xbox.com/en-US/Friends")

(defn get-page-data [pattern body]
	(first (rest (re-find pattern body))))

(defn login 
	"Take a username and password and logs the user in"
	[username password]
	(let [response (client/get *signin-url* {:cookie-store *cookie-store*}) body (:body response)]
		(client/post (get-page-data #"urlPost:'(.+?)'" body) 
			{:cookie-store *cookie-store* :form-params {:login username
						   :passwd password
						   :type 11
						   :LoginOptions 3
						   :PPSX "Passp"
						   :PPFT (get-page-data #"PPFT.*value=\"(.+?)\"" body)}})))


