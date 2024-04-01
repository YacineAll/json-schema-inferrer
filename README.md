# json-schema-inferrer

[![Build Status](https://github.com/YacineAll/json-schema-inferrer/workflows/Scala%20CI/badge.svg)](https://github.com/YacineAll/json-schema-inferrer/actions)
[![Codecov coverage](https://codecov.io/gh/YacineAll/json-schema-inferrer/branch/main/graph/badge.svg)](https://codecov.io/gh/YacineAll/json-schema-inferrer)
[![CodeFactor](https://www.codefactor.io/repository/github/yacineall/json-schema-inferrer/badge)](https://www.codefactor.io/repository/github/yacineall/json-schema-inferrer)
[![License](https://img.shields.io/github/license/YacineAll/json-schema-inferrer)](https://github.com/YacineAll/json-schema-inferrer/blob/main/LICENSE)

Scala library for automating JSON Schema generation from data and merging schemas for comprehensive data modeling.

## Features

- Infer JSON Schema from JSON data
- Merge multiple JSON Schemas into a single comprehensive schema
- Customize schema generation with user-defined options
- Support for both JSON Schema Draft 4 and Draft 7

## Getting Started

### Prerequisites

- Scala 2.13 or later
- sbt 1.x or later

### Installation

To use the json-schema-inferrer library in your Scala project, add the following dependency to your `build.sbt` file:

```scss
libraryDependencies += "com.github.yacineall" %% "json-schema-inferrer" % "0.1.0"

