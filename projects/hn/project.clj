(defproject hn "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.seleniumhq.selenium/selenium-java "2.53.0"]
                 [org.seleniumhq.selenium/htmlunit-driver "2.21"]]
  :main ^:skip-aot hn.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
