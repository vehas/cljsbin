(ns cljsbin.routes
  (:require
   [bidi.bidi :as bidi]
   [hiccups.runtime]
   [macchiato.util.response :as r]
   [cljsbin.endpoints :as ep])
  (:require-macros
   [cljsbin.route-spec :refer [route-spec]]
   [hiccups.core :refer [html]]))

(declare routes)

(defn render-endpoint
  [[url spec]]
  (let [path-text (if (string? url) url (apply str url))
        path-href (if (string? url) path-text "LALA")] ;; TODO properly select param replacements
    [:li
     (if (:get spec)
       [:a {:data-bare-link "true", :href path-href} [:code path-text]]
       [:code path-text])
     (str " " (:doc spec))]))

;; TODO compress the html a bit
(defn home
  "This page."
  [req res raise]
  (-> (html
       [:html
        [:head
         [:meta
          {:value "text/html;charset=utf8", :http-equiv "content-type"}]
         [:meta
          {:value "Ronn/v0.7.3 (http://github.com/rtomayko/ronn/tree/0.7.3)",
           :name "generator"}]
         [:title "cljsbin: HTTP Client Testing Service"]
         [:style {:media "all", :type "text/css"}
          "\n  /* style: man */\n  body#manpage {margin:0}\n  .mp {max-width:100ex;padding:0 9ex 1ex 4ex}\n  .mp p,.mp pre,.mp ul,.mp ol,.mp dl {margin:0 0 20px 0}\n  .mp h2 {margin:10px 0 0 0}\n  .mp > p,.mp > pre,.mp > ul,.mp > ol,.mp > dl {margin-left:8ex}\n  .mp h3 {margin:0 0 0 4ex}\n  .mp dt {margin:0;clear:left}\n  .mp dt.flush {float:left;width:8ex}\n  .mp dd {margin:0 0 0 9ex}\n  .mp h1,.mp h2,.mp h3,.mp h4 {clear:left}\n  .mp pre {margin-bottom:20px}\n  .mp pre+h2,.mp pre+h3 {margin-top:22px}\n  .mp h2+pre,.mp h3+pre {margin-top:5px}\n  .mp img {display:block;margin:auto}\n  .mp h1.man-title {display:none}\n  .mp,.mp code,.mp pre,.mp tt,.mp kbd,.mp samp,.mp h3,.mp h4 {font-family:monospace;font-size:14px;line-height:1.42857142857143}\n  .mp h2 {font-size:16px;line-height:1.25}\n  .mp h1 {font-size:20px;line-height:2}\n  .mp {text-align:justify;background:#fff}\n  .mp,.mp code,.mp pre,.mp pre code,.mp tt,.mp kbd,.mp samp {color:#131211}\n  .mp h1,.mp h2,.mp h3,.mp h4 {color:#030201}\n  .mp u {text-decoration:underline}\n  .mp code,.mp strong,.mp b {font-weight:bold;color:#131211}\n  .mp em,.mp var {font-style:italic;color:#232221;text-decoration:none}\n  .mp a,.mp a:link,.mp a:hover,.mp a code,.mp a pre,.mp a tt,.mp a kbd,.mp a samp {color:#0000ff}\n  .mp b.man-ref {font-weight:normal;color:#434241}\n  .mp pre {padding:0 4ex}\n  .mp pre code {font-weight:normal;color:#434241}\n  .mp h2+pre,h3+pre {padding-left:0}\n  ol.man-decor,ol.man-decor li {margin:3px 0 10px 0;padding:0;float:left;width:33%;list-style-type:none;text-transform:uppercase;color:#999;letter-spacing:1px}\n  ol.man-decor {width:100%}\n  ol.man-decor li.tl {text-align:left}\n  ol.man-decor li.tc {text-align:center;letter-spacing:4px}\n  ol.man-decor li.tr {text-align:right;float:right}\n  "]
         [:style {:media "all", :type "text/css"}
          "\n  /* style: 80c */\n  .mp {max-width:86ex}\n  ul {list-style: None; margin-left: 1em!important}\n  .man-navigation {left:101ex}\n  "]]
        [:body#manpage
         [:a {:href "http://github.com/facundoolano/cljsbin"}
          [:img {:alt "Fork me on GitHub",
                 :src "https://s3.amazonaws.com/github/ribbons/forkme_right_darkblue_121621.png",
                 :style "position: absolute; top: 0; right: 0; border: 0;"}]]
         [:div.mp
          [:h1 "cljsbin: HTTP Request & Response Service"]
          [:h2#ENDPOINTS "ENDPOINTS"]
          [:ul (map render-endpoint (second routes))]
          [:h2#DESCRIPTION "DESCRIPTION"]
          [:p
           "Testing an HTTP Library can become difficult sometimes. "
           [:a {:href "http://requestb.in"} "RequestBin"]
           " is fantastic for testing POST requests, but doesn't let you control the response. This exists to cover all kinds of HTTP scenarios. Additional endpoints are being considered."]
          [:p "All endpoint responses are JSON-encoded."]
          [:h2#EXAMPLES "EXAMPLES"]
          [:h3#-curl-http-httpbin-org-ip "$ curl http://cljsbin.org/ip"]
          [:pre [:code "{\"origin\": \"24.127.96.129\"}\n"]]
          [:h3#-curl-http-httpbin-org-user-agent
           "$ curl http://cljsbin.org/user-agent"]
          [:pre
           [:code
            "{\"user-agent\": \"curl/7.19.7 (universal-apple-darwin10.0) libcurl/7.19.7 OpenSSL/0.9.8l zlib/1.2.3\"}\n"]]
          [:h3#-curl-http-httpbin-org-get "$ curl http://cljsbin.org/get"]
          [:pre
           [:code
            "{\n   \"args\": {},\n   \"headers\": {\n      \"Accept\": \"*/*\",\n      \"Connection\": \"close\",\n      \"Content-Length\": \"\",\n      \"Content-Type\": \"\",\n      \"Host\": \"cljsbin.org\",\n      \"User-Agent\": \"curl/7.19.7 (universal-apple-darwin10.0) libcurl/7.19.7 OpenSSL/0.9.8l zlib/1.2.3\"\n   },\n   \"origin\": \"24.127.96.129\",\n   \"url\": \"http://cljsbin.org/get\"\n}\n"]]
          [:h3#-curl-I-http-httpbin-org-status-418
           "$ curl -I http://cljsbin.org/status/418"]
          [:pre
           [:code
            "HTTP/1.1 418 I'M A TEAPOT\nServer: nginx/0.7.67\nDate: Mon, 13 Jun 2011 04:25:38 GMT\nConnection: close\nx-more-info: http://tools.ietf.org/html/rfc2324\nContent-Length: 135\n"]]
          [:h3#-curl-https-httpbin-org-get-show_env-1
           "$ curl https://cljsbin.org/get?show_env=1"]
          [:pre
           [:code
            "{\n  \"headers\": {\n    \"Content-Length\": \"\",\n    \"Accept-Language\": \"en-US,en;q=0.8\",\n    \"Accept-Encoding\": \"gzip,deflate,sdch\",\n    \"X-Forwarded-Port\": \"443\",\n    \"X-Forwarded-For\": \"109.60.101.240\",\n    \"Host\": \"httpbin.org\",\n    \"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\",\n    \"User-Agent\": \"Mozilla/5.0 (X11; Linux i686) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.83 Safari/535.11\",\n    \"X-Request-Start\": \"1350053933441\",\n    \"Accept-Charset\": \"ISO-8859-1,utf-8;q=0.7,*;q=0.3\",\n    \"Connection\": \"keep-alive\",\n    \"X-Forwarded-Proto\": \"https\",\n    \"Cookie\": \"_gauges_unique_day=1; _gauges_unique_month=1; _gauges_unique_year=1; _gauges_unique=1; _gauges_unique_hour=1\",\n    \"Content-Type\": \"\"\n  },\n  \"args\": {\n    \"show_env\": \"1\"\n  },\n  \"origin\": \"109.60.101.240\",\n  \"url\": \"http://httpbin.org/get?show_env=1\"\n}\n"]]
          [:h2#Installing-and-running-from-PyPI
           "Installing and running from github"]
          [:p
           "You can install httpbin as a library from PyPI and run it as a WSGI app.  For example, using Gunicorn:"]
          [:pre
           [:code.bash "$ pip install httpbin\n$ gunicorn httpbin:app\n"]]
          [:h2#AUTHOR "AUTHOR"]
          [:p
           "Originally created by "
           [:a {:href "http://github.com/facundoolano"} "Facundo Olano"]
           "."]
          [:h2#SEE-ALSO "SEE ALSO"]
          [:p
           [:a {:href "https://httpbin.org"} "httpbin.org"]
           " HTTP Request & Response Service"]
          [:p
           [:a {:href "http://requestb.in"} "RequestBin"]
           " - Inspect HTTP requests."]]]])


      (r/ok)
      (r/content-type "text/html")
      (res)))

(defn not-found [req res raise]
  (-> (html
       [:html
        [:body
         [:h2 (:uri req) " was not found"]]])
      (r/not-found)
      (r/content-type "text/html")
      (res)))

(defn form-post
  "HTML form that submits to /post"
  [req res raise]
  (-> (html
       [:head]
       [:body
        [:form
         {:action (bidi/path-for routes :post) :method "post"}
         [:p [:label "Customer name: " [:input {:name "custname"}]]]
         [:p [:label "Telephone: " [:input {:name "custtel" :type "tel"}]]]
         [:p [:label "E-mail address: " [:input {:name "custemail" :type "email"}]]]
         [:fieldset [:legend " Pizza Size "]
          [:p [:label " " [:input {:value "small" :name "size" :type "radio"}] " Small "]]
          [:p [:label " " [:input {:value "medium" :name "size" :type "radio"}] " Medium "]]
          [:p [:label " " [:input {:value "large" :name "size" :type "radio"}] " Large "]]]
         [:fieldset [:legend " Pizza Toppings "]
          [:p [:label " " [:input {:value "bacon" :name "topping" :type "checkbox"}] " Bacon "]]
          [:p [:label " " [:input {:value "cheese" :name "topping" :type "checkbox"}] " Extra Cheese "]]
          [:p [:label " " [:input {:value "onion" :name "topping" :type "checkbox"}] " Onion "]]
          [:p [:label " " [:input {:value "mushroom" :name "topping" :type "checkbox"}] " Mushroom "]]]
         [:p [:label "Preferred delivery time: "
              [:input {:name "delivery" :step "900" :max "21:00" :min "11:00" :type "time"}]]]
         [:p [:label "Delivery instructions: " [:textarea {:name "comments"}]]]
         [:p [:button "Submit order"]]]])
      (r/ok)
      (r/content-type "text/html")
      (res)))

(defn links-page
  "Generate a page with n links"
  ([n index]
   (html
    [:head [:title "Links"]]
    [:body
     (mapcat #(if (= (or index "0") (str %))
                (str " " % " ")
                [[:a {:href (bidi/path-for routes :links :n n :index %)} (str % )] " "])
             (range n))])))

(defn links
  "Returns page containing n HTML links."
  [req res raise]
  (-> (links-page (get-in req [:route-params :n]) (get-in req [:route-params :index]))
      (r/ok)
      (r/content-type "text/html")
      (res)))

(defn redirect
  "302 relative redirects n times."
  [req res raise]
  (let [times (js/parseInt (get-in req [:route-params :n]))]
    (if (integer? times)
      (-> (if (= 1 times)
            (bidi/path-for routes ep/get_)
            (bidi/path-for routes redirect :n (- times 1)))
          (r/found)
          (res))
      (raise (js/Error "Not a valid cache age.")))))

(defn- absolute-path
  [req path]
  (str (-> req :scheme name)
       "://"
       (get-in req [:headers "host"])
       path))

(defn absolute-redirect
  "302 absolyte redirects n times."
  [req res raise]
  (let [times (js/parseInt (get-in req [:route-params :n]))]
    (if (integer? times)
      (->> (if (= 1 times)
             (bidi/path-for routes ep/get_)
             (bidi/path-for routes absolute-redirect :n (- times 1)))
           (absolute-path req)
           (r/found)
           (res))
      (raise (js/Error "Not a valid cache age.")))))

(defn redirect-to
  "302 Redirects to the given URL."
  [req res raise]
  (-> (get-in req [:query-params "url"])
      (r/found)
      (res)))

;; TODO consider with/without trailing slashes?
;; Define the route mappings in order and with a spec that contains all data
;; needed to generate the index in the home
(def routes ["" (array-map "/" (route-spec :get home)
                           "/ip" (route-spec :get ep/ip)
                           "/user-agent" (route-spec :get ep/user-agent)
                           "/headers" (route-spec :get ep/headers)
                           "/get" (route-spec :get ep/get_)
                           "/post" (route-spec :post ep/post :bidi-tag :post)
                           "/put" (route-spec :put ep/put)
                           "/patch" (route-spec :patch ep/patch)
                           "/delete" (route-spec :delete ep/delete)
                           "/encoding/utf8" (route-spec :get ep/encoding)
                           "/xml" (route-spec :get ep/xml)
                           "/html" (route-spec :get ep/html)
                           "/robots.txt" (route-spec :get ep/robots)
                           "/deny" (route-spec :get ep/deny)
                           "/cache" (route-spec :get ep/cache)
                           ["/cache/" :n] (route-spec :get ep/cache-seconds)
                           ["/status/" :status] (route-spec :get ep/status)
                           ["/delay/" :n] (route-spec :get ep/delay_)
                           "/response-headers" (route-spec :get ep/response-headers)
                           "/cookies" (route-spec :get ep/cookies)
                           "/cookies/set" (route-spec :get ep/set-cookies)
                           "/cookies/delete" (route-spec :get ep/delete-cookies)
                           "/image" (route-spec :get ep/image)
                           "/image/png" (route-spec :get ep/image-png)
                           "/image/webp" (route-spec :get ep/image-webp)
                           "/image/svg" (route-spec :get ep/image-svg)
                           "/image/jpeg" (route-spec :get ep/image-jpeg)
                           ["/basic-auth/" :user "/" :pass] (route-spec :get ep/basic-auth)
                           ["/hidden-basic-auth/" :user "/" :pass] (route-spec :get ep/hidden-basic-auth)
                           ["/digest-auth/" :user "/" :pass] (route-spec :get ep/digest-auth)
                           "/compress" (route-spec :get ep/compress)
                           "/forms/post" (route-spec :get form-post)
                           ["/links/" :n "/" :index] (route-spec :get links :bidi-tag :links)
                           ["/links/" :n] (route-spec :get links)
                           ["/redirect/" :n] (route-spec :get redirect)
                           ["/absolute-redirect/" :n] (route-spec :get absolute-redirect)
                           "/redirect-to" (route-spec :get redirect-to))])

(defn router [req res raise]
  (if-let [{:keys [handler route-params]} (bidi/match-route* routes (:uri req) req)]
    (handler (assoc req :route-params route-params) res raise)
    (not-found req res raise)))
