# Employee Management (Java)

Console application for managing employee groups, built as a university project at AGH University of Krakow (Computational Engineering). The focus is on the Java Collections Framework, `Comparable` / `Comparator` and the Stream API.

## Features

**Employees**
- Employee data: first name, last name, condition (present, business trip, sick, absent), birth year, salary
- Natural ordering by last name (`Comparable`)

**Employee groups (`ClassEmployee`)**
- Adding employees with duplicate detection and a group capacity limit
- Removing employees, changing their condition, raising salaries
- Searching by last name (`Comparator`) and partial search by name fragment
- Counting employees by condition
- Sorting by name and by salary (descending), finding the top earner (`Collections.max`)

**Group container (`ClassContainer`)**
- Groups stored in a `Map` keyed by group name
- Adding and removing groups, finding empty groups, summary with fill percentage
- Three storage modes (`StorageMode`): `HashMap`, `LinkedHashMap`, `TreeMap`

**Statistics and analysis**
- Median salary, grouping employees by condition (`Collectors.groupingBy`)
- Salary filters: minimum, range, top / bottom earners, percentile (`SalaryFilter`)
- Salary distribution, outlier detection and Gini coefficient (`SalaryAnalyzer`)
- Age statistics: oldest / youngest employee (`Optional`), average age
- Condition checks with `anyMatch` / `allMatch` / `noneMatch` (`ConditionAnalyzer`)

## Tech stack

Java · Collections Framework (`List`, `Map`, `Set`) · `Comparable` / `Comparator` · enums · Stream API · `Optional`

## Project structure

| Class | Responsibility |
| --- | --- |
| `Employee` | Employee model, comparison by last name |
| `EmployeeCondition` | Enum of employee conditions |
| `ClassEmployee` | Single employee group and its operations |
| `ClassContainer` | Collection of groups, storage modes |
| `StorageMode` | Map implementation used by the container |
| `SalaryFilter`, `SalaryAnalyzer`, `SalaryDistribution` | Salary filtering and statistics |
| `AgeStatistics`, `GroupStatistics`, `ConditionAnalyzer` | Age, group and condition statistics |
| `Main` | Demonstration of all features |
| `ManualTest` | Manual tests of selected features |

## How to run

Requirements: JDK 17 or newer.

From IntelliJ IDEA: open the project and run `Main`.

From the terminal:

    javac -d out src/*.java
    java -cp out Main

## What I learned

- Choosing the right collection for the task (`HashMap` vs `LinkedHashMap` vs `TreeMap`)
- Implementing `equals` / `hashCode` and writing custom comparators
- Processing data with the Stream API instead of manual loops
- Reviewing and testing AI-generated code: part of the project was developed with an AI assistant, and the generated code was verified by running and testing it
