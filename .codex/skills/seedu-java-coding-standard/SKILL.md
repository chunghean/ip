---
name: seedu-java-coding-standard
description: Apply the SE-EDU basic and intermediate Java coding conventions to code in this project.
---

# SE-EDU Java Coding Standard

Use this skill for all Java code changes in this project. Follow the [SE-EDU Java coding
standard](https://se-education.org/guides/conventions/java/intermediate.html); use the Google
Java Style Guide for topics the SE-EDU guide does not cover.

- Put every class in a logically named, all-lowercase package, with the source path matching it.
- Use PascalCase nouns for classes and enums, camelCase verbs for methods, camelCase variables,
  and SCREAMING_SNAKE_CASE constants. Keep boolean names readable with prefixes such as `is`,
  `has`, or `was`; test methods may use `featureUnderTest_testScenario_expectedBehavior`.
- Use four-space indentation, K&R braces, consistent explicit imports, and a maximum line length
  of 120 characters. Prefer shorter lines and wrap at readable boundaries.
- Initialize variables at declaration when practical and keep them in the smallest useful scope.
  Keep fields encapsulated; do not expose mutable class variables publicly.
- Always use braces for loops and conditionals. Keep `else` on the same line as the preceding
  closing brace, and make intentional switch fallthrough explicit with `// Fallthrough`.
- Write English, American-spelling comments. Add descriptive Javadocs to public classes and
  public methods, except getters/setters, overriding methods whose inherited documentation applies,
  and test code. Begin method summaries with an action such as “Returns” or “Adds”.
- Separate logical units with blank lines and place comments at the indentation level of the code
  they describe.
