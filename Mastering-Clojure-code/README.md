# Mastering Clojure

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
