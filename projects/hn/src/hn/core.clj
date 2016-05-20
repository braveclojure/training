(ns hn.core
  (:import [org.openqa.selenium By WebDriver WebElement]
           [org.openqa.selenium.firefox FirefoxDriver])
  (:gen-class))

(defn scrape
  []
  (let [driver (doto (FirefoxDriver.)
                 (.get "http://news.ycombinator.com"))]

    
    (let [titles (->> (.findElements driver (By/cssSelector ".title a:nth-child(2)"))
                      (map #(.getText %)))]
      (doseq [title titles]
        (println title)))

    ;; closes the browser
    (.quit driver)))

(defn -main
  [& args]
  (scrape))
