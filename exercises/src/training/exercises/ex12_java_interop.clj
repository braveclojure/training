(ns training.exercises.ex12-java-interop
  (:import [java.util Date]
           [org.openqa.selenium By WebDriver WebElement]
           [org.openqa.selenium.firefox FirefoxDriver]))

;; note to self: start with slides

;; ========================================
;; Java Interop
;; ========================================
;; * There's a direct mapping

;; Create an instance of a class
(java.util.Date.)

;; Call a method
(let [now (java.util.Date.)]
  (.toString now))

(.length "omg this is a java string")

;; since Date is imported, don't need the entire namespace
(Date.)

;; You try:
;; * get the index of the first occurrence of a substring
;;   https://docs.oracle.com/javase/7/docs/api/java/lang/String.html


;; If you want to do multiple things to an object, there's the `doto`
;; macro:
(comment
  (doto (SimpleEmail.)
    (.setHostName "smtp.gmail.com")
    (.setSslSmtpPort "465")
    (.setSSL true)
    (.addTo "you@you.com")
    (.setFrom "from-address@whatever.com" "Me me me")
    (.setSubject "Subject goes here")
    (.setMsg "Some kind of email message")
    (.send))

  ;; above is equivalent to
  (let [email (SimpleEMail.)]
    (.setHostName email "smtp.gmail.com")
    (.setSslSmtpPort email "465")))

;; Call a static method
(Math/abs -3)



;; ========================================
;; Selenium example
;; ========================================

(defn search-for-cheese
  []
  (let [driver (FirefoxDriver.)]
    (.get driver "http://www.google.com")

    (doto (.findElement driver (By/name "q"))
      ;; this into-array bit is one of the annoyances you have to deal
      ;; with when working with Java libs
      (.sendKeys (into-array String ["Cheese!"]))
      (.submit))

    (Thread/sleep 1000)
    
    (println "Page title is:" (.getTitle driver))

    ;; closes the browser
    (.quit driver)))
