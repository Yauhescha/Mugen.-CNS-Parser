# MUGEN CNS Parser

A Java reader for MUGEN character constants and state definitions. It maps the main character sections and parsed state definitions into Java objects.

## Parsed data

`CnsData` contains:

- `data`: general character constants.
- `size`: dimensions and related properties.
- `velocity`: movement velocity values.
- `movement`: movement constants.
- `statedefList`: parsed state definitions and their states.

Values in the constant sections are stored as strings, preserving their text representation for later interpretation.

## Development setup

The project targets **Java 11** and uses Lombok. Import `pom.xml` in a Maven-capable IDE and enable annotation processing.

The checked-in Maven `artifactId` is `Mugen CNS Parser`, which contains spaces. Normalize it to an identifier such as `mugen-cns-parser` before using standard Maven build/publishing workflows. Then compile with:

```sh
mvn compile
```

## Usage

```java
import com.hescha.cnsparser.reader.CnsReader;
import java.io.File;

var data = new CnsReader().read(new File("character.cns"));
System.out.println(data.getVelocity());
System.out.println(data.getStatedefList().size());
```

Supply a real CNS file. `CnsReader` also contains a `main` method with an example file path; update that path before running the example.

## Source layout

| Package | Purpose |
| --- | --- |
| `com.hescha.cnsparser.reader` | File-reading and section-parsing logic |
| `com.hescha.cnsparser.model` | `CnsData`, `Statedef` and `State` models |

This reader is an experiment in parsing character data. It does not execute state controllers or evaluate MUGEN expressions, and its accepted syntax should be checked against the files used by your tooling.
