(ns training.exercises.ex12-java-interop)

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

;; You try:
;; * get the index of the first occurrence of a substring

;; If you want to do multiple things, there's the `doto` macro:
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


