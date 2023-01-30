


# Mastering Clojure

This is the code repository for [Mastering Clojure](https://www.packtpub.com/application-development/mastering-clojure?utm_source=github&utm_medium=repository&utm_campaign=9781785889745), published by Packt. It contains all the supporting project files necessary to work through the book from start to finish.

## Instructions and Navigation

All of the code is organized into folders.
Each folder starts with `c` followed by the chapter number.
For example, `src/m_clj/c1/recur.clj` is a file from chapter 1, and `spec/m_clj/c10/speclj.clj` is one from chapter 10.

Similarly, each chapter will have many namespaces.
Each namespace starts with `m-clj.c` followed by the chapter
number.
For example, `m-clj.c1.recur` is a namespace from chapter 1.

Run the `lein repl` command and load the namespaces as follows.

```
$ lein repl
m-clj.core=> (load-file "src/m_clj/c1/recur.clj")
#'m-clj.c1-recur/factorial-ctco
m-clj.core=> (in-ns 'm-clj.c1.recur)
#<Namespace m-clj.c1-recur>
m-clj.c1-recur=> (fibo 10)
```

All of the ClojureScript examples can be run through web pages in the `resources/html/` folder.
Run `lein cljsbuild once` before you open these pages in a browser.

## Related Clojure books

* [Clojure for Machine Learning](https://www.packtpub.com/big-data-and-business-intelligence/clojure-machine-learning?utm_source=github&utm_medium=related&utm_campaign=9781783284351)
* [Mastering Clojure Data Analysis](https://www.packtpub.com/big-data-and-business-intelligence/mastering-clojure-data-analysis?utm_source=github&utm_medium=related&utm_campaign=9781783284139)
* [Building Web Applications with Clojure](https://www.packtpub.com/web-development/building-web-applications-clojure-video?utm_source=github&utm_medium=related&utm_campaign=9781783286157)
### Download a free PDF

 <i>If you have already purchased a print or Kindle version of this book, you can get a DRM-free PDF version at no cost.<br>Simply click on the link to claim your free PDF.</i>
<p align="center"> <a href="https://packt.link/free-ebook/9781785889745">https://packt.link/free-ebook/9781785889745 </a> </p>