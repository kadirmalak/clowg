# clowg

A Clojure library for generating Clojure wrappers around Java

## Installing

- Leiningen/Boot

```
[clowg "0.1.1"]
```

## Usage

```
(require '[clowg.core :refer [get-code]])
```

Wrap String class

```
(get-code String)
=>
"(defn
  getChars
  ([^java.lang.String this a b c d]
   (.getChars this (int a) (int b) (char-array c) (int d))))
 (defn trim ([^java.lang.String this] (.trim this)))
 (defn
  toLowerCase
  ([^java.lang.String this] (.toLowerCase this))
  ([^java.lang.String this ^java.util.Locale a] (.toLowerCase this a)))
 (defn
  replaceAll
  ([^java.lang.String this ^java.lang.String a ^java.lang.String b]
   (.replaceAll this a b)))
 (defn
  contains
  ([^java.lang.String this ^java.lang.CharSequence a]
   (.contains this a)))
 (defn
  endsWith
  ([^java.lang.String this ^java.lang.String a] (.endsWith this a)))
 (defn
  replaceFirst
  ([^java.lang.String this ^java.lang.String a ^java.lang.String b]
   (.replaceFirst this a b)))
 (defn toString ([^java.lang.String this] (.toString this)))
 (defn
  subSequence
  ([^java.lang.String this a b] (.subSequence this (int a) (int b))))
 (defn
  substring
  ([^java.lang.String this a] (.substring this (int a)))
  ([^java.lang.String this a b] (.substring this (int a) (int b))))
 (defn
  equalsIgnoreCase
  ([^java.lang.String this ^java.lang.String a]
   (.equalsIgnoreCase this a)))
 (defn toCharArray ([^java.lang.String this] (.toCharArray this)))
 (defn charAt ([^java.lang.String this a] (.charAt this (int a))))
 (defn
  split
  ([^java.lang.String this ^java.lang.String a] (.split this a))
  ([^java.lang.String this ^java.lang.String a b]
   (.split this a (int b))))
 (defn
  toUpperCase
  ([^java.lang.String this] (.toUpperCase this))
  ([^java.lang.String this ^java.util.Locale a] (.toUpperCase this a)))
 (defn
  concat
  ([^java.lang.String this ^java.lang.String a] (.concat this a)))
 (defn
  offsetByCodePoints
  ([^java.lang.String this a b]
   (.offsetByCodePoints this (int a) (int b))))
 (defn length ([^java.lang.String this] (.length this)))
 (defn
  regionMatches
  ([^java.lang.String this a ^java.lang.String b c d]
   (.regionMatches this (int a) b (int c) (int d)))
  ([^java.lang.String this a b ^java.lang.String c d e]
   (.regionMatches this (boolean a) (int b) c (int d) (int e))))
 (defn
  matches
  ([^java.lang.String this ^java.lang.String a] (.matches this a)))
 (defn intern ([^java.lang.String this] (.intern this)))
 (defn
  startsWith
  ([^java.lang.String this ^java.lang.String a] (.startsWith this a))
  ([^java.lang.String this ^java.lang.String a b]
   (.startsWith this a (int b))))
 (defn hashCode ([^java.lang.String this] (.hashCode this)))
 (defn isEmpty ([^java.lang.String this] (.isEmpty this)))
 (defn
  codePointCount
  ([^java.lang.String this a b] (.codePointCount this (int a) (int b))))
 (defn
  codePointBefore
  ([^java.lang.String this a] (.codePointBefore this (int a))))
 (defn
  equals
  ([^java.lang.String this ^java.lang.Object a] (.equals this a)))
 (defn
  compareToIgnoreCase
  ([^java.lang.String this ^java.lang.String a]
   (.compareToIgnoreCase this a)))
 (defn
  codePointAt
  ([^java.lang.String this a] (.codePointAt this (int a))))
 "
```

Or if you want to refer to a specific instance all the time (omits *this*)

