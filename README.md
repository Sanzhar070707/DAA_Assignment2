# DAA Assignment 2: Data Structures Benchmarking

This repository contains the implementation and performance analysis of core data structures (`DynamicArray`, `MyLinkedList`, and `MinHeap`) for the Design and Analysis of Algorithms (DAA) course.

## 📂 Project Structure
- `src/main/java/` — Core data structures, operation metrics, and benchmark runner.
- `src/test/java/` — Unit tests verifying structural correctness.
- `results/results.csv` — Raw benchmark performance data (time, steps, moves, comparisons).
- `results/plots/` — Generated performance charts (Time vs $n$ and Metrics vs $n$).
- `REPORT.md` — Detailed analysis and technical discussion.

## 🚀 Getting Started

### 1. Run Benchmarks (Java)
Execute the benchmark runner to generate performance metrics:
```bash
mvn clean test