## Useful Oshima-teru

List your preferences and subscribe to them to receive alerts for your dream haunted home in Japan.

Supports suumo.jp results for now.

## Requirements

- [docker](https://docs.docker.com/engine/install)
- [lein](https://leiningen.org/#install)


## Build

```
bash build.sh
```

## Options

```
  Switches               Default  Desc
  --------               -------  ----
  -i                              File where to read configuration from. Defaults to resources/example.conf
  -o                              File where to store the results. Defaults to stdout.
  -h, --no-help, --help  false    Shows this help
```

## Run

```
./useful-oshima-teru
```

Or if you prefer less speed/native image doesn't work for you:

```
java -jar ./target/app-0.1.0-SNAPSHOT-standalone.jar
```

## Future Work
Support non-haunted houses.
