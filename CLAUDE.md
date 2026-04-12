# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build

This is a Maven project (Java 8). Build with:

```bash
mvn clean package
```

Run tests:

```bash
mvn test
```

## Project Overview

Offline user visit session analysis system built on Apache Spark 2.1.0 (Scala 2.11). Reads user behavior logs and user info from CSV files, loads them into Spark SQL temp tables, then performs session-granularity aggregation, filtering, and statistical analysis. Results are persisted to MySQL.

## Architecture

- Entry point: `UserVisitSessionAnalyzeSpark.main()` in the `ananlysis` package (note: package name has a typo — "ananlysis" not "analysis")
- Configuration is loaded once from `src/main/resources/analysis.properties` via `ConfigurationManager` (static initializer, singleton pattern)
- Local vs production mode is controlled by the `spark.local` property — local uses `SQLContext`, production uses `HiveContext`
- Data loading: `SparkUtils.loadData()` reads CSV files, maps them to `UserVisitLog`/`User` POJOs, and registers as temp tables `user_visit_action` and `user_info`
- DAO layer uses a factory pattern (`DAOFactory`) with a custom JDBC connection pool (`JDBCHelper`, singleton with double-checked locking)
- Aggregated data uses a `key=value|key=value` concatenated string format throughout — parsed/updated via `StringUtils` utility methods
- Session statistics use a custom `Accumulator<String>` (`SessionAggrStatAccumulator`) to count visit duration and step length distributions across the cluster

## Key Analysis Pipeline

1. Load behavior data and user data into Spark SQL tables
2. Query task parameters from MySQL (task id, date range, filter criteria)
3. Aggregate actions by session — compute search keywords, click categories, visit duration, step length
4. Join session data with user info (age, profession, city, sex)
5. Filter sessions by task parameters using `ValidUtils`
6. Compute session duration/step distribution ratios via Accumulator, persist to MySQL
7. Calculate top 10 hot categories (by click/order/pay counts) using custom `CategorySortKey` for secondary sorting
8. Find top 10 active sessions per hot category, persist to MySQL
