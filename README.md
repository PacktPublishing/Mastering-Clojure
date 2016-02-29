# Mastering Clojure

You can have a look at [Mastering Clojure] (https://www.packtpub.com/application-development/mastering-clojure) book

## Testing code

Each chapter will have multiple chapter folders.
Each chapter folder starts with `c` followed by the chapter number.
For example, `src/m_clj/c1/recur.clj` is a file from chapter 1.

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
m-clj.c1-recur=> (factorial-ctco 10)
```
## Hardware requirements

CPU (Minimum: Intel Core 2 Duo or AMD Athlon, Recommended: Intel Core i5 or AMD FX)
Memory (Minimum: 2GB, Recommended: 4GB)

## Related Clojure books:

1. [Clojure for Machine Learning] (https://www.packtpub.com/big-data-and-business-intelligence/clojure-machine-learning)
2. [Mastering Clojure Data Analysis](https://www.packtpub.com/big-data-and-business-intelligence/mastering-clojure-data-analysis)
3. [Building Web Applications with Clojure [Video]] (https://www.packtpub.com/web-development/building-web-applications-clojure-video)


## eBooks, discount offers, and more

Did you know that Packt offers eBook versions of every book published, with PDF and ePub files available? You can upgrade to the eBook version at www.PacktPub.com and as a print book customer, you are entitled to a discount on the eBook copy. Get in touch with us at customercare@packtpub.com for more details.
At [PacktPub] (www.PacktPub.com), you can also read a collection of free technical articles, sign up for a range of free newsletters and receive exclusive discounts and offers on Packt books and eBooks.

Do you need instant solutions to your IT questions? PacktLib is Packt's online digital book library. Here, you can search, access, and read Packt's entire library of books.
Why subscribe?
•	Fully searchable across every book published by Packt
•	Copy and paste, print, and bookmark content
•	On demand and accessible via a web browser



