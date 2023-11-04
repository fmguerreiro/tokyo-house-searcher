## Real-estate alert

List your preferences and subscribe to them to receive alerts for your dream haunted home in Japan.

Supports suumo.jp results for now.

## Requirements

- [docker](https://docs.docker.com/engine/install)
- [lein](https://leiningen.org/#install)
- [node.js](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)
- [java](https://adoptopenjdk.net/)
- [clojure cli](https://clojure.org/guides/getting_started)

With these installed, run
```bash
yarn install
```

to install javascript dependencies.

## Running

To run this application in development mode, start a shadow-cljs server with
```bash
npx shadow-cljs -d nrepl:0.7.0-beta1 -d cider/piggieback:0.4.2 -d refactor-nrepl:2.5.0 -d cider/cider-nrepl:0.25.0-SNAPSHOT server
```

Alternatively, CIDER users can run `cider-jack-in-cljs`. With this running, you can control compilation by accessing the shadow-cljs server at http://localhost:9630 and access the application at http://localhost:8080.

### Running the parser

``` shell
clj -T:build uber
java -jar target/parser/core-0.0.1-standalone.jar
```

### Running the scraping job

``` shell
clojure -M -m app.scraper
```
