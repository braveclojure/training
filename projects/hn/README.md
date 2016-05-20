# hn

The purpose of this project is get experience interacting with a Java
library from a Clojure project. You'll do this by using Selenium to
visit Hacker News (http://news.ycombinator.com) and scrape link
titles.

[All the Selenium commands and operations](http://www.seleniumhq.org/docs/03_webdriver.jsp#introducing-the-selenium-webdriver-api-by-example)
has everything you'll need.

You'll need to install Firefox.

For extra credit, visit each link and get the name of every
commentor. Place the result in a vector of maps, like this:

```clojure
[{:title "Anguish: Invisible Programming Language and Invisible Data Theft"
  :commentors #{"danra" "yokohummer7" "lolc"}}]
```

Save this to the file "hn-scrape.edn".