```
(get-code String 'the-string)
=>
"(defn
  getChars
  ([a b c d]
   (.getChars the-string (int a) (int b) (char-array c) (int d))))
 (defn trim ([] (.trim the-string)))
 (defn
  toLowerCase
  ([] (.toLowerCase the-string))
  ([^java.util.Locale a] (.toLowerCase the-string a)))
 (defn
  replaceAll
  ([^java.lang.String a ^java.lang.String b]
   (.replaceAll the-string a b)))
 (defn contains ([^java.lang.CharSequence a] (.contains the-string a)))
 (defn endsWith ([^java.lang.String a] (.endsWith the-string a)))
 (defn
  replaceFirst
  ([^java.lang.String a ^java.lang.String b]
   (.replaceFirst the-string a b)))
 (defn toString ([] (.toString the-string)))
 (defn subSequence ([a b] (.subSequence the-string (int a) (int b))))
 (defn
  substring
  ([a] (.substring the-string (int a)))
  ([a b] (.substring the-string (int a) (int b))))
 (defn
  equalsIgnoreCase
  ([^java.lang.String a] (.equalsIgnoreCase the-string a)))
 (defn toCharArray ([] (.toCharArray the-string)))
 (defn charAt ([a] (.charAt the-string (int a))))
 (defn
  split
  ([^java.lang.String a] (.split the-string a))
  ([^java.lang.String a b] (.split the-string a (int b))))
 (defn
  toUpperCase
  ([] (.toUpperCase the-string))
  ([^java.util.Locale a] (.toUpperCase the-string a)))
 (defn concat ([^java.lang.String a] (.concat the-string a)))
 (defn
  offsetByCodePoints
  ([a b] (.offsetByCodePoints the-string (int a) (int b))))
 (defn length ([] (.length the-string)))
 (defn
  regionMatches
  ([a ^java.lang.String b c d]
   (.regionMatches the-string (int a) b (int c) (int d)))
  ([a b ^java.lang.String c d e]
   (.regionMatches the-string (boolean a) (int b) c (int d) (int e))))
 (defn matches ([^java.lang.String a] (.matches the-string a)))
 (defn intern ([] (.intern the-string)))
 (defn
  startsWith
  ([^java.lang.String a] (.startsWith the-string a))
  ([^java.lang.String a b] (.startsWith the-string a (int b))))
 (defn hashCode ([] (.hashCode the-string)))
 (defn isEmpty ([] (.isEmpty the-string)))
 (defn
  codePointCount
  ([a b] (.codePointCount the-string (int a) (int b))))
 (defn codePointBefore ([a] (.codePointBefore the-string (int a))))
 (defn equals ([^java.lang.Object a] (.equals the-string a)))
 (defn
  compareToIgnoreCase
  ([^java.lang.String a] (.compareToIgnoreCase the-string a)))
 (defn codePointAt ([a] (.codePointAt the-string (int a))))
 "
```

If you want the actual code generated, use *make-functions* instead of *get-code*

```
(require '[clowg.core :refer [make-functions]])

(make-functions Integer)
=>
((defn shortValue ([^java.lang.Integer this] (.shortValue this)))
 (defn doubleValue ([^java.lang.Integer this] (.doubleValue this)))
 (defn longValue ([^java.lang.Integer this] (.longValue this)))
 (defn byteValue ([^java.lang.Integer this] (.byteValue this)))
 (defn toString ([^java.lang.Integer this] (.toString this)))
 (defn floatValue ([^java.lang.Integer this] (.floatValue this)))
 (defn intValue ([^java.lang.Integer this] (.intValue this)))
 (defn hashCode ([^java.lang.Integer this] (.hashCode this)))
 (defn equals ([^java.lang.Integer this ^java.lang.Object a] (.equals this a))))

```

## TODO

- Generate static methods
- Generate field accessors
- Add support for Java method overloads with same arity (they're skipped for now)

## License

Copyright © 2020 Kadir Malak

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
